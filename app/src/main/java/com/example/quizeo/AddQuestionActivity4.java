package com.example.quizeo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AddQuestionActivity4 extends AppCompatActivity {

    // Declare local variables
    Button buttonRemoveOption;
    Button buttonSaveQuit;
    Button buttonDeleteQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_question_4);

        // Define variables with corresponding button
        buttonRemoveOption = (Button) findViewById(R.id.buttonRemoveOption);
        buttonSaveQuit = (Button) findViewById(R.id.buttonSaveQuit);
        buttonDeleteQuestion = (Button) findViewById(R.id.buttonDeleteQuestion);

        // Open previous activity with remove option button
        buttonRemoveOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddQuestionActivity3();
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

    // Method to open AddQuestionActivity3
    public void openAddQuestionActivity3() {
        Intent intent = new Intent(this, AddQuestionActivity3.class);
        startActivity(intent);
    }

    // Method to open CreateQuizActivity2
    public void openCreateQuizActivity2() {
        Intent intent = new Intent(this, CreateQuizActivity2.class);
        startActivity(intent);
    }
}
