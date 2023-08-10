package com.PACOsoft.promise_betting.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.PACOsoft.promise_betting.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Test_Signin3 extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextInputEditText ID;
    private TextInputEditText Password;




    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private SignInButton signInButton;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);

        auth = FirebaseAuth.getInstance();

        init();

        signInButton = findViewById(R.id.btn_google_sign_in);
        signInButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                googlesignIn();
            }
        });

    }


    public void login(View view) {
        ID = findViewById(R.id.inputLoginId);
        Password = findViewById(R.id.inputLoginPassword);
        signIn();
    }

    public void reg(View view) {
        Intent intent = new Intent(this, Test_Signin4.class);
        startActivity(intent);
    }


    public void signIn() {
        if (ID.getText().toString().trim().isEmpty() || ID.getText().toString() == null) {
            Toast.makeText(getApplicationContext(), "ID err", Toast.LENGTH_LONG).show();
        } else if (Password.getText().toString().trim().isEmpty() || Password.getText().toString() == null) {
            Toast.makeText(getApplicationContext(), "Password err", Toast.LENGTH_LONG).show();
        } else {
            auth.signInWithEmailAndPassword(ID.getText().toString().trim(), Password.getText().toString().trim())
                    .addOnCompleteListener(
                            task -> {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(getApplicationContext(), Test_Signin2.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Auth Fail", Toast.LENGTH_LONG).show();
                                }
                            }

                    );
        }
    }











//구글 로그인 시작



    private void init(){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent intent = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
                    try{
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        firebaseAuthWithGoogle(account);
                    } catch (ApiException e) {
                        Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        configSignIn();
        mAuth = FirebaseAuth.getInstance();
    }

    private void configSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    @Override
    public void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()==null ? false : true)
            updateUI();
    }

    private void updateUI() {

    }
    private void googlesignIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activityResultLauncher.launch(signInIntent);
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    //updateUI(user);
                    //Toast.makeText(getApplicationContext(), "Complete", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), Test_Signin2.class);
                    startActivity(intent);
                }
                else{
                    //Toast.makeText(getApplicationContext(), "Auth Fail", Toast.LENGTH_LONG).show();
                    //updateUI(null);
                }
            }
        });
    }


    //구글 로그인 끝
}





