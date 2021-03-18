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

    //local variables
    Button buttonSaveQuit;
    Button buttonSaveName;
    Button buttonAddQuestion;

    TextView numberOfQuestions;

    EditText quizName;
    Quiz quiz;
    Question questionToAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_quiz2);
        buttonSaveQuit = (Button) findViewById(R.id.buttonSaveQuit);
        quizName = (EditText) findViewById(R.id.YourQuizName);
        buttonAddQuestion = (Button) findViewById(R.id.buttonAddQuestion);
        numberOfQuestions = (TextView) findViewById(R.id.NumberOfQuestions);

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

        questionToAdd = getIntent().getParcelableExtra("question");
        if (questionToAdd != null) {
            quiz.addQuestion(questionToAdd);
        }


        String textToSet = "Number of questions: " + quiz.getNumberOfQuestions();
        numberOfQuestions.setText(textToSet);

        buttonSaveQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });


        buttonAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddQuestion();
            }
        });
    }
    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openCreateQuizActivity() {
        Intent intent = new Intent(this, CreateQuizActivity.class);
        startActivity(intent);
    }

    public void openAddQuestion() {
        Intent intent = new Intent(this, AddQuestionActivity.class);
        intent.putExtra("quiz", quiz);
        startActivity(intent);
    }

}