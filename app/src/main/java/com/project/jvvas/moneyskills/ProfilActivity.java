package com.project.jvvas.moneyskills;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.project.jvvas.moneyskills.Config.Config;

import org.json.JSONException;

import java.io.IOException;
import java.math.BigDecimal;

public class ProfilActivity extends AppCompatActivity implements View.OnClickListener, DialogPaymentMethod.dialogPaymentMethodListener  {

    private static final int CHOOSE_IMAGE = 101;
    // Get intent
    private Intent intent ;

    // Firebase
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    // Get a reference to our posts
    private int coins ;
    private String fullName;
    private StorageReference profileImageRef;

    private Uri uriProfileImage;
    private String profileImageUrl;
    private ProgressBar mProgressBarProfile;
    private ImageView arrowBack , insertMoney , mImageView_ProfileIcon;
    private TextView mTextView_Add , mTextViewName , mTextView_Coins;

    // ---------------------- PAY PAL --------------------------------
    private String currency = "EUR" ;
    private String paypalAmount = "0" ; // , totalAmount = "0";
    private static final int PAYPAL_REQUEST_CODE = 7171 ;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)  // Use Sandbox because we are on test
            .clientId(Config.PAYPAL_CLIENT_ID) ;

    // -------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        //String uid = user.getUid();

        setContentView(R.layout.activity_profil);

        // Initialize the buttons
        initializeTheButtons();

        // Retrieve the data of Real-time database
        retrieveTheData();

        intent = getIntent();

        //-----------------------   PAY PAL Service   ----------------------------
        Intent paypalIntent = new Intent(this , PayPalService.class) ;
        paypalIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION , config ) ;
        startService(paypalIntent) ;


        // ----------- Click On -----------
        mTextView_Add.setOnClickListener(this);
        arrowBack.setOnClickListener(this);
        insertMoney.setOnClickListener(this);
    }

    private void retrieveTheData()
    {
        if(user != null)
        {

            // Retrieve the name,coins of the user from the db
            DatabaseReference ref = database.getReference("Users").child(user.getUid()) ;

            // Attach a listener to read the data at our reference
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    User currentUser = dataSnapshot.getValue(User.class) ;
                    fullName = currentUser.fullName ;
                    coins   = currentUser.coins ;

                    // Create a string from the int 'coins'
                    String stringCoins = Integer.toString(coins);

                    mTextViewName.setText(fullName.toString());
                    mTextView_Coins.setText(stringCoins.toString());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });

            if (user.getPhotoUrl() != null) {
                String photoUrl = user.getPhotoUrl().toString();
                Glide.with(this)
                        .load(photoUrl)
                        .into(mImageView_ProfileIcon);
            }
        }
    }

    private void initializeTheButtons()
    {
        mTextView_Coins         = (TextView)    findViewById(R.id.textView_Coins);
        mTextView_Add           = (TextView)    findViewById(R.id.textView_Add);
        mTextViewName           = (TextView)    findViewById(R.id.textView_DisplayName);
        mImageView_ProfileIcon  = (ImageView)   findViewById(R.id.imageView_Profil);
        insertMoney             = (ImageView)   findViewById(R.id.imageView_insertMoney);
        mProgressBarProfile     = (ProgressBar) findViewById(R.id.progressBar_Profile);
        arrowBack               = (ImageView)   findViewById(R.id.imageView_ArrowBack);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this , PayPalService.class)) ;
        super.onDestroy();
    }

    @Override
    public void onStart()
    {
        super.onStart();

        if(user == null)
        {
            finish();
            startActivity(new Intent(this , LoginActivity.class));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.textView_Add:
                showImageChooser();
                break;

            case R.id.imageView_ArrowBack:
                goToSingleMulti();
                break;

            case R.id.imageView_insertMoney:
                openDialog();
                break;
        }
    }

    private void openDialog()
    {

        // if there is internet connection
        if(isNetworkAvailable())
        {
            DialogPaymentMethod dialog = new DialogPaymentMethod();
            dialog.show(getSupportFragmentManager() , "Payment Method");
        }
        else
        {
            Toast.makeText(ProfilActivity.this, "No network connection.", Toast.LENGTH_LONG).show();
        }

    }

    private void goToSingleMulti()
    {
        Intent singleMultiIntent = new Intent(ProfilActivity.this , SingleMultiPlayer.class);

        startActivity(singleMultiIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PAYPAL_REQUEST_CODE){

            if(resultCode == RESULT_OK)
            {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION) ;

                if(confirmation != null)
                {
                    // If payment method is successful
                    try{

                        int paypalCoins = Integer.parseInt(paypalAmount);
                        coins += paypalCoins ;

                        // Create a string from the int 'coins'
                        String stringCoins = Integer.toString(coins);
                        mTextView_Coins.setText(stringCoins) ;

                        // Retrieve the coins of the user from the db
                        DatabaseReference ref = database.getReference("Users").child(user.getUid()) ;
                        // Store the coins in the database
                        ref.child("coins").setValue(coins);

                        String paymentDetails = confirmation.toJSONObject().toString(4);

                        startActivity(new Intent(this , PaymentDetails.class)
                                        .putExtra("PaymentDetails" , paymentDetails)
                                        .putExtra("PaymentAmount" , paypalAmount)
                                        .putExtra("Currency" , currency)
                        );
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            else if(resultCode == Activity.RESULT_CANCELED)
            {
                Toast.makeText(this, "Cancel" , Toast.LENGTH_SHORT).show();
            }

        }
        else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID)
        {
            Toast.makeText(this , "Invalid" , Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == CHOOSE_IMAGE  && resultCode == RESULT_OK && data != null && data.getData() != null )
        {
            uriProfileImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver() , uriProfileImage);
                mImageView_ProfileIcon.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void uploadImageToFirebaseStorage()
    {
        profileImageRef =
                FirebaseStorage.getInstance().getReference("profilePictures/" + System.currentTimeMillis() + ".jpg");

        if(uriProfileImage != null)
        {
            mProgressBarProfile.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage)
                  .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                      @Override
                      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                      {
                          mProgressBarProfile.setVisibility(View.GONE);
                          profileImageUrl = taskSnapshot.getDownloadUrl().toString();
                          saveUserInformation();
                      }
                  })
                  .addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e)
                      {
                          mProgressBarProfile.setVisibility(View.GONE);
                          Toast.makeText(ProfilActivity.this , e.getMessage() , Toast.LENGTH_SHORT).show();
                      }
                  });
        }
    }

    private void saveUserInformation()
    {

        String displayName = mTextViewName.getText().toString() ;

        if(user != null && profileImageUrl != null)
        {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(ProfilActivity.this , "Profile updated" , Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void showImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent , "Select Profile Image") , CHOOSE_IMAGE);
    }

    @Override
    public void processPayment(String currentPaypalAmount , String currentCurrency) {

        currency = currentCurrency;

        paypalAmount = currentPaypalAmount ;

        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(currentPaypalAmount)) , currency  , "Buy Coins" , PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intentPayment  = new Intent(this , PaymentActivity.class) ;
        intentPayment.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION , config ) ;
        intentPayment.putExtra(PaymentActivity.EXTRA_PAYMENT , payPalPayment) ;
        startActivityForResult(intentPayment , PAYPAL_REQUEST_CODE ) ;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
