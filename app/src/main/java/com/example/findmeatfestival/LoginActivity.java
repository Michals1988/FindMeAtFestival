package com.example.findmeatfestival;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dbUser = db.collection("users");

    private FirebaseAuth fAuth=FirebaseAuth.getInstance();

    private EditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = fAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();}

        setContentView(R.layout.activity_login);

        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonGoToRegister = findViewById(R.id.buttonGoToRegister);

        editTextEmail = findViewById(R.id.editTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextPasswordLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View v1=v;

                String eMail=editTextEmail.getText().toString().trim();
                String password=editTextPassword.getText().toString().trim();

                if (eMail.equals("")||password.equals("")){
                    Toast.makeText(LoginActivity.this, "Wszystkie pola muszą być uzupełnione",Toast.LENGTH_LONG).show();
                    return;
                }

                fAuth.signInWithEmailAndPassword(eMail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Zalogowałes się",Toast.LENGTH_LONG).show();

                            final CollectionReference userReference=db.collection("users");
                            String userId=fAuth.getUid();
                            final String[] documentID = new String[1];

                            db.collection("users").whereEqualTo("eMail", editTextEmail.getText().toString())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                System.out.println(document.getId());
                                                documentID[0] =document.getId();
                                                db.collection("users").document(document.getId()).update("documentId",document.getId());
                                                db.collection("users").document(document.getId()).update("userId",fAuth.getUid());
                                            }
                                        }
                                    });

                            Context context=v1.getContext();

                            Intent intentMainPage=new Intent(context, MainActivity.class);
                            intentMainPage.putExtra("UserID",userId);
                            intentMainPage.putExtra("DocumentID",documentID[0]);
                            finish();
                            startActivity(intentMainPage);

                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Błędny mail lub haslo!",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                });

            }
        });


        buttonGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);

            }
        });
    }
}



