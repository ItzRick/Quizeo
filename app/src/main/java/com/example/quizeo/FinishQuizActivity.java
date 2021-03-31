package com.example.quizeo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class FinishQuizActivity extends AppCompatActivity {

    // variables
    Quiz quiz;
    User currentUser;
    AnswerQuiz answerQuiz;

    private Button buttonMainMenu;
    private Button buttonQuizzesMenu;
    private TextView quizInfo;
    private TextView quizName;
    private RatingBar ratingBar;

    boolean verified;
    boolean darkmode;
    boolean toMainMenu;
    LocationQuizeo location;

    Database database;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_finish_quiz);

        darkmode = getIntent().getBooleanExtra("darkmode", false);
        if (darkmode) {
            findViewById(R.id.globeFinish).setVisibility(View.INVISIBLE);
            findViewById(R.id.globeFinishDark).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.globeFinishDark).setVisibility(View.INVISIBLE);
            findViewById(R.id.globeFinish).setVisibility(View.VISIBLE);
        }

        // get the same instances of classes as the previous activity
        verified = getIntent().getBooleanExtra("verified", true);
        quiz = getIntent().getParcelableExtra("quiz");
        currentUser = getIntent().getParcelableExtra("user");
        answerQuiz = getIntent().getParcelableExtra("answerQuiz");
        location = getIntent().getParcelableExtra("location");

        // assign components of the screen to values
        buttonMainMenu = findViewById(R.id.main_menu);
        buttonQuizzesMenu = findViewById(R.id.quizes_menu);
        quizInfo = findViewById(R.id.finished_quiz_info);
        quizName = findViewById(R.id.quiz_name);
        ratingBar = findViewById(R.id.rating_bar);
        database = Database.getInstance();


        // when this button is pressed, go back to the main menu of the app
        buttonMainMenu.setOnClickListener(v -> {
            toMainMenu = true;
            database.getQuestions(quiz.getQuizId(), new QuestionCallback());
//            goToMainMenu();
        });

        // when this button is pressed, go back to the available quizzes menu of the app
        buttonQuizzesMenu.setOnClickListener(v -> {
            toMainMenu = false;
            database.getQuestions(quiz.getQuizId(), new QuestionCallback());
//            goToQuizzesMenu();
        });

        // when the rating bar is touched, retrieve the rating and add it to the other ratings
//        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> quiz.setAdditionalRating(rating));

        // show the name of the quiz
        quizName.setText(quiz.getQuizName());


        // if score is sufficient (>= 75% correct), print a corresponding message, else print diff message
        if (answerQuiz.getScore() >= quiz.getScoreToPass()) {
            quizInfo.setText("Congratulations!\n You passed this quiz!\n You got "
                    + answerQuiz.getScore() + " out of " + quiz.getNumberOfQuestions() + " correct.");
            if (darkmode) {
                quizInfo.setBackgroundColor(getResources().getColor(R.color.darkgreen));
            } else {
                quizInfo.setBackgroundColor(Color.GREEN);
            }
        } else {
            quizInfo.setText("Better luck next time!\n You failed this quiz..\n You got "
                    + answerQuiz.getScore() + " out of " + quiz.getNumberOfQuestions() + " correct.");
            if (darkmode) {
                quizInfo.setBackgroundColor(getResources().getColor(R.color.redd));
            } else {
                quizInfo.setBackgroundColor(Color.RED);
            }
        }

    }

    // Back button does the same thing as return to menu button
    @Override
    public void onBackPressed() {
        Button b = findViewById(R.id.main_menu);
        b.callOnClick();
    }

    /**
     * Transitions to the available quizzes menu, where the user can select a new quiz.
     */
    private void goToQuizzesMenu() {
//        database.removeQuiz(quiz);
////        System.out.println(quiz.getQuestions().length);
//        database.uploadQuiz(quiz, true);
        Intent intent = new Intent(this, PlayQuizActivity.class);
        intent.putExtra("location", location);
        intent.putExtra("verified", verified);
        intent.putExtra("darkmode", darkmode);
        intent.putExtra("user", currentUser);
        startActivity(intent);
    }


    /**
     * Transitions to the main menu screen
     */
    public void goToMainMenu() {
//        database.removeQuiz(quiz);
//        database.uploadQuiz(quiz, true);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("location", location);
        intent.putExtra("verified", verified);
        intent.putExtra("darkmode", darkmode);
        intent.putExtra("user", currentUser);
        startActivity(intent);
    }

//    /**
//     * Method to calculate the score in percents.
//     * @param num numenator
//     * @param denom denominator
//     * @return percentage num of denom.
//     */
//    public double calculateScore (int num, int denom) {
//        return ((double) num) / denom;
//    }
    private class QuestionCallback implements Database.DownloadQuestionListCallback {
        @Override
        public void onCallback(ArrayList<Question> list) {
            if (ratingBar.getRating() > 0) {
                System.out.println(ratingBar.getRating());
                quiz.setAdditionalRating(ratingBar.getRating());
            }
            database.removeQuiz(quiz);
            quiz.addQuestions(list);
            database.uploadQuiz(quiz, true);
//            System.out.println("number of ratings: " + quiz.getNumberOfRatings());
            if (toMainMenu) {
                goToMainMenu();
            } else {
                goToQuizzesMenu();
            }
        }
    }
}