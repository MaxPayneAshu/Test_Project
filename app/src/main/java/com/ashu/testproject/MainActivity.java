package com.ashu.testproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText emailEditText;
    Button editButton;
    Button logoutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        editButton = findViewById(R.id.editButton);
        logoutButton = findViewById(R.id.logoutButton);

        if (mAuth.getUid() != null) {
            FirebaseFirestore.getInstance().collection(Constants.ROOT_COLLECTION).document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    DataModel dataModel = documentSnapshot.toObject(DataModel.class);
                    if (dataModel != null) {
                        firstNameEditText.setText(dataModel.getFirst_name());
                        lastNameEditText.setText(dataModel.getLast_name());
                        emailEditText.setText(dataModel.getEmail());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("TAG", "onFailure: " + e.getLocalizedMessage());
                }
            });
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first_Name =firstNameEditText.getText().toString().trim();
                String last_Name=lastNameEditText.getText().toString().trim();
                String email=emailEditText.getText().toString().trim();
                HashMap<String , Object > map = new HashMap<>();
                map.put(Constants.FIRST_NAME,first_Name);
                map.put(Constants.LAST_NAME,last_Name);
                map.put(Constants.EMAIL,email);
                FirebaseFirestore.getInstance().collection(Constants.ROOT_COLLECTION).document(mAuth.getUid()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "onFailure: "+e.getLocalizedMessage() );
                        Toast.makeText(MainActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
