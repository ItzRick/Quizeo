package com.example.quizeo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.UUID;

public class CreateQuizActivity extends AppCompatActivity {

    // Declare local variables for the UI elements:
    Button buttonBack;
    Button buttonNew;
    ScrollView quizesView;
    LinearLayout quizzesLayout;

    // Local variables for thw quiz, user and a boolean to determine if the quiz was new,
    // which is used in CreateQuizActivity2:
    Quiz quiz;
    User user;
    boolean newQuiz;

    // Local variables for the locaiton, if the quiz is verified or not, whether the darkmode
    // and sound are enabled:
    LocationQuizeo location;
    boolean verified;
    boolean darkmode;
    boolean sound;
    boolean active = false;

    // ArrayList to contain the quizzes:
    ArrayList<Quiz> quizzes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_quiz);

        // If the darkmode is enabled, set the colors appropriately, likewise if it isn't enabled:
        darkmode = getIntent().getBooleanExtra("darkmode", false);
        if (darkmode) {
            findViewById(R.id.globeCreate1).setVisibility(View.INVISIBLE);
            findViewById(R.id.globeCreate1Dark).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.globeCreate1Dark).setVisibility(View.INVISIBLE);
            findViewById(R.id.globeCreate1).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Define variables with corresponding button
        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonNew = (Button) findViewById(R.id.buttonNew);
        quizesView = (ScrollView) findViewById(R.id.quizzes_scroll_create);
        quizzesLayout = new LinearLayout(this);
        quizzesLayout.setOrientation(LinearLayout.VERTICAL);

        // Retrieve all objects that were pushed from the previous class:
        user = getIntent().getParcelableExtra("user");
        location = getIntent().getParcelableExtra("location");
        verified = getIntent().getBooleanExtra("verified", true);
        sound = getIntent().getBooleanExtra("sound", true);
        Database database = Database.getInstance();
        database.getQuizzes(user, true, new quizzesCallback());

        // Remove everything that currently might be in the quizesView:
        quizesView.removeAllViews();
        // Add the linearLayout to the ScrollView:
        quizesView.addView(quizzesLayout);

        // Open the home screen with back button
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

        // Open the CreateQuizActivity2 with new button
        buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newQuiz = true;
                editQuiz();
            }
        });
    }

    // Back button does the same thing as the button with the text "back"
    @Override
    public void onBackPressed() {
        openMainActivity();
    }

    // Method to open the home screen
    public void openMainActivity() {
        active = true;
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("location", location);
        intent.putExtra("user", user);
        intent.putExtra("verified", verified);
        intent.putExtra("darkmode", darkmode);
        intent.putExtra("sound", sound);
        startActivity(intent);
    }

    // Method to open CreateQuizActivity2
    public void editQuiz() {
        active = true;
        Intent intent = new Intent(this, CreateQuizActivity2.class);
        intent.putExtra("newquiz", newQuiz);
        intent.putExtra("location", location);
        intent.putExtra("user", user);
        intent.putExtra("quiz", quiz);
        intent.putExtra("verified", verified);
        intent.putExtra("darkmode", darkmode);
        intent.putExtra("sound", sound);
        startActivity(intent);
    }

    public void updateDisplay() {
        // If there were no quizzes passed, create a new empty arraylist (to prevent crashes):
        if (quizzes == null) {
            quizzes = new ArrayList<>();
        }

        // Create an array of buttons with the same length as the quizzes arraylist:
        Button[] buttons = new Button[quizzes.size()];
        for (int i = 0; i < quizzes.size(); i++) {
            // Create a new button for each quiz:
            buttons[i] = new Button(this);
            // Set the right colors for darkmode
            if (darkmode) {
                buttons[i].getBackground().setColorFilter(buttons[i].getContext().getResources().getColor(R.color.darkgray2), PorterDuff.Mode.MULTIPLY);
                buttons[i].setTextColor(ContextCompat.getColor(this, R.color.white2));
            }
            // Get all quizzes, set correctly if the quiz is verified and/or published:
            Quiz tempQuiz = quizzes.get(i);
            String verified1;
            String published1;
            if (tempQuiz.getNumberOfRatings() == -1) {
                published1 = "not Published";
                verified1 = "not Verified";
            } else {
                published1 = "Published";
                if (tempQuiz.getNumberOfRatings() >= 100 && tempQuiz.getRating() >= 7.5) {
                    verified1 = "Verified";
                } else {
                    verified1 = "not Verified";
                }
            }

            // Set the correct text for this quiz:
            String string = "Quiz: " + quizzes.get(i).getQuizName() + "\n" +
                    quizzes.get(i).getNumberOfQuestions() +
                    " questions \n" + "Created by: " + quizzes.get(i).getUserCreated().getNickName()
                    + "\n" + published1 + "               " + verified1;
            buttons[i].setText(string);
            buttons[i].setTag(i);
            // Add the button to the linearlayout:
            quizzesLayout.addView(buttons[i]);
            // Set a clicklistener which makes sure you can open that quiz:
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int) v.getTag();
                    quiz = quizzes.get(tag);
                    newQuiz = false;
                    editQuiz();
                }
            });

        }

        // If there are no quizzes, display that there are no quizzes:
        if (quizzes.size() == 0) {
            TextView noQuizes = new TextView(this);
            noQuizes.setTextSize(38);
            if (darkmode) {
                noQuizes.setTextColor(Color.parseColor("#FF121212"));
            } else {
                noQuizes.setTextColor(Color.WHITE);
            }
            noQuizes.setText("No quizzes found!");
            noQuizes.setGravity(Gravity.CENTER);
            quizzesLayout.addView(noQuizes);
        }
    }

    /**
     * Class which can be used to download all quizzes from the database, updates the display.
     */
    private class quizzesCallback implements Database.DownloadQuizzesCallback {
        @Override
        public void onCallback(ArrayList<Quiz> list) {
            quizzes = new ArrayList<>(list);
            updateDisplay();
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
