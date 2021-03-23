package com.example.quizeo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AddQuestionActivity3 extends AppCompatActivity {

    // Declare local variables
    Button buttonRemoveOption;
    Button buttonAddOption;
    Button buttonSaveQuit;
    Button buttonDeleteQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_question_3);

        // Define variables with corresponding button
        buttonRemoveOption = (Button) findViewById(R.id.buttonRemoveOption);
        buttonAddOption = (Button) findViewById(R.id.buttonAddOption);
        buttonSaveQuit = (Button) findViewById(R.id.buttonSaveQuit);
        buttonDeleteQuestion = (Button) findViewById(R.id.buttonDeleteQuestion);

        // Open previous activity with remove option button
        buttonRemoveOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddQuestionActivity2();
            }
        });

        // Open next activity with add option button
        buttonAddOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddQuestionActivity4();
            }
        });

        // Open the CreateQuizActivity2 with the save & quit button
        buttonSaveQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateQuizActivity2();
            }
        });

        // Open the CreateQuizActivity2 with the delete button
        buttonDeleteQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateQuizActivity2();
            }
        });
    }

    // Method to open AddQuestionActivity2
    public void openAddQuestionActivity2() {
        Intent intent = new Intent(this, AddQuestionActivity2.class);
        startActivity(intent);
    }

    // Method to open AddQuestionActivity4
    public void openAddQuestionActivity4() {
        Intent intent = new Intent(this, AddQuestionActivity4.class);
        startActivity(intent);
    }

    // Method to open CreateQuizActivity2
    public void openCreateQuizActivity2() {
        Intent intent = new Intent(this, CreateQuizActivity2.class);
        startActivity(intent);
    }
}