package com.example.quizeo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.UUID;

public class PickUsername extends AppCompatActivity {

    Button pickUsername;
    EditText userName;
    LocationQuizeo location;
    User user;

    int PERMISSION_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PERMISSION_ID = getIntent().getIntExtra("permission_id", 44);
        requestPermissions();
        // Set the correct layout:
        setContentView(R.layout.fragment_pick_username);

        location = getIntent().getParcelableExtra("location");


        userName = (EditText) findViewById(R.id.Your_User_Name);
        pickUsername = (Button) findViewById(R.id.button_save_username);

        pickUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = new User(userName.getText().toString(), UUID.randomUUID().toString());
                saveData();
                toMain();
            }
        });
    }

    // Back button closes the app
    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void toMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("location", location);
        startActivity(intent);
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
}
