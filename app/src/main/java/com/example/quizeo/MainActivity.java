package com.example.quizeo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void exitClick(View v) {
        System.exit(0);
    }

}