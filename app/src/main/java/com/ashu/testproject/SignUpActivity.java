package com.ashu.testproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText phoneEditText;
    EditText emailEditText;
    EditText passEditText;
    EditText confirmpassEditText;
    RadioButton genMale;
    RadioButton genFemale;
    RadioButton genOthers;
    RadioGroup radioGroupLayout;
    Button registerButton;
    TextView loginTextView;
    TextView errorTV;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_sign_up);
         firstNameEditText = findViewById(R.id.firstNameEditText);
         lastNameEditText = findViewById(R.id.lastNameEditText);
         phoneEditText = findViewById(R.id.phoneEditText);
         emailEditText = findViewById(R.id.emailEditText);
         passEditText = findViewById(R.id.passEditText);
         confirmpassEditText = findViewById(R.id.confirmpassEditText);
         genMale = findViewById(R.id.genMale);
         genFemale = findViewById(R.id.genFemale);
         genOthers = findViewById(R.id.genOthers);
         radioGroupLayout = findViewById(R.id.radioGroupLayout);
         registerButton = findViewById(R.id.registerButton);
        loginTextView= findViewById(R.id.loginTextView);
        errorTV= findViewById(R.id.errorTV);

        mAuth=FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone_number = phoneEditText.getText().toString().trim();

                FirebaseFirestore.getInstance().collection(Constants.ROOT_COLLECTION).whereEqualTo(Constants.PHONE_NUMBER,phone_number).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (!task.getResult().isEmpty()){
                                    //already have account
                                    Toast.makeText(SignUpActivity.this, "this number is already registered", Toast.LENGTH_SHORT).show();
                                }else {
                                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + phone_number, 60, TimeUnit.SECONDS, SignUpActivity.this,
                                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                                @Override
                                                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                                    errorTV.setText("onVerificationCompleted: "+phoneAuthCredential);

                                                }

                                                @Override
                                                public void onVerificationFailed(FirebaseException e) {
                                                    errorTV.setText("onVerificationFailed: "+e.getLocalizedMessage());


                                                }

                                                @Override
                                                public void onCodeSent(String code, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                                    super.onCodeSent(code, forceResendingToken);
                                                    Intent intent = new Intent(SignUpActivity.this,OTPActivity.class);
                                                    intent.putExtra(Constants.OTP_CODE,code);
                                                    intent.putExtra(Constants.PHONE_NUMBER,phone_number);
                                                    intent.putExtra(Constants.FIRST_NAME,firstNameEditText.getText().toString().trim());
                                                    intent.putExtra(Constants.LAST_NAME,lastNameEditText.getText().toString().trim());
                                                    intent.putExtra(Constants.EMAIL,emailEditText.getText().toString().trim());
                                                    intent.putExtra(Constants.FIRST_NAME,firstNameEditText.getText().toString().trim());
                                                    Constants.isFromRegister = true;
                                                    startActivity(intent);
                                                }
                                            });
                                }
                            }
                        });
            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
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
