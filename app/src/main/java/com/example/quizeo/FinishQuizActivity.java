package com.example.quizeo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FinishQuizActivity extends AppCompatActivity {

    Quiz quiz;
    User currentUser;
    AnswerQuiz answerQuiz;

    private Button buttonMainMenu;
    private TextView quizInfo;
    private TextView quizName;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_finish_quiz);

        quiz = getIntent().getParcelableExtra("quiz");
        currentUser = getIntent().getParcelableExtra("user");
        answerQuiz = getIntent().getParcelableExtra("answerQuiz");
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

        if (calculateScore(answerQuiz.getScore(), quiz.getNumberOfQuestions()) >= 0.75) {
            quizInfo.setText("Congratulations!\n You passed this quiz!\n You got "
                    + answerQuiz.getScore() + " out of " + quiz.getNumberOfQuestions() + " correct.");
        } else {
            quizInfo.setText("Better luck next time!\n You failed this quiz..\n You got "
                    + answerQuiz.getScore() + " out of " + quiz.getNumberOfQuestions() + " correct.");
        }


    }

    public void goToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public double calculateScore (int num, int denom) {
        return ((double) num) / denom;
    }
}