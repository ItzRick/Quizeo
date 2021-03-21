package com.example.quizeo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FinishQuizActivity extends AppCompatActivity {

    // variables
    Quiz quiz;
    User currentUser;
    AnswerQuiz answerQuiz;

    private Button buttonMainMenu;
    private Button buttonQuizzesMenu;
    private TextView quizInfo;
    private TextView quizName;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_finish_quiz);

        // get the same instances of classes as the previous activity
        quiz = getIntent().getParcelableExtra("quiz");
        currentUser = getIntent().getParcelableExtra("user");
        answerQuiz = getIntent().getParcelableExtra("answerQuiz");

        // assign components of the screen to values
        buttonMainMenu = findViewById(R.id.main_menu);
        buttonQuizzesMenu = findViewById(R.id.quizes_menu);
        quizInfo = findViewById(R.id.finished_quiz_info);
        quizName = findViewById(R.id.quiz_name);



        // when this button is pressed, go back to the main menu of the app
        buttonMainMenu.setOnClickListener(v -> goToMainMenu());

        // when this button is pressed, go back to the available quizzes menu of the app
        buttonQuizzesMenu.setOnClickListener(v -> {
            goToQuizzesMenu();
        });

        // show the name of the quiz
        quizName.setText(quiz.getQuizName());


        // if score is sufficient (>= 75% correct), print a corresponding message, else print diff message
        if (calculateScore(answerQuiz.getScore(), quiz.getNumberOfQuestions()) >= 0.75) {
            quizInfo.setText("Congratulations!\n You passed this quiz!\n You got "
                    + answerQuiz.getScore() + " out of " + quiz.getNumberOfQuestions() + " correct.");
        } else {
            quizInfo.setText("Better luck next time!\n You failed this quiz..\n You got "
                    + answerQuiz.getScore() + " out of " + quiz.getNumberOfQuestions() + " correct.");
        }


    }

    /**
     * Transitions to the available quizzes menu, where the user can select a new quiz.
     */
    private void goToQuizzesMenu() {
        Intent intent = new Intent(this, PlayQuizActivity.class);
        startActivity(intent);
    }


    /**
     * Transitions to the main menu screen
     */
    public void goToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Method to calculate the score in percents.
     * @param num numenator
     * @param denom denominator
     * @return percentage num of denom.
     */
    public double calculateScore (int num, int denom) {
        return ((double) num) / denom;
    }
}