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

    // Local variables for thew quiz and user:
    Quiz quiz;
    User currentUser;
    AnswerQuiz answerQuiz;

    // Local variables for the UI elements:
    private Button buttonMainMenu;
    private Button buttonQuizzesMenu;
    private TextView quizInfo;
    private TextView quizName;
    private RatingBar ratingBar;

    // Variables that need to be passed to the next class:
    boolean verified;
    boolean darkmode;
    boolean sound;
    boolean active = false;
    boolean toMainMenu;
    LocationQuizeo location;
    Database database;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_finish_quiz);

        // Retrieve the darkmode variable and change the UI appropriately:
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
        sound = getIntent().getBooleanExtra("sound", true);
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
        });

        // when this button is pressed, go back to the available quizzes menu of the app
        buttonQuizzesMenu.setOnClickListener(v -> {
            toMainMenu = false;
            database.getQuestions(quiz.getQuizId(), new QuestionCallback());
        });

        // show the name of the quiz
        quizName.setText(quiz.getQuizName());


        // if score is sufficient (>= 75% correct), print a corresponding message,
        // else print failed message
        if (answerQuiz.getScore() >= quiz.getScoreToPass()) {
            Music.passedSound(this);
            quizInfo.setText("Congratulations!\n You passed this quiz!\n You got "
                    + answerQuiz.getScore() + " out of " + quiz.getNumberOfQuestions() +
                    " correct.");
            // Set the darkmode colors correctly:
            if (darkmode) {
                quizInfo.setBackgroundColor(getResources().getColor(R.color.darkgreen));
            } else {
                quizInfo.setBackgroundColor(Color.GREEN);
            }
        } else {
            Music.failedSound(this);
            quizInfo.setText("Better luck next time!\n You failed this quiz..\n You got "
                    + answerQuiz.getScore() + " out of " + quiz.getNumberOfQuestions() +
                    " correct.");
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
        active = true;
        Intent intent = new Intent(this, PlayQuizActivity.class);
        intent.putExtra("location", location);
        intent.putExtra("verified", verified);
        intent.putExtra("darkmode", darkmode);
        intent.putExtra("sound", sound);
        intent.putExtra("user", currentUser);
        startActivity(intent);
    }


    /**
     * Transitions to the main menu screen
     */
    public void goToMainMenu() {
        active = true;
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("location", location);
        intent.putExtra("verified", verified);
        intent.putExtra("darkmode", darkmode);
        intent.putExtra("sound", sound);
        intent.putExtra("user", currentUser);
        startActivity(intent);
    }

    /**
     * QuestionCallBAck that retrieves the questions of the current quiz, updates the rating and
     * then re-uploads the quiz to the database.
     */
    private class QuestionCallback implements Database.DownloadQuestionListCallback {
        @Override
        public void onCallback(ArrayList<Question> list) {
            // If there is a rating in the ratingbar, update the rating:
            if (ratingBar.getRating() > 0) {
                System.out.println(ratingBar.getRating());
                quiz.setAdditionalRating(ratingBar.getRating());
            }

            // Remove the quiz from the database, and add the questions again, then update it with
            // the correct rating:
            database.removeQuiz(quiz);
            quiz.addQuestions(list);
            database.uploadQuiz(quiz, true);
            // Go to either the main menu or the quizzes menu:
            if (toMainMenu) {
                goToMainMenu();
            } else {
                goToQuizzesMenu();
            }
        }
    }

    @Override
    public void onPause() {
        // Stop the music
        if (!active) {
            Music.stop(this);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Resume music
        if (!Music.isPlaying() && sound) {
            Music.play(this);
        }
    }

}