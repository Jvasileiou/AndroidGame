package com.project.jvvas.moneyskills;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends LoginActivity implements View.OnClickListener{

    private ProgressBar progressBar;
    private EditText editTextFullname ,editTextEmail ,editTextPassword ,editTextConfirmPassword;

    private ImageView imageViewEyeHide1 , imageViewEyeShow1;
    private ImageView imageViewEyeHide2 , imageViewEyeShow2;


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        imageViewEyeHide1       = (ImageView) findViewById(R.id.imageView_eyeHide1);
        imageViewEyeShow1       = (ImageView) findViewById(R.id.imageView_eyeShow1);
        imageViewEyeHide2       = (ImageView) findViewById(R.id.imageView_eyeHide2);
        imageViewEyeShow2       = (ImageView) findViewById(R.id.imageView_eyeShow2);

        imageViewEyeShow1.setVisibility(View.GONE);
        imageViewEyeShow2.setVisibility(View.GONE);

        editTextFullname        = (EditText) findViewById(R.id.editText_Fullname);
        editTextEmail           = (EditText) findViewById(R.id.editText_Email);
        editTextPassword        = (EditText) findViewById(R.id.editText_Password);
        editTextConfirmPassword = (EditText) findViewById(R.id.editText_ConfirmPassword);
        progressBar             = (ProgressBar) findViewById(R.id.progress_Bar);

        mAuth = FirebaseAuth.getInstance();


        imageViewEyeHide1.setOnClickListener(this);
        imageViewEyeShow1.setOnClickListener(this);
        imageViewEyeHide2.setOnClickListener(this);
        imageViewEyeShow2.setOnClickListener(this);

        findViewById(R.id.button_SignUp).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);

    }


    private void registerUser(){

        final String fullName =        editTextFullname.getText().toString().trim();
        final String email    =        editTextEmail.getText().toString().trim();
        String password =        editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if(fullName.isEmpty()){
            editTextFullname.setError("Full name is required.");
            editTextFullname.requestFocus();
            return;
        }

        if(email.isEmpty()){
            editTextEmail.setError("Email is required.");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please enter a valid email.");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required.");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6 ){
            editTextPassword.setError("Minimum length of password is 6.");
            editTextPassword.requestFocus();
            return;
        }

        if(confirmPassword.isEmpty()){
            editTextConfirmPassword.setError("Confirm password is required.");
            editTextConfirmPassword.requestFocus();
            return;
        }

        if( !(password.equals(confirmPassword)) ){
            editTextConfirmPassword.setError("Passwords need to match.");
            editTextConfirmPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Create a new account
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            // Store the additional fields in db
                            User user = new User(fullName , email  );

                            mDatabase = FirebaseDatabase.getInstance().getReference("Users");

                            mDatabase.child(mAuth.getCurrentUser().getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressBar.setVisibility(View.GONE);

                                            if(task.isSuccessful()){
                                                Toast.makeText(getApplicationContext(), "User Registered successful", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            Intent intent = new Intent(SignUpActivity.this, SingleMultiPlayer.class);

                            // Clear all activities
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else
                        {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


        return;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.button_SignUp:
                registerUser();
                break;

            case R.id.textViewLogin:
                startActivity(new Intent(this, LoginActivity.class));
                break;

            case R.id.imageView_eyeHide1:
                showPassword1();
                break;

            case R.id.imageView_eyeShow1:
                hidePassword1();
                break;

            case R.id.imageView_eyeHide2:
                showPassword2();
                break;

            case R.id.imageView_eyeShow2:
                hidePassword2();
                break;
        }

    }

    private void hidePassword2()
    {
        imageViewEyeHide2.setVisibility(View.VISIBLE);
        imageViewEyeShow2.setVisibility(View.GONE);

        editTextConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private void showPassword2()
    {
        imageViewEyeShow2.setVisibility(View.VISIBLE);
        imageViewEyeHide2.setVisibility(View.GONE);

        editTextConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    }

    private void hidePassword1()
    {
        imageViewEyeHide1.setVisibility(View.VISIBLE);
        imageViewEyeShow1.setVisibility(View.GONE);

        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private void showPassword1()
    {
        imageViewEyeShow1.setVisibility(View.VISIBLE);
        imageViewEyeHide1.setVisibility(View.GONE);

        editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    }
}