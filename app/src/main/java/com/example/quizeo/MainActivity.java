package com.example.quizeo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;


import java.time.LocalDate;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.protobuf.StringValue;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
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

    boolean active = false;

    //for fetching the location
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;

    // variables for latitude and longitude
    public double longitude;
    public double latitude;
    LocationQuizeo location;

    User user;

    /** Holds the Authentication instance */
    private Authentication authentication;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    @SuppressLint("MissingPermission")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);
        Intent intent = getIntent();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //fetch location on create
        getLastLocation();

        // get options from options activity
        getOptions(intent);
        // find the globes in the background
        ImageView lightGlobe = findViewById(R.id.globeHome);
        ImageView darkGlobe = findViewById(R.id.globeHomeDark);
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


        // Open the CreateQuizActivity with make quiz button
        buttonMakeQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (location == null) {
//                    new AddQuestionActivity().showPopup(v, R.layout.popup_no_location_found);
//                } else {
                    getLastLocation();
//                    Log.d("latitude", String.valueOf(latitude));
//                    Log.d("longitude", String.valueOf(longitude));
                    openCreateQuizActivity();
//                }

            }
        });

        // Open the PlayQuizActivity with start quiz button
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location == null) {
                    new AddQuestionActivity().showPopup(v, R.layout.popup_no_location_found);
                } else {
                    getLastLocation();
                    Log.d("latitude", String.valueOf(latitude));
                    Log.d("longitude", String.valueOf(longitude));
                    openPlayQuizActivity();
                }
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
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        authentication = Authentication.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getLastLocation();
        verified = getIntent().getBooleanExtra("verified", true);
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
//        System.out.println("UUID "+ id);
        user = new User(nickname, id);
    }

    public void pickUsername() {
        Intent intent = new Intent(this, PickUsername.class);
        intent.putExtra("permission_id", PERMISSION_ID);
        intent.putExtra("location", location);
        intent.putExtra("darkmode", darkmode);
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
        active = true;
        Intent intent = new Intent(this, CreateQuizActivity.class);
        intent.putExtra("verified", verified);
        intent.putExtra("darkmode", darkmode);
        intent.putExtra("location", location);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    //Method to open the PlayQuizActivity
    public void openPlayQuizActivity(){
        active = true;
        Intent intent = new Intent(this, PlayQuizActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("verified", verified);
        intent.putExtra("darkmode", darkmode);
        intent.putExtra("location", location);
        startActivity(intent);
    }

    // Opens the options activity
    public void openOptionsActivity(){
        active = true;
        Intent intent = new Intent(this, OptionsActivity.class);
        // put the values for options in the intent
        intent.putExtra("sound", sound);
        intent.putExtra("verified", verified);
        intent.putExtra("darkmode", darkmode);
        intent.putExtra("request", request);
        startActivity(intent);
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
                    // Stop music
                    Music.stop(MainActivity.this);
                    // close the application
                    finishAffinity();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:       // cancel button clicked
                    // return to home activity
                    break;
            }
        }
    };

    /**
     * Get the last know location of the device.
     */
    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {

            if (isLocationEnabled()) {
//                System.out.println("HENK");
                    Task<Location> task = mFusedLocationClient.getLastLocation();

                    task.addOnCompleteListener(
                            task1 -> {
                                Location location = task1.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    this.location = new LocationQuizeo(latitude, longitude);

                                }
                            }

                    );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    /**
     * Method for fetching location data for the case when the location value is null
     */
    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude  = mLastLocation.getLatitude();
            longitude =  mLastLocation.getLongitude();
            location = new LocationQuizeo(latitude, longitude);
        }
    };

    /**
     * Method for checking if the user has granted access to to the location services at runtime.
     * @return true if he has granted access, false otherwise
     */
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Method for the case when the user has not granted location access, make him allow to use this access
     */
    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    /**
     * Method for the case when the location is turned off from the device itslef
     * @return true if it is turned on, false otherwise
     */
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    /**
     *  Method for detecting if the needed permissions are given, if they are, start detecting location immediately
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onPause() {
        // Stop the music
        if (!active) {
            Music.stop(this);
        }
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        // Resume music
        if (!Music.isPlaying() && sound) {
            Music.play(this);
        }

        if (checkPermissions()) {
            getLastLocation();
        }
    }

}