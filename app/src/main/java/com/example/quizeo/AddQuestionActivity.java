package com.example.quizeo;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.UUID;

public class AddQuestionActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;

    // Declare local variables
    ImageView imageUpload;

    Button buttonSaveQuit;
    Button buttonDeleteQuestion;
    Button buttonAddOption;
    ScrollView answersView;
    LinearLayout answersLayout;

    EditText textQuestion;
    ArrayList<EditText> answers;
    ArrayList<Button> removeButtons;
    ArrayList<Button> correctButtons;
    ArrayList<LinearLayout> layouts;

    boolean newQuiz;

    LocationQuizeo location;
    Quiz quiz;
    User user;
    int correct;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_question);

    }

    @Override
    protected void onStart() {
        super.onStart();
        buttonSaveQuit = (Button) findViewById(R.id.buttonSaveQuit);
        buttonDeleteQuestion = (Button) findViewById(R.id.buttonDeleteQuestion);
        buttonAddOption = (Button) findViewById(R.id.buttonAddOption);

        answersView = (ScrollView) findViewById(R.id.answers_scroll);
        textQuestion = (EditText) findViewById(R.id.textQuestion);
        answersLayout = new LinearLayout(this);
        answersLayout.setOrientation(LinearLayout.VERTICAL);

        location = getIntent().getParcelableExtra("location");
        quiz = getIntent().getParcelableExtra("quiz");
        user = getIntent().getParcelableExtra("user");
        newQuiz = getIntent().getBooleanExtra("newquiz", false);
        answers = new ArrayList<>();
        removeButtons = new ArrayList<>();
        correctButtons = new ArrayList<>();
        layouts = new ArrayList<>();


        correct = -1;
        index = 0;

        for (int i = 0; i < 2; i++) {
            addOption();

        }
        answersView.addView(answersLayout);
        // Open next activity with add option button
        buttonAddOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOption();
            }
        });

        // Open the CreateQuizActivity2 with the save & quit button
        buttonSaveQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (correct == -1 ) {
                    showPopup(v, R.layout.popup_no_correct);
                } else if (textQuestion.getText().toString().equals("")) {
                    showPopup(v, R.layout.popup_no_question);
                    return;
                } else {
                    String[] answerss = new String[answers.size()];
                    int i = 0;
                    for (EditText answer : answers) {
                        if (answer.getText().toString().equals("")) {
                            showPopup(v, R.layout.popup_no_answer);
                            return;
                        } else {
                            answerss[i] = answer.getText().toString();
                            i++;
                        }
                    }
                    Question question = new Question(textQuestion.getText().toString(), answerss,
                            correct + 1, "", quiz.getNumberOfQuestions() + 1,
                            UUID.randomUUID());
                    quiz.addQuestion(question);
                    createQuiz();
                }
            }
        });

        // Open the CreateQuizActivity2 with the delete button
        buttonDeleteQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createQuiz();
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
//            Uri selectedImage = data.getData();
//            imageUpload.setImageURI(selectedImage);
//        }
//    }

    void createQuiz() {
        Intent intent = new Intent(this, CreateQuizActivity2.class);
        intent.putExtra("user", user);
        intent.putExtra("quiz", quiz);
        intent.putExtra("location", location);
        intent.putExtra("newquiz", newQuiz);
        startActivity(intent);
    }

    void addOption() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 30, 0, 0);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        LinearLayout tempLayout = new LinearLayout(this);
        tempLayout.setOrientation(LinearLayout.HORIZONTAL);
        Button tempButton = new Button(this);
        tempButton.setTag(index);
        tempButton.setBackgroundColor(Color.WHITE);

        LinearLayout.LayoutParams lparams1 = new LinearLayout.LayoutParams((int) (0.05*width), (int) (0.05*height));
        lparams1.setMargins(20, 0, 20, 0);
        tempButton.setLayoutParams(lparams1);
        correctButtons.add(tempButton);
        tempLayout.addView(tempButton);




        EditText tempEdit = new EditText(this);
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams((int) (0.75 * width), LinearLayout.LayoutParams.WRAP_CONTENT);
        tempEdit.setGravity(Gravity.CENTER);
        tempEdit.setLayoutParams(lparams);
        tempEdit.setBackgroundColor(Color.WHITE);
        tempEdit.setTextColor(Color.BLACK);
        tempEdit.setHint("Enter answer text");
        tempLayout.addView(tempEdit);
        answers.add(tempEdit);

        Button tempButton1 = new Button(this);
        tempButton1.setTag(index);
        tempButton1.setBackgroundColor(Color.RED);

        LinearLayout.LayoutParams lparams2 = new LinearLayout.LayoutParams((int) (0.05*width), (int) (0.05*height));
        lparams2.setMargins(20, 0, 20, 0);
        tempButton1.setLayoutParams(lparams2);
        removeButtons.add(tempButton1);
        tempLayout.addView(tempButton1);
        layouts.add(tempLayout);
        answersLayout.addView(tempLayout, layoutParams);
        index++;

        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) v.getTag();
                correct = tag;
                for (Button button : correctButtons) {
                    button.setBackgroundColor(Color.WHITE);
                }
                Button tempButton = correctButtons.get(tag);
                tempButton.setBackgroundColor(Color.GREEN);
            }
        });

        tempButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) v.getTag();
                LinearLayout child = layouts.get(tag);
                answersLayout.removeView(child);
                answers.remove(tag);
                removeButtons.remove(tag);
                correctButtons.remove(tag);
                layouts.remove(tag);
                if (tag < correct) {
                    correct--;
                }
            }
        });
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
}
