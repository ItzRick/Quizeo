package com.example.quizeo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

public class CreateQuizActivity2 extends AppCompatActivity {

    //local variables
    Button buttonSaveQuit;
    Button buttonSaveName;
    Button buttonAddQuestion;

    EditText quizName;
    Quiz quiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_quiz_2);
        buttonSaveQuit = (Button) findViewById(R.id.buttonSaveQuit);
        buttonSaveName = (Button) findViewById(R.id.buttonSelectName);
        quizName = (EditText) findViewById(R.id.YourQuizName);
        buttonAddQuestion = (Button) findViewById(R.id.buttonAddQuestion);

        quiz = new Quiz();
        quiz.setQuizId(UUID.randomUUID());

        buttonSaveQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateQuizActivity();
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

        buttonAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddQuestion();
            }
        });
    }

    public void openCreateQuizActivity() {
        Intent intent = new Intent(this, CreateQuizActivity.class);
        startActivity(intent);
    }

    public void openAddQuestion() {
        Intent intent = new Intent(this, AddQuestionActivity.class);
        startActivity(intent);
    }
}