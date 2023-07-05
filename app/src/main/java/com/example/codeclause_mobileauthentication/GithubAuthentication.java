package com.example.codeclause_mobileauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthProvider;

import java.util.ArrayList;
import java.util.List;

public class GithubAuthentication extends AppCompatActivity {

    EditText inputemail;
    Button btnlogin;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github_authentication);

        inputemail = findViewById(R.id.emailedittextgithub);
        btnlogin = findViewById(R.id.githubloginbutton);

        mAuth = FirebaseAuth.getInstance();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputemail.getText().toString();

                if(email.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Enter the Email",Toast.LENGTH_SHORT).show();
                }
                else{
                    OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");
                    // Target specific email with login hint.
                    provider.addCustomParameter("login", email);

                    // Request read access to a user's email addresses.
// This must be preconfigured in the app's API permissions.
                    List<String> scopes =
                            new ArrayList<String>() {
                                {
                                    add("user:email");
                                }
                            };
                    provider.setScopes(scopes);

                    Task<AuthResult> pendingResultTask = mAuth.getPendingAuthResult();
                    if (pendingResultTask != null) {
                        // There's something already here! Finish the sign-in for your user.
                        pendingResultTask
                                .addOnSuccessListener(
                                        new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                                // User is signed in.
                                                // IdP data available in
                                                // authResult.getAdditionalUserInfo().getProfile().
                                                // The OAuth access token can also be retrieved:
                                                // ((OAuthCredential)authResult.getCredential()).getAccessToken().
                                                // The OAuth secret can be retrieved by calling:
                                                // ((OAuthCredential)authResult.getCredential()).getSecret().
                                            }
                                        })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), ""+e.getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                        });
                    } else {

                        mAuth
                                .startActivityForSignInWithProvider(GithubAuthentication.this, provider.build())
                                .addOnSuccessListener(
                                        new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                                openNextActivity();

                                            }
                                        })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                               Toast.makeText(getApplicationContext(), ""+e.getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                        });



                    }



                }

            }
        });
    }

    private void openNextActivity(){
        Intent intent = new Intent(getApplicationContext(), mainpageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}