package com.example.jhoanrivers.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.TreeMap;

public class LoginActivity extends AppCompatActivity {


    private EditText email,password;
    private Button loginbtn;
    private ProgressBar pb;
    private FirebaseAuth auth;
    private TextView register;
    private static final String TAG ="EmailPassword";
    private ProgressBar dpb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.emailtxt);
        password = (EditText) findViewById(R.id.passwordtxt);
        loginbtn = (Button) findViewById(R.id.btn_login);
        //pb = (ProgressBar) findViewById(R.id.progressbar);
        auth = FirebaseAuth.getInstance();
        register = (TextView) findViewById(R.id.registertxt);
        dpb = (ProgressBar) findViewById(R.id.determinateBar);

        dpb.setVisibility(View.GONE);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userSign(email.getText().toString(), password.getText().toString());
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

    }
    private boolean validateForm() {
        boolean valid = true;

        String inEmail = email.getText().toString();
        if (TextUtils.isEmpty(inEmail)) {
            email.setError("Required email");
            valid = false;
        } else {
            email.setError(null);
        }

        String inPassword = password.getText().toString();
        if (TextUtils.isEmpty(inPassword)) {
            password.setError("Required password");
            valid = false;
        } else {
            password.setError(null);
        }
        return valid;
    }


    private void userSign(final String inEmail, final String inPassword){
        dpb.setVisibility(View.VISIBLE);
        Progressbar(40);

        Log.d(TAG,"SignIn:" + email);
        if(!validateForm()){
            return;
        }
        auth.signInWithEmailAndPassword(inEmail,inPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            //Toast.makeText(LoginActivity.this,"SIgn In Successed",Toast.LENGTH_SHORT).show();
                            dpb.setVisibility(View.GONE);
                            updateUI(user);

                        } else {
                            dpb.setVisibility(View.GONE);
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            //Toast.makeText(LoginActivity.this, "Login Gagal.",Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            if(inPassword.length() < 6){
                                Toast.makeText(LoginActivity.this, "Password lower than 6", Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(LoginActivity.this,"Failed Login",Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
    }

    private void Progressbar(final int i) {

        dpb.setProgress(i);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
                Progressbar(i + 20);
            }

        });
        thread.start();
    }


    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null){
            findViewById(R.id.btn_login).setEnabled(!currentUser.isEmailVerified());
            startActivity(new Intent(LoginActivity.this,UserActivity.class));
            finish();
        }
    }


}
