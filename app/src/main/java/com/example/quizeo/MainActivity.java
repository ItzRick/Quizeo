package com.example.quizeo;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    //local variables
    Button buttonMakeQuiz;
    Button buttonStartQuiz;
    Button buttonOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);
        buttonMakeQuiz = (Button) findViewById(R.id.buttonMakeQuiz);
        buttonStartQuiz = (Button) findViewById(R.id.buttonStartQuiz);
        buttonOptions = (Button) findViewById(R.id.buttonOptions);

        buttonMakeQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateQuizActivity();
            }
        });

        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlayQuizActivity();
            }
        });

        buttonOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOptionsActivity();
            }
        });
    }

    public void openCreateQuizActivity(){
        Intent intent = new Intent(this, CreateQuizActivity.class);
        startActivity(intent);
    }

    public void openPlayQuizActivity(){
        Intent intent = new Intent(this, PlayQuizActivity.class);
        startActivity(intent);
    }

    public void openOptionsActivity(){
        Intent intent = new Intent(this, OptionsActivity.class);
        intent.putExtra("Sign", true);
        startActivity(intent);
    }

    public void test(View v) {
        int resID=getResources().getIdentifier("test", "raw", getPackageName());
        MediaPlayer mp =MediaPlayer.create(this, resID);
        mp.start();
    }

    public void exitClick(View v) {
        System.exit(0);
    }

}