package com.example.quizeo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
//    ImageView imageUpload;

    Button buttonSaveQuit;
    Button buttonDeleteQuestion;
    Button buttonAddOption;
    Button buttonAddExplanation;
    ScrollView answersView;
    LinearLayout answersLayout;

    EditText textQuestion;
    ArrayList<EditText> answers;
    ArrayList<Button> removeButtons;
    ArrayList<Button> correctButtons;
    ArrayList<LinearLayout> layouts;

    boolean newQuiz;
    boolean verified;
    boolean darkmode;
    boolean sound;
    String explanation;

    boolean active = false;

    LocationQuizeo location;
    Quiz quiz;
    Question question;
    User user;
    int correct;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_question);

        darkmode = getIntent().getBooleanExtra("darkmode", false);
        if (darkmode) {
            findViewById(R.id.globeQuestion).setVisibility(View.INVISIBLE);
            findViewById(R.id.globeQuestionDark).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.globeQuestionDark).setVisibility(View.INVISIBLE);
            findViewById(R.id.globeQuestion).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        buttonSaveQuit = (Button) findViewById(R.id.buttonSaveQuit);
        buttonDeleteQuestion = (Button) findViewById(R.id.buttonDeleteQuestion);
        buttonAddOption = (Button) findViewById(R.id.buttonAddOption);
        buttonAddExplanation = (Button) findViewById(R.id.buttonAddExplanation);

        question = getIntent().getParcelableExtra("question");

        textQuestion = (EditText) findViewById(R.id.textQuestion);


        location = getIntent().getParcelableExtra("location");
        verified = getIntent().getBooleanExtra("verified", true);
        sound = getIntent().getBooleanExtra("sound", true);
        quiz = getIntent().getParcelableExtra("quiz");
        user = getIntent().getParcelableExtra("user");
        newQuiz = getIntent().getBooleanExtra("newquiz", false);
        explanation = getIntent().getStringExtra("explanation");
        if (explanation == null) {
            explanation = "";
        }

        answers = new ArrayList<>();
        removeButtons = new ArrayList<>();
        correctButtons = new ArrayList<>();
        layouts = new ArrayList<>();


        correct = -1;
        index = 0;

        if (answersView == null) {
            answersView = (ScrollView) findViewById(R.id.answers_scroll);
            answersLayout = new LinearLayout(this);
            answersLayout.setOrientation(LinearLayout.VERTICAL);
            answersView.addView(answersLayout);
        }

        if (!(question == null)) {
            textQuestion.setText(question.getQuestion());
            String[] answerss = question.getAnswers();
            for (int i = 0; i < answerss.length; i++) {
                addOption();
                answers.get(i).setText(answerss[i]);
            }
//            System.out.println(question.getCorrectInt());
            if (question.getCorrectInt() > -1) {
                correct = question.getCorrectInt();
                correctButtons.get(correct).setBackgroundColor(Color.GREEN);
            }
        } else {
            for (int i = 0; i < 2; i++) {
                addOption();
            }
        }


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
                } else if (answers.size() < 2) {
                    showPopup(v, R.layout.popup_not_enough_answers_added);
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
                    String questionString = textQuestion.getText().toString();
                    if (!questionString.contains("?")) {
                        questionString = questionString + "?";
                    }
                    Question question = new Question(questionString, answerss,
                            correct + 1, explanation, quiz.getNumberOfQuestions() + 1,
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

        buttonAddExplanation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExplanation();
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

// Back button does the same thing as delete question button
    @Override
    public void onBackPressed() {
        createQuiz();
    }

    void createQuiz() {
        active = true;
        Intent intent = new Intent(this, CreateQuizActivity2.class);
        intent.putExtra("user", user);
        intent.putExtra("quiz", quiz);
        intent.putExtra("location", location);
        intent.putExtra("newquiz", newQuiz);
        intent.putExtra("verified", verified);
        intent.putExtra("darkmode", darkmode);
        intent.putExtra("sound", sound);
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
        if (width > height) {
            width = (int) (0.8 * width);
//            System.out.println(width);
        }

        LinearLayout tempLayout = new LinearLayout(this);
        tempLayout.setOrientation(LinearLayout.HORIZONTAL);
        Button tempButton = new Button(this);
        tempButton.setTag(index);
        if (darkmode) {
            tempButton.setBackgroundColor(getResources().getColor(R.color.darkgray));
        } else {
            tempButton.setBackgroundColor(getResources().getColor(R.color.white));
        }
        LinearLayout.LayoutParams lparams1 = new LinearLayout.LayoutParams((int) (0.05*width), (int) (0.05*height));
//        LinearLayout.LayoutParams lparams1 = new LinearLayout.LayoutParams((int) 54, (int) 111);
        lparams1.setMargins(20, 0, 20, 0);
        tempButton.setLayoutParams(lparams1);
        correctButtons.add(tempButton);
        tempLayout.addView(tempButton);




        EditText tempEdit = new EditText(this);
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams((int) (0.75 * width), LinearLayout.LayoutParams.WRAP_CONTENT);
//        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams((int) 810, LinearLayout.LayoutParams.WRAP_CONTENT);
        tempEdit.setGravity(Gravity.CENTER);
        tempEdit.setLayoutParams(lparams);
        if (darkmode) {
            tempEdit.setBackgroundColor(getResources().getColor(R.color.darkgray));
            tempEdit.setTextColor(getResources().getColor(R.color.white2));
        } else {
            tempEdit.setBackgroundColor(getResources().getColor(R.color.white));
            tempEdit.setTextColor(getResources().getColor(R.color.black));
        }
        tempEdit.setHint("Enter answer text");
        tempLayout.addView(tempEdit);
        answers.add(tempEdit);

        Button tempButton1 = new Button(this);
        tempButton1.setTag(index);
        if (darkmode) {
            tempButton1.setBackgroundColor(getResources().getColor(R.color.redd));
        } else {
            tempButton1.setBackgroundColor(getResources().getColor(R.color.brightred));
        }
        LinearLayout.LayoutParams lparams2 = new LinearLayout.LayoutParams((int) (0.05*width), (int) (0.05*height));
//        LinearLayout.LayoutParams lparams2 = new LinearLayout.LayoutParams((int) 54, (int) 111);
        lparams2.setMargins(20, 0, 20, 0);
        tempButton1.setLayoutParams(lparams2);
        removeButtons.add(tempButton1);
        tempLayout.addView(tempButton1);
        layouts.add(tempLayout);
        answersLayout.addView(tempLayout, layoutParams);
        index++;
//        System.out.println("INDEX" + index);

        answersView.getLayoutParams().width = (int) (width*0.9);


        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) v.getTag();
                correct = tag;
                for (Button button : correctButtons) {
                    if (darkmode) {
                        button.setBackgroundColor(getResources().getColor(R.color.darkgray));
                    } else {
                        button.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                }
                Button tempButton = correctButtons.get(tag);
                if (darkmode) {
                    tempButton.setBackgroundColor(getResources().getColor(R.color.darkgreen));
                } else {
                    tempButton.setBackgroundColor(Color.GREEN);
                }
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
                for (int i = removeButtons.size() - 1; i >= tag; i--) {
//                    System.out.println("HENK" + i);
                    Button temp1 = removeButtons.get(i);
                    int tag1 = (int) temp1.getTag();
                    Button temp2 = correctButtons.get(i);
                    temp1.setTag((tag1 - 1));
                    temp2.setTag((tag1 - 1));
                }
                index--;
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

    void addExplanation() {
        Question question1 = new Question();
        String[] answerss = new String[answers.size()];
        int i = 0;
        for (EditText answer : answers) {
            answerss[i] = answer.getText().toString();
            i++;
        }
        if (!(explanation.equals("")) && !(explanation == null)) {
            question1.setExplanation(explanation);
        }
        question1.setAnswers(answerss);
        question1.setQuestion(textQuestion.getText().toString());
        question1.setCorrectInt(correct);
        Intent intent = new Intent(this, PickExplanation.class);
        intent.putExtra("user", user);
        intent.putExtra("quiz", quiz);
        intent.putExtra("location", location);
        intent.putExtra("newquiz", newQuiz);
        intent.putExtra("verified", verified);
        intent.putExtra("darkmode", darkmode);
        intent.putExtra("sound", sound);
        intent.putExtra("question", question1);
        startActivity(intent);
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
