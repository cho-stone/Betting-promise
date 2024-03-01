package com.PACOsoft.promise_betting.view;

        import android.accounts.Account;
        import android.app.Activity;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.util.Log;
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
        import com.PACOsoft.promise_betting.obj.Promise;
        import com.PACOsoft.promise_betting.obj.User;
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
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;
        import java.util.Optional;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextInputEditText ID;
    private TextInputEditText Password;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private SignInButton signInButton;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private TextView tv_google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        signInButton = findViewById(R.id.btn_google_sign_in);
        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
        Intent intent = new Intent(getApplicationContext(), signup_page.class);
        startActivity(intent);
    }


    public void signIn() {
        if (ID.getText().toString().trim().isEmpty() || ID.getText().toString() == null) {
            //아이디 오류
            Toast.makeText(getApplicationContext(), "ID err", Toast.LENGTH_LONG).show();
        } else if (Password.getText().toString().trim().isEmpty() || Password.getText().toString() == null) {
            //비밀번호 오류
            Toast.makeText(getApplicationContext(), "Password err", Toast.LENGTH_LONG).show();
        } else {
            //로그인 성공
            mAuth.signInWithEmailAndPassword(ID.getText().toString().trim(), Password.getText().toString().trim())
                    .addOnCompleteListener(
                            task -> {
                                if (task.isSuccessful()) {
                                    //이메일 인증받은 계정인지 검사
                                    if (mAuth.getCurrentUser().isEmailVerified()) {
                                        updateUI(mAuth.getCurrentUser().getUid());
                                    } else {
                                        Toast.makeText(getApplicationContext(), "이메일 인증 실패", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_LONG).show();
                                }
                            }
                    );
        }
    }

    //구글 로그인 시작
    private void googlesignIn() {
        if (mAuth.getCurrentUser() == null) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            activityResultLauncher.launch(signInIntent);
        }
        if (mAuth.getCurrentUser() != null) {
            database = FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연결
            databaseReference = database.getReference("User").child(mAuth.getCurrentUser().getUid());//DB테이블 연결, 파이어베이스 콘솔에서 User에 접근
            ValueEventListener CheckMeListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User me = snapshot.getValue(User.class);
                    if (me == null) {
                        User user = new User();
                        user.setProfile("https://firebasestorage.googleapis.com/v0/b/fir-listexample-4b146.appspot.com/o/free-icon-font-user-3917688.png?alt=media&token=6d701d27-9620-4b12-b315-46fa39a42210");
                        user.setAccount(0);
                        user.setId(mAuth.getCurrentUser().getEmail());
                        user.setNickName(mAuth.getCurrentUser().getDisplayName());
                        user.setPromiseKey("");
                        user.setFriendsUID("");
                        user.setHistoryKey("");
                        user.setUID(mAuth.getCurrentUser().getUid());
                        databaseReference.setValue(user);
                        updateUI(mAuth.getCurrentUser().getUid());
                    } else {
                        updateUI(mAuth.getCurrentUser().getUid());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };
            databaseReference.addListenerForSingleValueEvent(CheckMeListener);
        }
    }

    private void init() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
                    try {
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
        //자동 로그인
        if (mAuth.getCurrentUser() == null ? false : true) {
            if (mAuth.getCurrentUser().isEmailVerified()) {
                updateUI(mAuth.getCurrentUser().getUid());
            }
        }

    }

    private void updateUI(String UID) {
        Intent intent = new Intent(getApplicationContext(), Home.class);
        intent.putExtra("UID", UID);//UID 전송
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//기존 모든 엑티비티 종료 후 intent
        startActivity(intent);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                } else {
                    Toast.makeText(getApplicationContext(), "Auth Fail", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    //구글 로그인 끝

    //비밀번호 변경
    public void btn_reset_password(View view) {
        Intent intent = new Intent(getApplicationContext(), Reset_Password.class);
        startActivity(intent);
    }
}