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

import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class AddQuestionActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;

    // Declare local variables
    ImageView imageUpload;

    Button buttonSaveQuit;
    Button buttonDeleteQuestion;
    Button buttonAddOption;

    EditText textQuestion;
    EditText textAddAnswer1;
    EditText textAddAnswer2;

    LocationQuizeo location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_question);

        // Define variables with corresponding image/button/text
        imageUpload = (ImageView) findViewById(R.id.imageUpload);

        buttonSaveQuit = (Button) findViewById(R.id.buttonSaveQuit);
        buttonDeleteQuestion = (Button) findViewById(R.id.buttonDeleteQuestion);
        buttonAddOption = (Button) findViewById(R.id.buttonAddOption);

        textQuestion = (EditText) findViewById(R.id.textQuestion);
        textAddAnswer1 = (EditText) findViewById(R.id.textAddAnswer1);
        textAddAnswer2 = (EditText) findViewById(R.id.textAddAnswer2);

        location = getIntent().getParcelableExtra("location");

        // Open next activity with add option button
        buttonAddOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddQuestionActivity2();
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

    // Method to open the AddQuestionsActivity2
    public void openAddQuestionActivity2() {
        Intent intent = new Intent(this, AddQuestionActivity2.class);
        startActivity(intent);
    }

    // Method to open the CreateQuizActivity2
    public void openCreateQuizActivity2() {
        Intent intent = new Intent(this, CreateQuizActivity2.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imageUpload.setImageURI(selectedImage);
        }
    }
}
