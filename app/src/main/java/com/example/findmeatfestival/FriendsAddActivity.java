package com.example.findmeatfestival;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;


public class FriendsAddActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DrawerLayout drawer;

    private String userId;
    private String documentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_add);

        userId = getIntent().getStringExtra("UserID");
        documentID = getIntent().getStringExtra("DocumentID");

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