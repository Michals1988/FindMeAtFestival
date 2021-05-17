package com.example.findmeatfestival;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    FusedLocationProviderClient fusedLocationProviderClient;

    FirebaseAuth fAuth = FirebaseAuth.getInstance();

    String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;
    public String latitude = "";
    public String longitude = "";

    FirebaseUser currentUser = fAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String userId;
    private String documentID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (currentUser != null) {
            userId = getIntent().getStringExtra("UserID");
            documentID = getIntent().getStringExtra("DocumentID");
            currentUser.reload();
        } else {
            Intent intentLoginPage = new Intent(this, LoginActivity.class);
            finish();
            startActivity(intentLoginPage);
        }


        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigationDrawerOpen, R.string.navigationDrawerClose);
        drawer.addDrawerListener(toggle);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle.syncState();

        Button buttonFestival = findViewById(R.id.buttonFestivals);
        Button buttonFriends = findViewById(R.id.buttonFriends);


        FloatingActionButton FABemergencyCall = findViewById(R.id.FABemergencyCall);
        FloatingActionButton FABshareLocation = findViewById(R.id.FABshareLocation);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        buttonFestival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intentFestival = new Intent(context, FestivalActivity.class);
                startActivity(intentFestival);
            }
        });

        buttonFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intentFriends = new Intent(context, FriendsActivity.class);
                startActivity(intentFriends);
            }
        });

        FABemergencyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emergencyNo = "112";
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(Uri.parse("tel:" + emergencyNo));

                startActivity(intentCall);
            }
        });

        FABshareLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    getLocation();

                    Toast.makeText(MainActivity.this, "Latitude: "+latitude +"\nLongitude: "+longitude,Toast.LENGTH_LONG).show();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_maingPage:
                Intent intentMainPage = new Intent(this, MainActivity.class);
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

            case R.id.nav_logOut:
                fAuth.signOut();

                Intent intentLoginPage = new Intent(this, LoginActivity.class);
                //finish();
                startActivity(intentLoginPage);
        }
        return true;
    }

    @SuppressLint("MissingPermission")

    private void getLocation()
    {

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();

                if (location != null) {

                    try {

                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        latitude = String.valueOf(addresses.get(0).getLatitude());
                        longitude = String.valueOf(addresses.get(0).getLongitude());


                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

            }
        });
    }
}