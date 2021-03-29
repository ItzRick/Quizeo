package com.example.quizeo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.UUID;

public class CreateQuizActivity2 extends AppCompatActivity {

    // Declare local variables
    Button buttonSaveQuit;
    Button buttonAddQuestion;
    Button buttonAddLocation;
    Button buttonSubmitQuiz;
    Button buttonRemoveQuestion;
    boolean publish;

    TextView locationAdded;
    boolean newQuiz;

    TextView numberOfQuestions;

    EditText quizName;
    EditText yourUserName;

    Database database;

    Quiz quiz;
    LocationQuizeo location;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_quiz_2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.fragment_create_quiz_2);
        buttonSaveQuit = (Button) findViewById(R.id.buttonSaveQuit);
        quizName = (EditText) findViewById(R.id.YourQuizName);
        yourUserName = (EditText) findViewById(R.id.YourUsername);
        buttonAddQuestion = (Button) findViewById(R.id.buttonAddQuestion);
        numberOfQuestions = (TextView) findViewById(R.id.NumberOfQuestions);

        buttonSubmitQuiz = (Button) findViewById(R.id.buttonSubmitQuiz);

        locationAdded = (TextView) findViewById(R.id.textLocation);
        buttonAddLocation = (Button) findViewById(R.id.buttonAddLocation);

        buttonRemoveQuestion = (Button) findViewById(R.id.buttonRemoveQuestion);

        user = getIntent().getParcelableExtra("user");

        location = getIntent().getParcelableExtra("location");
        database = Database.getInstance();

        newQuiz = getIntent().getBooleanExtra("newquiz", false);
        // Retrieve passed quiz element if it exists:
        quiz = getIntent().getParcelableExtra("quiz");

        // Set new if it does not exist:
        if (quiz == null) {
            quiz = new Quiz();
            quiz.setQuizId(UUID.randomUUID());
            quiz.setUserCreated(user);
        } else {
            quizName.setText(quiz.getQuizName());
        }

        if (!newQuiz) {
            quiz.setLocation(location);
            String text = "Location has been added!";
            locationAdded.setText(text);
        }


        String textToSet = "Number of questions: " + quiz.getNumberOfQuestions();
        numberOfQuestions.setText(textToSet);

        buttonSubmitQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quiz.getNumberOfQuestions() < 5) {
                    showPopup(v, R.layout.popup_little_questions);
                    return;
                } else if (quizName.getText().toString().equals("")) {
                    showPopup(v, R.layout.popup_no_quizname);
                } else if (quiz.getLocation() == null) {
                    showPopup(v, R.layout.popup_no_location);
                    return;
                } else if (quiz.getQuestions().length == 0) {
                    publish = true;
                    quiz.setQuizName(quizName.getText().toString());
                    if (quiz.getNumberOfRatings() == - 1) {
                        quiz.setNrOfRatings(0);
                    }
                    database.getQuestions(quiz.getQuizId(), new QuestionsCallback());
                    return;
                } else {
                    quiz.setQuizName(quizName.getText().toString());
                    if (quiz.getNumberOfRatings() == - 1) {
                        quiz.setNrOfRatings(0);
                    }
                    database.uploadQuiz(quiz, true);
                    openCreateQuizActivity();
                }
            }
        });

        // Open the home screen with the save & quit button
        buttonSaveQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quiz.getLocation() ==  null) {
                    showPopup(v, R.layout.popup_no_location);
                    return;
                } else if (quizName.getText().toString().equals("")) {
                    showPopup(v, R.layout.popup_no_quizname);
                } else if (quiz.getQuestions().length == 0 && !newQuiz) {
                    database.getQuestions(quiz.getQuizId(), new QuestionsCallback());
                    publish = false;
                    quiz.setQuizName(quizName.getText().toString());
                    return;
                }
                quiz.setQuizName(quizName.getText().toString());
                quiz.setNrOfRatings(-1);
                database.removeQuiz(quiz);
//                System.out.println("GlobalID" + quiz.getQuestions()[0].getGlobalId());
                database.uploadQuiz(quiz, false);
                openCreateQuizActivity();
            }
        });

        // Open the AddQuestionActivity with add question button
        buttonAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quiz.setNrOfRatings(-1);
                quiz.setQuizName(quizName.getText().toString());
                openAddQuestion();
            }
        });

        buttonAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quiz.setQuizName(quizName.getText().toString());
                quiz.setLocation(location);
                String text = "Location has been added!";
                locationAdded.setText(text);
                newQuiz = false;
            }
        });

        buttonRemoveQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database database = Database.getInstance();
                database.removeQuiz(quiz);
                openCreateQuizActivity();
            }
        });
    }

    // Method to open the home screen
    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Method to open the home screen
    public void openCreateQuizActivity() {
        Intent intent = new Intent(this, CreateQuizActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("location", location);
        startActivity(intent);
    }

    // Method to open the AddQuestionActivity
    public void openAddQuestion() {
        Intent intent = new Intent(this, AddQuestionActivity.class);
        intent.putExtra("quiz", quiz);
        intent.putExtra("user", user);
        intent.putExtra("location", location);
        intent.putExtra("newquiz", newQuiz);
        startActivity(intent);
    }

    void showPopup (View v, int popup) {
        // Create a pop up that the question has already been answered:
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                v.getContext().getSystemService
                        (v.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView =
                inflater.inflate(popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        // Makes sure you can also press outside the popup to dismiss it:
        boolean focusable = true;
        PopupWindow popupWindow =
                new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    private class QuestionsCallback implements Database.DownloadQuestionListCallback {

        @Override
        public void onCallback(ArrayList<Question> list) {
            quiz.addQuestions(list);
            database.removeQuiz(quiz);
//                System.out.println("GlobalID" + quiz.getQuestions()[0].getGlobalId());
            database.uploadQuiz(quiz, publish);
            openCreateQuizActivity();
        }
    }
}