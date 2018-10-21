package com.project.jvvas.moneyskills;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordActivity extends AppCompatActivity {

    private EditText mRegisteredEmail ;
    private Button mResetPassword;
    private FirebaseAuth mAuth;
    private ImageView arrowBack ;


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

        setContentView(R.layout.activity_password);

        arrowBack        = (ImageView)findViewById(R.id.imageView_ArrowBack);
        mResetPassword   = (Button)   findViewById(R.id.button_LoginPassword);
        mRegisteredEmail = (EditText) findViewById(R.id.editText_RegisteredEmail);

        mResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = mRegisteredEmail.getText().toString().trim();

                if(userEmail.equals(""))
                {
                    Toast.makeText(PasswordActivity.this, "Please enter your registered email ID" , Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(PasswordActivity.this , "Password reset email sent!", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(PasswordActivity.this , LoginActivity.class));
                            }
                            else
                            {
                                Toast.makeText(PasswordActivity.this , "Error in sending password reset email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });



        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(PasswordActivity.this , LoginActivity.class));
            }
        });

    }
}
