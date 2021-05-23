package com.example.findmeatfestival;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class FestivalOneShowActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    private String documentID,userId;

    String festivalID;
    TextView textViewFestivalName, textViewFestivalDate;

    Button deleteButton, editButton;

    private FirebaseFirestore db=FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festival_one_show);


        documentID = getIntent().getStringExtra("documentUserID");
        userId = getIntent().getStringExtra("userID");
        festivalID = getIntent().getStringExtra("festivalID");


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

        textViewFestivalName=findViewById(R.id.textViewNameOneShow);
        textViewFestivalDate=findViewById(R.id.textViewDateOneShow);

        deleteButton=findViewById(R.id.buttonOneShowDelete);
        editButton=findViewById(R.id.buttonOneShowEdit);

        db.collection("users").document(documentID).collection("festivals").document(festivalID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String pattern = "dd-MM-yyyy";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                        String dateString=simpleDateFormat.format(documentSnapshot.getTimestamp("dateFestival").toDate());

                        textViewFestivalName.setText(documentSnapshot.get("nameFestival").toString());
                        textViewFestivalDate.setText(dateString);
                    }
                });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                db.collection("users").document(documentID).collection("festivals").document(festivalID)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Festiwal usunięty!", Toast.LENGTH_SHORT).show();
                                Context context=v.getContext();
                                Intent intentBackToFestivals=new Intent(context, FestivalActivity.class);
                                intentBackToFestivals.putExtra("UserId",userId);
                                intentBackToFestivals.putExtra("DocumentID",documentID);
                                finish();
                                startActivity(intentBackToFestivals);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Nie udało się usunąć!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context=v.getContext();
                Intent intentEditFestival=new Intent(context, FestivalEditActivity.class);
                intentEditFestival.putExtra("UserID",userId);
                intentEditFestival.putExtra("DocumentID",documentID);
                intentEditFestival.putExtra("FestivalID", festivalID);
                startActivity(intentEditFestival);
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_maingPage:
                Intent intentMainPage= new Intent(this, MainActivity.class);
                intentMainPage.putExtra("UserId",userId);
                intentMainPage.putExtra("DocumentID",documentID);
                startActivity(intentMainPage);
                break;

            case R.id.nav_Festival:
                Intent intentFestival = new Intent(this, FestivalActivity.class);
                intentFestival.putExtra("UserId",userId);
                intentFestival.putExtra("DocumentID",documentID);
                startActivity(intentFestival);
                break;

            case R.id.nav_Friend:
                Intent intentFriend = new Intent(this, FriendsActivity.class);
                intentFriend.putExtra("UserId",userId);
                intentFriend.putExtra("DocumentID",documentID);
                startActivity(intentFriend);
                break;
        }
        return true;
    }
}