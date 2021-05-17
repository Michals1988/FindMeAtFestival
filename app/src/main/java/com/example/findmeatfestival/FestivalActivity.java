package com.example.findmeatfestival;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;

public class FestivalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private DrawerLayout drawer;
    private String userId;
    private String documentID;
    private Button addFestivalButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festival);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userId = getIntent().getStringExtra("UserID");
        documentID = getIntent().getStringExtra("DocumentID");

        System.out.println(documentID);

        drawer = findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigationDrawerOpen, R.string.navigationDrawerClose);
        drawer.addDrawerListener(toggle);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        addFestivalButton=findViewById(R.id.buttonAddFestival);

        addFestivalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddFestival = new Intent(v.getContext(), FestivalAddActivity.class);
                intentAddFestival.putExtra("UserId",userId);
                intentAddFestival.putExtra("DocumentID",documentID);
                finish();
                startActivity(intentAddFestival);
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
                finish();
                startActivity(intentMainPage);
                break;

            case R.id.nav_Festival:
                Intent intentFestival = new Intent(this, FestivalActivity.class);
                intentFestival.putExtra("UserId",userId);
                intentFestival.putExtra("DocumentID",documentID);
                finish();
                startActivity(intentFestival);
                break;

            case R.id.nav_Friend:
                Intent intentFriend = new Intent(this, FriendsActivity.class);
                intentFriend.putExtra("UserId",userId);
                intentFriend.putExtra("DocumentID",documentID);
                finish();
                startActivity(intentFriend);
                break;
        }
        return true;
    }
}