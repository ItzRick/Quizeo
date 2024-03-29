package com.example.quizeo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.view.Gravity;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.UUID;

public class PlayQuizActivity extends AppCompatActivity implements Database.DownloadQuizzesCallback {

    //local variables for UI elements:
    Button buttonBack;
    ScrollView quizzesView;
    LinearLayout quizzesLayout;

    // Variables for the quizzes and users:
    ArrayList<Quiz> quizzes;
    Quiz quiz;
    User user;

    // Variables for retrieving the quizzes:
    Database database;
    LocationQuizeo location;

    // Local variable
    boolean verified;
    boolean darkmode;
    boolean sound;

    boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the xml file and retrieve the UI elements and the database instance, also retrieve
        // which user is going to answer this quiz:
        setContentView(R.layout.fragment_play_quiz);

        darkmode = getIntent().getBooleanExtra("darkmode", false);
        if (darkmode) {
            findViewById(R.id.globePlay).setVisibility(View.INVISIBLE);
            findViewById(R.id.globePlayDark).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.globePlayDark).setVisibility(View.INVISIBLE);
            findViewById(R.id.globePlay).setVisibility(View.VISIBLE);
        }

        buttonBack = (Button) findViewById(R.id.buttonBack);
        quizzesView = (ScrollView) findViewById(R.id.quizzes_scroll);
        user = getIntent().getParcelableExtra("userAnswered");

//        quizzes = new ArrayList<>();

        // Add a listener to the back button:
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
    }

        public void openMainActivity() {
            active = true;
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("verified", verified);
            intent.putExtra("darkmode", darkmode);
            intent.putExtra("sound", sound);
            intent.putExtra("location", location);
            intent.putExtra("user", user);
            startActivity(intent);
        }

    /** To retrieve the quizzes in a certain location. */
    @Override
    public void onCallback(ArrayList<Quiz> list) {
        quizzes = new ArrayList<>(list);
        updateDisplay();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Retrieve all objects that were pushed from the previous class:
        location = getIntent().getParcelableExtra("location");
        verified = getIntent().getBooleanExtra("verified", false);
        sound = getIntent().getBooleanExtra("sound", true);

        quizzesLayout = new LinearLayout(this);
        quizzesLayout.setOrientation(LinearLayout.VERTICAL);

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            database = Database.getInstance();
            database.getQuizzes(location, new callBackQuizzes());
        } else {
            quizzes = new ArrayList<>();
            updateDisplay();
        }


        quizzesView.removeAllViews();
        // Add the linearLayout to the ScrollView:
        quizzesView.addView(quizzesLayout);
//        String string1 = "this is a question";
//        String string2 = "this is another question";
//        String[] array = new String[]{
//                "first, two, third, fourth,fithshgakjhgadhaglkhsalfadjh", "two", "three", "four", "five"
//        };
//        String correct = "two";
//        String explanation = "this is an explanation!";
//        int id = 1;
//        UUID globalId = UUID.randomUUID();
//        Question question = new Question(string1, array, correct, explanation, id, globalId);
//        Question question1 = new Question(string2, array, correct, explanation, id + 1, globalId);
//        UUID id1 = UUID.randomUUID();
//        String quizName = "This is a quiz";
//        LocationQuizeo location1 = new LocationQuizeo(1, 1);
//        Quiz quiz1 = new Quiz(id1, quizName, location1);
//        quiz1.addQuestion(question);
//        quiz1.addQuestion(question1);
//        String nickName = "test";
//        String id2 = UUID.randomUUID().toString();
//        User user1 = new User(nickName, id2);
//        quiz1.setUserCreated(user1);
//        quizzes.add(quiz1);
//        quizzes.add(quiz1);

    }

    // Back button does the same thing as the button with the text "back"
    @Override
    public void onBackPressed() {
        openMainActivity();
    }

    /** To invoke the AnswerQuizActivity class and play the quiz. */
    public void playQuiz() {
            active = true;
            Intent intent = new Intent(this, AnswerQuizActivity.class);
            intent.putExtra("verified", verified);
            intent.putExtra("darkmode", darkmode);
            intent.putExtra("sound", sound);
            intent.putExtra("location", location);
            intent.putExtra("quiz", quiz);
            intent.putExtra("user", user);
            startActivity(intent);
    }

    public void updateDisplay() {
        // If there were no quizzes passed, create a new empty arraylist (to prevent crashes):
        if (quizzes == null) {
            quizzes = new ArrayList<>();
        }
        if (verified) {
            for (int i = 0; i < quizzes.size(); i++) {
                Quiz tempQuiz = quizzes.get(i);
                if (tempQuiz.getNumberOfRatings() < 100 || tempQuiz.getRating() < 7.5) {

                    quizzes.remove(i);
                    i--;
                }
            }
        }


        // Create an array of buttons with the same length as the quizzes arraylist:
        Button[] buttons = new Button[quizzes.size()];
//        System.out.println(quizzes.size());
        for (int i = 0; i < quizzes.size(); i++) {
            System.out.println("number of ratings1: " + quizzes.get(i).getNumberOfRatings());
            System.out.println(quizzes.get(i).getRating());
            // Create a new button for each quiz:
            buttons[i] = new Button(this);
            // Set the right colors for darkmode
            if (darkmode) {
                buttons[i].getBackground().setColorFilter(buttons[i].getContext().getResources().getColor(R.color.darkgray2), PorterDuff.Mode.MULTIPLY);
                buttons[i].setTextColor(ContextCompat.getColor(this, R.color.white2));
            }
            // Set the correct text for this quiz:
            String string = "Quiz: " + quizzes.get(i).getQuizName() + "\n" +
                    quizzes.get(i).getNumberOfQuestions() +
                    " questions \n" + "Created by: " + quizzes.get(i).getUserCreated().getNickName();
            buttons[i].setText(string);
            buttons[i].setTag(i);
            // Add the button to the linearlayout:
            quizzesLayout.addView(buttons[i]);
            // Set a clicklistener which makes sure you can play that quiz:
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int) v.getTag();
                    quiz = quizzes.get(tag);
                    database.getQuestions(quiz.getQuizId(), new callBackQuestions());
//                    playQuiz();
                }
            });

        }

        // If there are no quizzes: display this:
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


    private class callBackQuizzes implements  Database.DownloadQuizzesCallback {

        @Override
        public void onCallback(ArrayList<Quiz> list) {
            quizzes = new ArrayList<>(list);
            updateDisplay();
        }
    }

    private class callBackQuestions implements Database.DownloadQuestionListCallback {

        @Override
        public void onCallback(ArrayList<Question> list) {
            quiz.addQuestions(list);
            playQuiz();
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
