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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText email,password;
    private Button registerbtn;
    private TextView loginview;
    private FirebaseAuth auth;
    private static final String TAG ="EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

    email = (EditText) findViewById(R.id.emailreg);
    password = (EditText) findViewById(R.id.passwordreg);
    registerbtn = (Button) findViewById(R.id.btn_register);
    loginview = (TextView) findViewById(R.id.logintext);

    // Authentication firebase

    auth = FirebaseAuth.getInstance();

    loginview.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    });


    registerbtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            createAccount(email.getText().toString(),password.getText().toString());
        }

        private void createAccount(String inEmail, String inPassword) {

            Log.d(TAG,"SignIn:" + email);
            if(Validation() != true)
            {
                return;
            }

            auth.createUserWithEmailAndPassword(inEmail,inPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        Toast.makeText(RegisterActivity.this,"Email anda telah diregistrasikan, Silahkan login kembali",Toast.LENGTH_SHORT).show();
                        FirebaseUser user = auth.getCurrentUser();
                        //updateUI(user);
                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(RegisterActivity.this, "Email anda gagal didaftarkan",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }

                }
            });







        }
    });

    }

    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser !=null){
            findViewById(R.id.btn_login).setEnabled(!currentUser.isEmailVerified());
        }

    }

    public boolean Validation(){

        boolean valid = true;
        String inEmail = email.getText().toString();

        if(TextUtils.isEmpty(inEmail)){
            email.setError("Silahkan Mengisi Email anda");
        } else
            email.setError(null);

        String inPassword = password.getText().toString();

        if(TextUtils.isEmpty(inPassword)){
            password.setError("Silahkan mengisi password anda");
        } else
            password.setError(null);

        return valid;
    }


}
