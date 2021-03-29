package com.example.quizeo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;
import android.content.Intent;
import android.widget.ImageView;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    // Declare local variables
    Button buttonMakeQuiz;
    Button buttonStartQuiz;
    Button buttonOptions;

    // default values for the options
    boolean sound;
    boolean verified;
    boolean darkmode;
    boolean request;

    private LocationManager locationManager;
    private LocationListener locationListener = new MyLocationListener();
    private boolean gpsEnabled = false;
    private boolean connectionEnabled = false;

    // variables for latitude and longitude
    public double longitude;
    public double latitude;
    LocationQuizeo location;

    User user;

    /** Holds the Authentication instance */
    private Authentication authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);
        Intent intent = getIntent();

        // get options from options activity
        getOptions(intent);
        // find the globes in the background
        ImageView lightGlobe = findViewById(R.id.imageView4);
        ImageView darkGlobe = findViewById(R.id.imageView5);
        if (darkmode) {     // if dark mode is turned on
            // show the dark globe
            lightGlobe.setVisibility(View.INVISIBLE);
            darkGlobe.setVisibility(View.VISIBLE);
            // check whether dark mode is actually on
            if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
                // turn dark mode on if it was not on yet
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        } else {            // if dark mode is turned off
            // show the light globe
            darkGlobe.setVisibility(View.INVISIBLE);
            lightGlobe.setVisibility(View.VISIBLE);
            // check whether dark mode is actually off
            if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO) {
                // turn dark mode off if it was not off yet
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }

        // Define variables with corresponding button
        buttonMakeQuiz = (Button) findViewById(R.id.buttonMakeQuiz);
        buttonStartQuiz = (Button) findViewById(R.id.buttonStartQuiz);
        buttonOptions = (Button) findViewById(R.id.buttonOptions);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Open the CreateQuizActivity with make quiz button
        buttonMakeQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
                location = new LocationQuizeo(latitude, longitude);
                openCreateQuizActivity();
            }
        });

        // Open the PlayQuizActivity with start quiz button
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
                location = new LocationQuizeo(latitude, longitude);
                openPlayQuizActivity();
            }
        });

        // Open the OptionsActivity with the options button
        buttonOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOptionsActivity();
            }
        });


        // check if needed permissions for location are granted
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        authentication = Authentication.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        authentication.loginAnonymously(MainActivity.this);
        user = getIntent().getParcelableExtra("user");
        if (user == null) {
            loadData();
            if (user.getNickName().equals("")) {
                pickUsername();
            }
        }
    }

    private void saveData() {
        SharedPreferences sp =
                getSharedPreferences("MyPrefs",
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("nickname", user.getNickName());
        editor.putString("id", user.getUserId());
        editor.commit();
    }

    private void loadData() {
        SharedPreferences sp =
                getSharedPreferences("MyPrefs",
                        Context.MODE_PRIVATE);
//        sp.edit().remove("nickname").commit();
//        sp.edit().remove("id").commit();
        String nickname = sp.getString("nickname", "");
        String id = sp.getString("id", "");
        System.out.println("UUID "+ id);
        user = new User(nickname, id);
    }

    public void pickUsername() {
        Intent intent = new Intent(this, PickUsername.class);
        intent.putExtra("location", location);
        startActivity(intent);
    }

    @Override
    // Back button on device acts the same as the exit icon
    public void onBackPressed() {
        exitClick(findViewById(R.id.exitIconHome));
    }

    // Get the options sent by the options activity
    public void getOptions(Intent i) {
        sound = i.getBooleanExtra("sound", true);
        verified = i.getBooleanExtra("verified", true);
        darkmode = i.getBooleanExtra("darkmode", false);
        request = i.getBooleanExtra("request", false);
    }

    // Method to open the CreateQuizActivity
    public void openCreateQuizActivity(){
        Intent intent = new Intent(this, CreateQuizActivity.class);
        intent.putExtra("location", location);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    //Method to open the PlayQuizActivity
    public void openPlayQuizActivity(){
        boolean verified = getIntent().getBooleanExtra("verified", true);
        Intent intent = new Intent(this, PlayQuizActivity.class);
        intent.putExtra("verified", verified);
        intent.putExtra("location", location);
        startActivity(intent);
    }

    // Opens the options activity
    public void openOptionsActivity(){
        Intent intent = new Intent(this, OptionsActivity.class);
        // put the values for options in the intent
        intent.putExtra("sound", sound);
        intent.putExtra("verified", verified);
        intent.putExtra("darkmode", darkmode);
        intent.putExtra("request", request);
        startActivity(intent);
    }

    public void test(View v) {
        int resID=getResources().getIdentifier("test", "raw", getPackageName());
        MediaPlayer mp =MediaPlayer.create(this, resID);
        mp.start();
    }

    // Asks the user if they want to quit the application
    public void exitClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Do you want to quit?")
                .setPositiveButton("Quit", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener).show();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:       // quit button clicked
                    // close the application
                    finishAffinity();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:       // cancel button clicked
                    // return to home activity
                    break;
            }
        }
    };

    // class where the location is found and assigned
    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (location != null) {
                locationManager.removeUpdates(locationListener);
                latitude =  location.getLatitude();
                longitude = location.getLongitude();
            }
        }

    }

    // method to fetch the location
    public void getLocation() {

        //check if the gps is present and turned on
        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {

        }

        //check if internet connection is present and turned on
        try {
            connectionEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {

        }

        // if they aren't, print a message
        if (!gpsEnabled && !connectionEnabled) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Error");
            builder.setMessage("location services are disabled");

            builder.create().show();


        }

        // fetch the location if everything is fine
        if (gpsEnabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                    0, locationListener);
        }

        if (connectionEnabled) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                    0, locationListener);

        }

    }

}