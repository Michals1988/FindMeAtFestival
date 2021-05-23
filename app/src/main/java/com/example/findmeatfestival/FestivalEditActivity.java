package com.example.findmeatfestival;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FestivalEditActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    private DrawerLayout drawer;

    private String userId;
    private String documentID;
    private String festivalID;
    private Date date;

    Calendar mCalender = Calendar.getInstance();

    EditText editTextEditNameFestival, editTextEditDateFestival;
    Button buttonEditAccept, buttonEditCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festival_edit);


        userId = getIntent().getStringExtra("UserID");
        documentID = getIntent().getStringExtra("DocumentID");
        festivalID = getIntent().getStringExtra("FestivalID");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigationDrawerOpen, R.string.navigationDrawerClose);
        drawer.addDrawerListener(toggle);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        editTextEditNameFestival = findViewById(R.id.editTextEditFestivalName);
        editTextEditDateFestival = findViewById(R.id.editTextEditFestivalDate);

        editTextEditDateFestival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int year = mCalender.get(Calendar.YEAR);
                int month = mCalender.get(Calendar.MONTH);
                int day = mCalender.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dp = new DatePickerDialog(v.getContext(), android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        editTextEditDateFestival.setText(checkDigit(dayOfMonth) + "-" + checkDigit((monthOfYear + 1)) + "-" + year);
                    }
                }, year, month, day);
                dp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dp.show();


            }
        });

        db.collection("users").document(documentID).collection("festivals").document(festivalID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        editTextEditNameFestival.setText(documentSnapshot.get("nameFestival").toString());


                        String pattern = "dd-MM-yyyy";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                        String dateString = simpleDateFormat.format(documentSnapshot.getTimestamp("dateFestival").toDate());

                        editTextEditDateFestival.setText(dateString);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        buttonEditAccept = findViewById(R.id.buttonEditAccept);
        buttonEditCancel = findViewById(R.id.buttonEditCancel);


        buttonEditAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    date = formatter.parse(editTextEditDateFestival.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Map<String, Object> festivalUpdate = new HashMap<>();
                festivalUpdate.put("nameFestival", editTextEditNameFestival.getText().toString());
                festivalUpdate.put("dateFestival", date);


                db.collection("users").document(documentID).collection("festivals").document(festivalID)
                        .update(festivalUpdate)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(v.getContext(), "Dane zaktualizowano", Toast.LENGTH_LONG).show();
                                Intent intentFestival = new Intent(v.getContext(), FestivalActivity.class);
                                intentFestival.putExtra("UserId", userId);
                                intentFestival.putExtra("DocumentID", documentID);
                                finish();
                                startActivity(intentFestival);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(v.getContext(), "Błąd zapisu.", Toast.LENGTH_LONG).show();
                        Intent intentFestival = new Intent(v.getContext(), FestivalActivity.class);
                        intentFestival.putExtra("UserId", userId);
                        intentFestival.putExtra("DocumentID", documentID);
                        finish();
                        startActivity(intentFestival);
                    }
                });
            }
        });

        buttonEditCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentFestival = new Intent(v.getContext(), FestivalActivity.class);
                intentFestival.putExtra("UserId", userId);
                intentFestival.putExtra("DocumentID", documentID);
                finish();
                startActivity(intentFestival);

            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_maingPage:
                Intent intentMainPage = new Intent(this, MainActivity.class);
                intentMainPage.putExtra("UserId", userId);
                intentMainPage.putExtra("DocumentID", documentID);
                finish();
                startActivity(intentMainPage);
                break;

            case R.id.nav_Festival:
                Intent intentFestival = new Intent(this, FestivalActivity.class);
                intentFestival.putExtra("UserId", userId);
                intentFestival.putExtra("DocumentID", documentID);
                finish();
                startActivity(intentFestival);
                break;

            case R.id.nav_Friend:
                Intent intentFriend = new Intent(this, FriendsActivity.class);
                intentFriend.putExtra("UserId", userId);
                intentFriend.putExtra("DocumentID", documentID);
                finish();
                startActivity(intentFriend);
                break;
        }
        return true;
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }
}