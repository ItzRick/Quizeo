package com.example.quizeo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.UUID;

public class CreateQuizActivity2 extends AppCompatActivity {

    // Declare local variables for UI elements:
    Button buttonSaveQuit;
    Button buttonAddQuestion;
    Button buttonAddLocation;
    Button buttonSubmitQuiz;
    Button buttonRemoveQuestion;
    TextView locationAdded;
    TextView numberOfQuestions;
    EditText quizName;

    // Local variables:
    boolean publish;
    boolean newQuiz;
    boolean verified;
    boolean darkmode;
    boolean sound;
    boolean active = false;

    // Variables for the quiz elements:
    Database database;
    Quiz quiz;
    LocationQuizeo location;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set the correct layout:
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_create_quiz_2);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Retrieve the UI elements:
        setContentView(R.layout.fragment_create_quiz_2);
        buttonSaveQuit = (Button) findViewById(R.id.buttonSaveQuit);
        quizName = (EditText) findViewById(R.id.YourQuizName);
        buttonAddQuestion = (Button) findViewById(R.id.buttonAddQuestion);
        numberOfQuestions = (TextView) findViewById(R.id.NumberOfQuestions);
        buttonSubmitQuiz = (Button) findViewById(R.id.buttonSubmitQuiz);
        locationAdded = (TextView) findViewById(R.id.textLocation);
        buttonAddLocation = (Button) findViewById(R.id.buttonAddLocation);
        buttonRemoveQuestion = (Button) findViewById(R.id.buttonRemoveQuestion);

        // Get the parcelables from the previous class:
        user = getIntent().getParcelableExtra("user");
        verified = getIntent().getBooleanExtra("verified", true);
        sound = getIntent().getBooleanExtra("sound", true);
        location = getIntent().getParcelableExtra("location");
        database = Database.getInstance();

        newQuiz = getIntent().getBooleanExtra("newquiz", false);
        // Retrieve passed quiz element if it exists:
        quiz = getIntent().getParcelableExtra("quiz");

        // Set new if the quiz does not exist:
        if (quiz == null) {
            quiz = new Quiz();
            quiz.setQuizId(UUID.randomUUID());
            quiz.setUserCreated(user);
        } else {
            quizName.setText(quiz.getQuizName());
        }

        // If the quiz was not new, set the location to the previous value:
        if (!newQuiz) {
            quiz.setLocation(location);
            String text = "Location has been added!";
            locationAdded.setText(text);
        }

        // Set the number of questions text:
        String textToSet = "Number of questions: " + quiz.getNumberOfQuestions();
        numberOfQuestions.setText(textToSet);

        // If darkmode, change colors appropriately:
        darkmode = getIntent().getBooleanExtra("darkmode", false);
        if (darkmode) {
            findViewById(R.id.globeCreate2).setVisibility(View.INVISIBLE);
            findViewById(R.id.globeCreate2Dark).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.globeCreate2Dark).setVisibility(View.INVISIBLE);
            findViewById(R.id.globeCreate2).setVisibility(View.VISIBLE);
        }

        // Add a clicklistener to the submit quiz button:
        buttonSubmitQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If the question is not correct, so there are too little questions,
                // or no quizname has been added, or there is no location, show the popup for this
                // error, otherwise submit quiz, if necessary with retrieving the questions
                // from the database.
                if (quiz.getNumberOfQuestions() < 5) {
                    showPopup(v, R.layout.popup_little_questions);
                    return;
                } else if (quizName.getText().toString().equals("")) {
                    showPopup(v, R.layout.popup_no_quizname);
                } else if (quiz.getLocation() == null) {
                    showPopup(v, R.layout.popup_no_location);
                    return;
                } else if (quiz.getQuestions().length != quiz.getNumberOfQuestions()) {
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
                // Save the quiz, while showing the pop up if there is an error:
                if (quiz.getLocation() ==  null) {
                    showPopup(v, R.layout.popup_no_location);
                    return;
                } else if (quizName.getText().toString().equals("")) {
                    showPopup(v, R.layout.popup_no_quizname);
                    return;
                } else if (quiz.getQuestions().length != quiz.getNumberOfQuestions()) {
                    publish = false;
                    quiz.setQuizName(quizName.getText().toString());
                    database.getQuestions(quiz.getQuizId(), new QuestionsCallback());
                    return;
                }
                quiz.setQuizName(quizName.getText().toString());
                quiz.setNrOfRatings(-1);
                database.removeQuiz(quiz);
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

        // Add the location with the buttonAddLocation, if no location can be found, show popup:
        buttonAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    quiz.setQuizName(quizName.getText().toString());
                    quiz.setLocation(location);
                    String text = "Location has been added!";
                    Log.d("lat", String.valueOf(location.getLatitude()));
                    Log.d("lon", String.valueOf(location.getLongitude()));
                    locationAdded.setText(text);
                    newQuiz = false;
                } catch (Exception e) {
                    showPopup(v, R.layout.popup_no_location_found);
                }

            }
        });

        // Remove a question if the button to remove a question is pressed:
        buttonRemoveQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database database = Database.getInstance();
                database.removeQuiz(quiz);
                openCreateQuizActivity();
            }
        });
    }

    // Back button does the same thing as the save and quit button
    @Override
    public void onBackPressed() {
        Button b = findViewById(R.id.buttonSaveQuit);
        b.callOnClick();
    }

    // Method to open the home screen
    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Method to open the home screen
    public void openCreateQuizActivity() {
        active = true;
        Intent intent = new Intent(this, CreateQuizActivity.class);
        intent.putExtra("verified", verified);
        intent.putExtra("darkmode", darkmode);
        intent.putExtra("sound", sound);
        intent.putExtra("user", user);
        intent.putExtra("location", location);
        startActivity(intent);
    }

    // Method to open the AddQuestionActivity
    public void openAddQuestion() {
        active = true;
        Intent intent = new Intent(this, AddQuestionActivity.class);
        intent.putExtra("verified", verified);
        intent.putExtra("darkmode", darkmode);
        intent.putExtra("sound", sound);
        intent.putExtra("quiz", quiz);
        intent.putExtra("user", user);
        intent.putExtra("location", location);
        intent.putExtra("newquiz", newQuiz);
        startActivity(intent);
    }

    /**
     * Shows a popup.
     *
     * @param v view to show the popup in.
     * @param popup Int of the popup layout file.
     */
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

    /**
     * Callback to download the questions from the database, used while uploading an existing quiz.
     */
    private class QuestionsCallback implements Database.DownloadQuestionListCallback {

        @Override
        public void onCallback(ArrayList<Question> list) {
            quiz.addQuestions(list);
            database.removeQuiz(quiz);
            database.uploadQuiz(quiz, publish);
            openCreateQuizActivity();
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