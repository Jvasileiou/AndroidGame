package com.project.jvvas.moneyskills;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;


public class  LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private TwitterAuthClient mTwitterClient;

    private ImageView imageViewEyeHide , imageViewEyeShow ;

    private TextView textViewSignUp , textViewForgotPassword;
    private ProgressBar progressBar;
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;


    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initialize Firebase auth
        mAuth = FirebaseAuth.getInstance();


        // set Layout
        setContentView(R.layout.activity_login);

        imageViewEyeShow = findViewById(R.id.imageView_eyeShow);
        imageViewEyeShow.setVisibility(View.GONE);

        imageViewEyeHide = findViewById(R.id.imageView_eyeHide);
        textViewSignUp   = findViewById(R.id.textView_Signup);
        buttonLogin      = findViewById(R.id.button_LoginPassword);
        editTextEmail    = findViewById(R.id.editText_RegisteredEmail);
        editTextPassword = findViewById(R.id.editText_Password);
        progressBar      = findViewById(R.id.progress_Bar);
        textViewForgotPassword = findViewById(R.id.textView_ForgotPassword);

        // ----------- Click On -----------

        imageViewEyeHide.setOnClickListener(this);
        imageViewEyeShow.setOnClickListener(this);

        textViewSignUp  .setOnClickListener(this);
        buttonLogin     .setOnClickListener(this);
        textViewForgotPassword.setOnClickListener(this);

    }

    @Override
    public void onStart()
    {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            updateAuth();
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.textView_Signup:
                startActivity(new Intent(this, SignUpActivity.class));
                break;

            case R.id.button_LoginPassword:
                userLogin();
                break;

            case R.id.textView_ForgotPassword:
                resetPassword();
                break;

            case R.id.imageView_eyeHide:
                showPassword();
                break;

            case R.id.imageView_eyeShow:
                hidePassword();
                break;

        }
    }

    public void updateAuth()
    {

        Toast.makeText(LoginActivity.this, "You're logged in ", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(LoginActivity.this,SingleMultiPlayer.class);
        startActivity(intent);
        finish();
    }

    private void showPassword()
    {
        imageViewEyeShow.setVisibility(View.VISIBLE);
        imageViewEyeHide.setVisibility(View.GONE);

        editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    }

    private void hidePassword()
    {
        imageViewEyeHide.setVisibility(View.VISIBLE);
        imageViewEyeShow.setVisibility(View.GONE);

        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private void resetPassword()
    {
        startActivity(new Intent(LoginActivity.this , PasswordActivity.class));
        finish();
    }


    //////////////////////////////// APP LOGIN  ///////////////////////////////////////////


    private void userLogin(){

        String email    =  editTextEmail.getText().toString().trim();
        String password =  editTextPassword.getText().toString().trim();

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

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.GONE);

                if(task.isSuccessful()){
                    Intent intent = new Intent(LoginActivity.this, SingleMultiPlayer.class);

                    // Clear all activities
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}