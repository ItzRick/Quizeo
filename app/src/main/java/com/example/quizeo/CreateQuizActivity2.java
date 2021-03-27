package com.example.quizeo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

public class CreateQuizActivity2 extends AppCompatActivity {

    // Declare local variables
    Button buttonSaveQuit;
    Button buttonAddQuestion;

    TextView numberOfQuestions;

    EditText quizName;
    EditText yourUserName;

    Quiz quiz;
    LocationQuizeo location;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_quiz_2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.fragment_create_quiz_2);
        buttonSaveQuit = (Button) findViewById(R.id.buttonSaveQuit);
        quizName = (EditText) findViewById(R.id.YourQuizName);
        yourUserName = (EditText) findViewById(R.id.YourUsername);
        buttonAddQuestion = (Button) findViewById(R.id.buttonAddQuestion);
        numberOfQuestions = (TextView) findViewById(R.id.NumberOfQuestions);

        user = getIntent().getParcelableExtra("user");

        location = getIntent().getParcelableExtra("location");

        // Retrieve passed quiz element if it exists:
        quiz = getIntent().getParcelableExtra("quiz");
        // Set new if it does not exist:
        if (quiz == null) {
            System.out.println("test");
            quiz = new Quiz();
            quiz.setQuizId(UUID.randomUUID());
        } else {
            quizName.setText(quiz.getQuizName());
        }


        String textToSet = "Number of questions: " + quiz.getNumberOfQuestions();
        numberOfQuestions.setText(textToSet);

        // Open the home screen with the save & quit button
        buttonSaveQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

        // Open the AddQuestionActivity with add question button
        buttonAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddQuestion();
            }
        });
    }

    // Method to open the home screen
    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Method to open the AddQuestionActivity
    public void openAddQuestion() {
        Intent intent = new Intent(this, AddQuestionActivity.class);
        intent.putExtra("quiz", quiz);
        intent.putExtra("user", user);
        intent.putExtra("location", location);
        startActivity(intent);
    }
}