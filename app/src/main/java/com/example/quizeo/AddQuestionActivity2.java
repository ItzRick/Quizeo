package com.example.quizeo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.io.OutputStreamWriter;

public class AddQuestionActivity2 extends AppCompatActivity {

    // Declare local variables
    Button buttonRemoveOption;
    Button buttonAddOption;
    Button buttonSaveQuit;
    Button buttonDeleteQuestion;

    EditText textQuestion;
    EditText textAddAnswer1;
    EditText textAddAnswer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_question_2);

        // Define variables with corresponding button
        buttonRemoveOption = (Button) findViewById(R.id.buttonRemoveOption);
        buttonAddOption = (Button) findViewById(R.id.buttonAddOption);
        buttonSaveQuit = (Button) findViewById(R.id.buttonSaveQuit);
        buttonDeleteQuestion = (Button) findViewById(R.id.buttonDeleteQuestion);

        textQuestion = (EditText) findViewById(R.id.textQuestion);
        textAddAnswer1 = (EditText) findViewById(R.id.textAddAnswer1);
        textAddAnswer2 = (EditText) findViewById(R.id.textAddAnswer2);

        // Open previous activity with remove option button
        buttonRemoveOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddQuestionActivity();
            }
        });

        // Open next activity with add option button
        buttonAddOption.setOnClickListener(new View.OnClickListener() {
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

    // Method to open AddQuestionActivity
    public void openAddQuestionActivity() {
        Intent intent = new Intent(this, AddQuestionActivity.class);
        startActivity(intent);
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
