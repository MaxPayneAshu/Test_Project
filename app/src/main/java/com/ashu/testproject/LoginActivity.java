package com.ashu.testproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    EditText phoneEditText;

    TextView registerTextView;
    TextView errorTV;
    Button loginButton;



    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneEditText = findViewById(R.id.phoneEditText);
         loginButton = findViewById(R.id.loginButton);
         registerTextView= findViewById(R.id.registerTextView);
        errorTV= findViewById(R.id.errorTV);
        mAuth=FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone = phoneEditText.getText().toString().trim();
                FirebaseFirestore.getInstance().collection(Constants.ROOT_COLLECTION).whereEqualTo(Constants.PHONE_NUMBER,phone).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (!task.getResult().isEmpty()){
                                    //already registered
                                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + phone, 60, TimeUnit.SECONDS, LoginActivity.this,
                                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                                @Override
                                                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                                    errorTV.setText("onVerificationCompleted: "+phoneAuthCredential);
                                                    Log.e("TAG", "onVerificationCompleted: " );
                                                }

                                                @Override
                                                public void onVerificationFailed(FirebaseException e) {
                                                    errorTV.setText("onVerificationFailed: "+e.getLocalizedMessage());
                                                    Log.e("TAG", "onVerificationFailed: "+e.getLocalizedMessage() );
                                                }

                                                @Override
                                                public void onCodeSent(String code, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                                    super.onCodeSent(code, forceResendingToken);
                                                    Log.e("TAG", "onCodeSent: " );
                                                    Intent intent = new Intent(LoginActivity.this,OTPActivity.class);
                                                    intent.putExtra(Constants.OTP_CODE,code);
                                                    intent.putExtra(Constants.PHONE_NUMBER,phone);
                                                    Constants.isFromRegister = false;
                                                    startActivity(intent);
                                                }
                                            });
                                }else {
                                    Log.e("TAG", "onComplete: Fail" );
                                    Toast.makeText(LoginActivity.this, "Phone number is not register", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }
}
