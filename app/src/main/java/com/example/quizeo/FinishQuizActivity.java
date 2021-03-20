package com.example.quizeo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FinishQuizActivity extends AppCompatActivity {

    Quiz quiz;
    User currentUser;

    private Button buttonMainMenu;
    private TextView quizInfo;
    private TextView quizName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_finish_quiz);

        quiz = getIntent().getParcelableExtra("quiz");
        currentUser = getIntent().getParcelableExtra("user");

        buttonMainMenu = findViewById(R.id.main_menu);
        quizInfo = findViewById(R.id.finished_quiz_info);
        quizName = findViewById(R.id.quiz_name);


        buttonMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainMenu();
            }
        });

        quizName.setText(quiz.getQuizName());
    }

    public void goToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}