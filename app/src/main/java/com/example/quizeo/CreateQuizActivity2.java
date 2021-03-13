package com.example.quizeo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CreateQuizActivity2 extends AppCompatActivity {

    //local variables
    Button buttonSaveQuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_quiz2);
        buttonSaveQuit = (Button) findViewById(R.id.buttonSaveQuit);

        buttonSaveQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
    }
        public void openMainActivity() {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
    }
}