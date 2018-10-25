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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class  LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private ProgressBar progressBar;
    private Button buttonLogin;
    private EditText editTextEmail, editTextPassword;
    private ImageView imageViewEyeHide , imageViewEyeShow ;
    private TextView textViewSignUp , textViewForgotPassword;



    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Remove Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initialize Firebase auth
        mAuth = FirebaseAuth.getInstance();

        // set Layout
        setContentView(R.layout.activity_login);

        initializeTheButtons();

        // ----------- Click On -----------
        imageViewEyeHide.setOnClickListener(this);
        imageViewEyeShow.setOnClickListener(this);
        textViewSignUp  .setOnClickListener(this);
        buttonLogin     .setOnClickListener(this);
        textViewForgotPassword.setOnClickListener(this);

    }

    private void initializeTheButtons()
    {
        imageViewEyeShow = findViewById(R.id.imageView_eyeShow);
        imageViewEyeShow.setVisibility(View.GONE);
        imageViewEyeHide = findViewById(R.id.imageView_eyeHide);
        textViewSignUp   = findViewById(R.id.textView_Signup);
        buttonLogin      = findViewById(R.id.button_ResetPassword);
        editTextEmail    = findViewById(R.id.editText_RegisteredEmail);
        editTextPassword = findViewById(R.id.editText_Password);
        progressBar      = findViewById(R.id.progress_Bar);
        textViewForgotPassword = findViewById(R.id.textView_ForgotPassword);
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

            case R.id.button_ResetPassword:
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