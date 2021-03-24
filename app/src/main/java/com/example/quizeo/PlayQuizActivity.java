package com.example.quizeo;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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

    // Variables for the quizzes and users:
    ArrayList<Quiz> quizzes;
    Quiz quiz;
    User user;

    // Variables for retrieving the quizzes:
    Database database;
    LocationQuizeo location;

    // Local variable
    boolean verified;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the xml file and retrieve the UI elements and the database instance, also retrieve
        // which user is going to answer this quiz:
        setContentView(R.layout.fragment_play_quiz);
        buttonBack = (Button) findViewById(R.id.buttonBack);
        database = Database.getInstance();
        quizzesView = (ScrollView) findViewById(R.id.quizzes_scroll);
        user = getIntent().getParcelableExtra("userAnswered");

        quizzes = new ArrayList<>();

        // Add a listener to the back button:
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
    }

        public void openMainActivity() {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }

    /** To retrieve the quizzes in a certain location. */
    @Override
    public void onCallback(ArrayList<Quiz> list) {
//        quizzes = new ArrayList<>(list);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Retrieve all objects that were pushed from the previous class:
        location = getIntent().getParcelableExtra("location");
        verified = getIntent().getBooleanExtra("verified", false);
        database.getQuizzes(location, this);


        String string1 = "this is a question";
        String string2 = "this is another question";
        String[] array = new String[]{
                "first, two, third, fourth,fithshgakjhgadhaglkhsalfadjh", "two", "three", "four", "five"
        };
        String correct = "two";
        String explanation = "this is an explanation!";
        int id = 1;
        UUID globalId = UUID.randomUUID();
        Question question = new Question(string1, array, correct, explanation, id, globalId);
        Question question1 = new Question(string2, array, correct, explanation, id + 1, globalId);
        UUID id1 = UUID.randomUUID();
        String quizName = "This is a quiz";
        LocationQuizeo location1 = new LocationQuizeo(1, 1);
        Quiz quiz1 = new Quiz(id1, quizName, location1);
        quiz1.addQuestion(question);
        quiz1.addQuestion(question1);
        quiz1.addQuestion(question);
        String nickName = "test";
        String id2 = UUID.randomUUID().toString();
        User user1 = new User(nickName, id2);
        quiz1.setUserCreated(user1);
        quizzes.add(quiz1);
        quizzes.add(quiz1);

        // If there were no quizzes passed, create a new empty arraylist (to prevent crashes):
        if (quizzes == null) {
            quizzes = new ArrayList<>();
        }

        // Remove all quizzes that are not verified.
        if (verified) {
            for (int i = 0; i < quizzes.size(); i++) {
                Quiz temp = quizzes.get(i);
                if (temp.getNumberOfRatings() < 100 && temp.getRating() < 0.75) {
                    quizzes.remove(i);
                    i--;
                }
            }
        }


        // Create a vertical linearlayout:
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // Create an array of buttons with the same length as the quizzes arraylist:
        Button[] buttons = new Button[quizzes.size()];
        for (int i = 0; i < quizzes.size(); i++) {
            // Create a new button for each quiz:
            buttons[i] = new Button(this);
            // Set the correct text for this quiz:
            String string = "Quiz: " + quizzes.get(i).getQuizName() + "\n" +
                    quizzes.get(i).getNumberOfQuestions() +
                    " questions \n" + "Created by: " + quizzes.get(i).getUserCreated().getNickName();
            buttons[i].setText(string);
            buttons[i].setTag(i);
            // Add the button to the linearlayout:
            linearLayout.addView(buttons[i]);
            // Set a clicklistener which makes sure you can play that quiz:
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int) v.getTag();
                    quiz = quizzes.get(tag);
                    playQuiz();
                }
            });

        }

        // If there are no quizzes: display this:
        if (quizzes.size() == 0) {
            TextView noQuizes = new TextView(this);
            noQuizes.setTextSize(38);
            noQuizes.setTextColor(Color.WHITE);
            noQuizes.setText("No quizzes found!");
            noQuizes.setGravity(Gravity.CENTER);
            linearLayout.addView(noQuizes);
        }
        // Add the linearLayout to the ScrollView:
        quizzesView.addView(linearLayout);
    }

    /** To invoke the AnswerQuizActivity class and play the quiz. */
    public void playQuiz() {
            Intent intent = new Intent(this, AnswerQuizActivity.class);
            intent.putExtra("location", location);
            intent.putExtra("quiz", quiz);
            intent.putExtra("user", user);
            startActivity(intent);
    }
}
