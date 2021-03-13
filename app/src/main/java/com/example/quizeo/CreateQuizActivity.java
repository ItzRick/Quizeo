package com.example.quizeo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CreateQuizActivity extends AppCompatActivity {

    //local variables
    Button buttonBack;
    Button buttonNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_quiz);
        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonNew = (Button) findViewById(R.id.buttonNew);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

        buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateQuizActivity2();
            }
        });
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openCreateQuizActivity2() {
        Intent intent = new Intent(this, CreateQuizActivity2.class);
        startActivity(intent);
    }
}
