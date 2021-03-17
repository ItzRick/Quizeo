package com.example.quizeo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class CreateQuizActivity2 extends AppCompatActivity {

    //local variables
    Button buttonSaveQuit;
    Button buttonSaveName;

    EditText quizName;
    Quiz quiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_quiz2);
        buttonSaveQuit = (Button) findViewById(R.id.buttonSaveQuit);
        buttonSaveName = (Button) findViewById(R.id.buttonSelectName);
        quizName = (EditText) findViewById(R.id.YourQuizName);
        quiz = new Quiz();

        buttonSaveQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

        buttonSaveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quiz.setQuizName(quizName.getText().toString());

                // Only for testing purposes: 
                System.out.println(quiz.getQuizName());
            }
        });
    }
        public void openMainActivity() {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
    }
}