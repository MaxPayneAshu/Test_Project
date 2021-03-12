package com.ashu.testproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class OTPActivity extends AppCompatActivity {

    EditText inputCode1;
    EditText inputCode2;
    EditText inputCode3;
    EditText inputCode4;
    EditText inputCode5;
    EditText inputCode6;
    TextView number;
    TextView resentOTP;
    Button verifyOTPButton;
    String otp_verification_id;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);

        otp_verification_id = getIntent().getStringExtra(Constants.OTP_CODE);
        final String phone_number = getIntent().getStringExtra(Constants.PHONE_NUMBER);
        final String first_Name   = getIntent().getStringExtra(Constants.FIRST_NAME);
        final String last_Name   = getIntent().getStringExtra(Constants.LAST_NAME);
        final String email   = getIntent().getStringExtra(Constants.EMAIL);

        inputCode1 = findViewById(R.id.inputCode1);
        inputCode2 = findViewById(R.id.inputCode2);
        inputCode3 = findViewById(R.id.inputCode3);
        inputCode4 = findViewById(R.id.inputCode4);
        inputCode5 = findViewById(R.id.inputCode5);
        inputCode6 = findViewById(R.id.inputCode6);
        verifyOTPButton = findViewById(R.id.verifyOTPButton);
        resentOTP = findViewById(R.id.resentOTP);
        number = findViewById(R.id.number);

        number.setText("+91" + phone_number);

        setupOTPView();
        verifyOTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String otp_code = inputCode1.getText().toString().trim() +
                        inputCode2.getText().toString().trim() +
                        inputCode3.getText().toString().trim() +
                        inputCode4.getText().toString().trim() +
                        inputCode5.getText().toString().trim() +
                        inputCode6.getText().toString().trim();

                if (otp_code == null) {
                    Toast.makeText(OTPActivity.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otp_verification_id, otp_code);
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if (authResult != null) {
                                if (Constants.isFromRegister){
                                    DataModel dataModel = new DataModel();
                                    dataModel.setFirst_name(first_Name);
                                    dataModel.setLast_name(last_Name);
                                    dataModel.setPhone_number(phone_number);
                                    dataModel.setEmail(email);
                                    FirebaseFirestore.getInstance().collection(Constants.ROOT_COLLECTION).document(FirebaseAuth.getInstance().getUid()).set(dataModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Log.e(TAG, "onComplete: Data saved" );
                                                Intent intent=new Intent(OTPActivity.this,MainActivity.class);
                                                startActivity(intent);
                                            }
                                            else
                                            {
                                                Log.e(TAG, "onComplete: "+task.getException() );
                                                Toast.makeText(OTPActivity.this, "Check Details Carefully", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }else {
                                    Intent intent = new Intent(OTPActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: " + e);
                        }
                    });
                }
            }
        });
    }

    private void setupOTPView() {
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode2.requestFocus();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode3.requestFocus();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode4.requestFocus();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode5.requestFocus();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputCode6.requestFocus();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

}
