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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FestivalAddActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DrawerLayout drawer;


    String documentID, userId;

    Date date;

    EditText editDateFestival;
    EditText editNameFestival;

    Button buttonAddFestivalToBase, buttonCancel;

    Calendar mCalender = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festival_add);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userId = getIntent().getStringExtra("UserID");
        documentID = getIntent().getStringExtra("DocumentID");

        System.out.println("festival ADD  "+documentID);

        drawer = findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigationDrawerOpen, R.string.navigationDrawerClose);
        drawer.addDrawerListener(toggle);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        editDateFestival = findViewById(R.id.editTextDate);
        editNameFestival = findViewById(R.id.editTextFestivalName);

        buttonAddFestivalToBase = findViewById(R.id.buttonAddFestivalToBase);
        buttonCancel=findViewById(R.id.buttonCancel);

        editDateFestival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int year = mCalender.get(Calendar.YEAR);
                int month = mCalender.get(Calendar.MONTH);
                int day = mCalender.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dp = new DatePickerDialog(v.getContext(), android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        editDateFestival.setText(checkDigit(dayOfMonth) + "-" + checkDigit((monthOfYear + 1)) + "-" + year);
                    }
                }, year, month, day);
                dp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dp.show();


            }
        });



        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFestival = new Intent(v.getContext(), FestivalActivity.class);
                intentFestival.putExtra("UserId",userId);
                intentFestival.putExtra("DocumentID",documentID);
                finish();
                startActivity(intentFestival);
            }
        });

        buttonAddFestivalToBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    date = formatter.parse(editDateFestival.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Map<String, Object> festival = new HashMap<>();
                festival.put("dateFestival", date);
                festival.put("nameFestival", editNameFestival.getText().toString());


                db.collection("users").document(documentID).collection("festivals")
                        .add(festival)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getApplicationContext(), "Festiwal zapisany!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Błąd zapisu...", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_maingPage:
                Intent intentMainPage= new Intent(this, MainActivity.class);
                startActivity(intentMainPage);
                break;

            case R.id.nav_Festival:
                Intent intentFestival = new Intent(this, FestivalActivity.class);
                startActivity(intentFestival);
                break;

            case R.id.nav_Friend:
                Intent intentFriend = new Intent(this, FriendsActivity.class);
                startActivity(intentFriend);
                break;
        }
        return true;
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }
}