package com.example.quizeo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.UUID;

public class PlayQuizActivity extends AppCompatActivity implements Database.DownloadQuizzesCallback {

    //local variables
    Button buttonBack;

    ArrayList<Quiz> quizzes;
    Database database;
    ScrollView quizzesView;
    Quiz quiz;
    User user;

    LocationQuizeo location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_play_quiz);
        buttonBack = (Button) findViewById(R.id.buttonBack);
        database = Database.getInstance();
        quizzesView = (ScrollView) findViewById(R.id.quizzes_scroll);
        user = getIntent().getParcelableExtra("userAnswered");

        //quizzes = new ArrayList<>();

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
    }

        public void openMainActivity() {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    @Override
    public void onCallback(ArrayList<Quiz> list) {
        quizzes = new ArrayList<>(list);
    }

    @Override
    protected void onStart() {
        super.onStart();
        location = getIntent().getParcelableExtra("location");
        database.getQuizzes(location, this);

//        String string1 = "this is a question";
//        String string2 = "this is another question";
//        String[] array = new String[]{
//                "first", "two", "three", "four", "five"
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

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        Button[] buttons = new Button[quizzes.size()];
        for (int i = 0; i < quizzes.size(); i++) {
            buttons[i] = new Button(this);
            String string = "Quiz: " + quizzes.get(i).getQuizName() + "\n" +
                    quizzes.get(i).getNumberOfQuestions() +
                    " questions \n" + "Created by: " + quizzes.get(i).getUserCreated().getNickName();
            buttons[i].setText(string);
            buttons[i].setTag(i);
            linearLayout.addView(buttons[i]);
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int) v.getTag();
                    quiz = quizzes.get(tag);
                    playQuiz();
                }
            });

        }
        quizzesView.addView(linearLayout);
    }
    public void playQuiz() {
        if (quiz.getFirst().getNumberOfAnswers() == 2) {
            Intent intent = new Intent(this, AnswerQuizActivity2.class);
            intent.putExtra("location", location);
            intent.putExtra("quiz", quiz);
            intent.putExtra("user", user);
            startActivity(intent);
        } else if (quiz.getFirst().getNumberOfAnswers() == 3) {
            Intent intent = new Intent(this, AnswerQuizActivity3.class);
            intent.putExtra("location", location);
            intent.putExtra("quiz", quiz);
            intent.putExtra("user", user);
            startActivity(intent);
        } else if (quiz.getFirst().getNumberOfAnswers() == 4) {
            Intent intent = new Intent(this, AnswerQuizActivity4.class);
            intent.putExtra("location", location);
            intent.putExtra("quiz", quiz);
            intent.putExtra("user", user);
            startActivity(intent);
        } else if (quiz.getFirst().getNumberOfAnswers() == 5) {
            Intent intent = new Intent(this, AnswerQuizActivity5.class);
            intent.putExtra("location", location);
            intent.putExtra("quiz", quiz);
            intent.putExtra("user", user);
            startActivity(intent);
        }


    }
}
