package com.project.jvvas.moneyskills;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SingleMultiPlayer extends AppCompatActivity implements View.OnClickListener
{
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Intent intent ;

    private int coins;
    private int numberOfHearts = 0 ;
    private TextView textViewCoins;
    private ImageView mImageViewProfil ;
    private ImageView heart1 , heart2 , heart3 ;
    private Button mLogoutBtn , mTrainingBtn , mSingleBtn , mMultiBtn ;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Remove Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_single_multi_player);

        // Initialize Firebase Auth;
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        // Retrieve the data of Real-time Database
        retrieveTheData();

        // Initialize buttons
        initializeTheButtons();

        intent = getIntent();

        // ----------- Click On -----------
        mImageViewProfil.setOnClickListener(this);
        mLogoutBtn.setOnClickListener(this);
        mTrainingBtn.setOnClickListener(this);
        mSingleBtn.setOnClickListener(this);
        mMultiBtn.setOnClickListener(this);

        if(!isNetworkAvailable())
        {
            Toast.makeText(SingleMultiPlayer.this, "No network connection.", Toast.LENGTH_LONG).show();
        }

    }

    private void initializeTheButtons()
    {
        heart1              = (ImageView) findViewById(R.id.imageView_firstHeart);
        heart2              = (ImageView) findViewById(R.id.imageView_secondHeart);
        heart3              = (ImageView) findViewById(R.id.imageView_thirdHeart);
        mImageViewProfil    = (ImageView) findViewById(R.id.imageView_Profil);
        mLogoutBtn          = (Button) findViewById(R.id.Logout_btn);
        mTrainingBtn        = (Button) findViewById(R.id.button_Training);
        mSingleBtn          = (Button) findViewById(R.id.button_SinglePlayer);
        mMultiBtn           = (Button) findViewById(R.id.button_MultiPlayer);
        textViewCoins       = (TextView) findViewById(R.id.textView_Coins) ;
    }

    private void retrieveTheData()
    {
        if(user != null)
        {
            // Retrieve the coins of the user from the db
            final DatabaseReference ref = database.getReference("Users").child(user.getUid()) ;

            // Attach a listener to read the data at our reference
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    User currentUser = dataSnapshot.getValue(User.class) ;

                    numberOfHearts = currentUser.numberOfHearts ;
                    coins   = currentUser.coins ;
                    setTheImagesOfTheHearts() ;

                    // Create a string from the int 'coins'
                    String stringCoins = Integer.toString(coins);

                    textViewCoins.setText(stringCoins.toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }
    }

    private void setTheImagesOfTheHearts() {

        if(numberOfHearts == 0 ){
            heart1.setImageResource(R.drawable.empty_heart);
            heart2.setImageResource(R.drawable.empty_heart);
            heart3.setImageResource(R.drawable.empty_heart);
        }
        else if (numberOfHearts == 1 )
        {
            heart1.setImageResource(R.drawable.half_empty_heart);
            heart2.setImageResource(R.drawable.empty_heart);
            heart3.setImageResource(R.drawable.empty_heart);
        }
        else if(numberOfHearts == 2 )
        {
            heart1.setImageResource(R.drawable.full_heart);
            heart2.setImageResource(R.drawable.empty_heart);
            heart3.setImageResource(R.drawable.empty_heart);
        }
        else if (numberOfHearts == 3 )
        {
            heart1.setImageResource(R.drawable.full_heart);
            heart2.setImageResource(R.drawable.half_empty_heart);
            heart3.setImageResource(R.drawable.empty_heart);
        }
        else if(numberOfHearts == 4 )
        {
            heart1.setImageResource(R.drawable.full_heart);
            heart2.setImageResource(R.drawable.full_heart);
            heart3.setImageResource(R.drawable.empty_heart);
        }
        else if (numberOfHearts == 5 )
        {
            heart1.setImageResource(R.drawable.full_heart);
            heart2.setImageResource(R.drawable.full_heart);
            heart3.setImageResource(R.drawable.half_empty_heart);
        }
        else if (numberOfHearts == 6 )
        {
            heart1.setImageResource(R.drawable.full_heart);
            heart2.setImageResource(R.drawable.full_heart);
            heart3.setImageResource(R.drawable.full_heart);
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.Logout_btn:
                userLogout();
                break;

            case R.id.imageView_Profil:
                showProfile();
                break;

            case R.id.button_Training:
                startTraining();
                break;

            case R.id.button_SinglePlayer:
                startSingle();
                break;

            case R.id.button_MultiPlayer:
                startMulti();
                break;
        }
    }



    public void userLogout()
    {
        //String provider = user.getProviders().get(0);

        // if there is internet connection
        if(isNetworkAvailable())
        {
            // Sign out From Firebase
            mAuth.signOut();

            updateUI();
        }
        else
        {
            Toast.makeText(SingleMultiPlayer.this, "No network connection.", Toast.LENGTH_LONG).show();
        }

    }

    private void showProfile()
    {
        // if there is internet connection
        if(isNetworkAvailable())
        {
            Intent profileIntent = new Intent(SingleMultiPlayer.this , ProfilActivity.class);
            startActivity(profileIntent);
            finish();
        }
        else
        {
            Toast.makeText(SingleMultiPlayer.this, "No network connection.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            updateUI();
        }
    }

    public void  updateUI(){
        Toast.makeText(SingleMultiPlayer.this, "You're logged out ", Toast.LENGTH_LONG).show();

        Intent accountIntent = new Intent(SingleMultiPlayer.this,LoginActivity.class);
        startActivity(accountIntent);
        finish();
    }

    public void startSingle()
    {
        Intent accountIntent = new Intent(SingleMultiPlayer.this,GameActivity.class);
        startActivity(accountIntent);
        finish();
    }

    public void startMulti()
    {
        // if there is internet connection
        if(isNetworkAvailable())
        {
            Intent accountIntent = new Intent(SingleMultiPlayer.this,WaitingMulti.class);
            startActivity(accountIntent);
            finish();
        }
        else
        {
            Toast.makeText(SingleMultiPlayer.this, "No network connection.", Toast.LENGTH_LONG).show();
        }

    }

    public void startTraining()
    {
        Intent accountIntent = new Intent(SingleMultiPlayer.this,Training.class);
        startActivity(accountIntent);
        finish();
    }


    public void exitBut(View v)
    {
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
