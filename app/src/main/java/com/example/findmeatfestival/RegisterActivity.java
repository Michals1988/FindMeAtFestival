package com.example.findmeatfestival;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth=FirebaseAuth.getInstance();

    private static final String TAG = "MyActivity";

    Button buttonRegister;
    Button buttonBacktoLogin;

    private EditText editTextName, editTextlastName, editTexteMail, editTextTel, editTextPassword, editTextrepeatPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextlastName = (EditText) findViewById(R.id.editTextLastName);
        editTexteMail = (EditText) findViewById(R.id.editTextEmailAddress);
        editTextTel = (EditText) findViewById(R.id.editTelNumber);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextrepeatPassword = (EditText) findViewById(R.id.editTextPasswordRepeat);

        buttonRegister = findViewById(R.id.buttonRegister);
        buttonBacktoLogin = findViewById(R.id.buttonbackToLogin);

        buttonBacktoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                    if (editTextPassword.getText().toString().equals(editTextrepeatPassword.getText().toString())) {

                        fAuth.createUserWithEmailAndPassword(editTexteMail.getText().toString().trim(),editTextPassword.getText().toString().trim())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Map<String, Object> user = new HashMap<>();
                                user.put("eMail", editTexteMail.getText().toString());
                                user.put("lastName", editTextlastName.getText().toString());
                                user.put("name", editTextName.getText().toString());
                                user.put("tel", editTextTel.getText().toString());

                                db.collection("users")
                                        .add(user)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                Toast.makeText(getApplicationContext(), "Udało się!", Toast.LENGTH_SHORT).show();

                                                Context context=v.getContext();                        
                                                Intent intent = new Intent(context, LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        });}
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if(e instanceof FirebaseAuthUserCollisionException)
                                {
                                    Toast.makeText(getApplicationContext(), "Użytkownik już istnieje!", Toast.LENGTH_SHORT).show();
                                }
                                Log.w(TAG, "Error adding document", e);
                                Toast.makeText(getApplicationContext(), "Nie udało się!", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                    else
                {
                    Toast.makeText(getApplicationContext(), "Niepoprawne dane!", Toast.LENGTH_SHORT).show();
                }





            }


        });


    }
}