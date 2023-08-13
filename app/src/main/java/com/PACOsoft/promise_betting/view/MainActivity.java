package com.PACOsoft.promise_betting.view;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            auth.signInWithEmailAndPassword(ID.getText().toString().trim(), Password.getText().toString().trim())
                    .addOnCompleteListener(
                            task -> {
                                if (task.isSuccessful()) {
                                    //이메일 인증받은 계정인지 검사
                                    if(auth.getCurrentUser().isEmailVerified()) {
                                        updateUI(auth.getCurrentUser().getUid());
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "verify Fail", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Auth Fail", Toast.LENGTH_LONG).show();
                                }
                            }
                    );
        }
    }

    //구글 로그인 시작
    private void googlesignIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activityResultLauncher.launch(signInIntent);
    }
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
        //자동 로그인
//        if(mAuth.getCurrentUser()==null ? false : true)
//            updateUI(mAuth.getCurrentUser().getUid());
    }

    private void updateUI(String UID) {
        Intent intent = new Intent(getApplicationContext(), Home.class);
        intent.putExtra("myId", ID.getText().toString().trim());//UID 전송
        intent.putExtra("UID", UID);//UID 전송
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//기존 모든 엑티비티 종료 후 intent
        startActivity(intent);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    database = FirebaseDatabase.getInstance();//파이어베이스 데이터베이스 연결
                    databaseReference = database.getReference("User");//DB테이블 연결, 파이어베이스 콘솔에서 User에 접근
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                            ArrayList<User> users = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                users.add(snapshot.getValue(User.class));
                            }

                            if (users.stream().parallel().anyMatch(u -> u.getId().equals(mAuth.getCurrentUser().getUid()))) {
                                //myId와 동일한 id가 DB에 있는지 확인 이미 존재하며 아무 것도 안 함
                            }
                            else{
                                //없다면 DB에 유저 정보 추가
                                User user = new User();
                                user.setProfile("https://firebasestorage.googleapis.com/v0/b/fir-listexample-4b146.appspot.com/o/free-icon-font-user-3917688.png?alt=media&token=6d701d27-9620-4b12-b315-46fa39a42210");
                                user.setAccount(0);
                                user.setId(mAuth.getCurrentUser().getEmail());
                                user.setNickName(mAuth.getCurrentUser().getDisplayName());
                                user.setPromiseKey("");
                                user.setFriendsId("");
                                user.setUID(auth.getCurrentUser().getUid());
                                databaseReference.child(auth.getCurrentUser().getUid()).setValue(user);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            //DB를 가져오는 중에 에러 발생 시 어떤걸 띄울 것인가
                            Log.e("MainActivity", String.valueOf(databaseError.toException()));//에러문 출력
                        }
                    });
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user.getUid());
                }
                else{
                    Toast.makeText(getApplicationContext(), "Auth Fail", Toast.LENGTH_LONG).show();

                }
            }
        });
    }


    //구글 로그인 끝
}