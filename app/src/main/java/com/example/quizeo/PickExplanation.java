package com.example.quizeo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class PickExplanation extends AppCompatActivity {

    String explanation;
    User user;
    Quiz quiz;
    LocationQuizeo location;
    boolean newQuiz;
    boolean verified;
    boolean darkmode;
    boolean sound;
    Question question;

    EditText explanationText;
    Button remove;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_explanation);

        darkmode = getIntent().getBooleanExtra("darkmode", false);
        if (darkmode) {
            findViewById(R.id.globeExplanation).setVisibility(View.INVISIBLE);
            findViewById(R.id.globeExplanationDark).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.globeExplanationDark).setVisibility(View.INVISIBLE);
            findViewById(R.id.globeExplanation).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        question = getIntent().getParcelableExtra("question");
        location = getIntent().getParcelableExtra("location");
        verified = getIntent().getBooleanExtra("verified", true);
        sound = getIntent().getBooleanExtra("sound", true);
        quiz = getIntent().getParcelableExtra("quiz");
        user = getIntent().getParcelableExtra("user");
        newQuiz = getIntent().getBooleanExtra("newquiz", false);

        add = (Button) findViewById(R.id.button_save_explanation);
        remove = (Button) findViewById(R.id.button_remove_explanation);
        explanationText = (EditText) findViewById(R.id.explanationText);
        System.out.println(question.getExplanation());
        if (!(question.getExplanation() == null)) {
            explanationText.setText(question.getExplanation());
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                explanation = explanationText.getText().toString();
                addQuestion();
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                explanation = "";
                addQuestion();
            }
        });

    }

    void addQuestion() {
        Intent intent = new Intent(this, AddQuestionActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("quiz", quiz);
        intent.putExtra("location", location);
        intent.putExtra("newquiz", newQuiz);
        intent.putExtra("verified", verified);
        intent.putExtra("darkmode", darkmode);
        intent.putExtra("sound", sound);
        intent.putExtra("question", question);
        intent.putExtra("explanation", explanation);
        startActivity(intent);
    }
}
