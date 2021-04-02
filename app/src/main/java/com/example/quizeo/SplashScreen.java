package com.example.quizeo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Music.instantiate();
        if (!Music.isPlaying()) {
            Music.random(this);
            Music m = new Music();
            m.start(this);
        }

        startActivity(new Intent(SplashScreen.this, MainActivity.class));
        finish();
    }
}
