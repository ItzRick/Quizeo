package com.example.quizeo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.UUID;

public class AnswerQuizActivity extends AppCompatActivity {

    // Local variables for thw quiz and user:
    Quiz quiz;
    Question currentQuestion;
    AnswerQuiz answerQuiz;
    User userAnswered;
    LocationQuizeo location;

    // User interface elements:
    Button[] buttons;
    Button next;
    Button quit;
    TextView currentQuestionNumber;
    TextView numberCorrect;
    TextView currentQuestionText;
    ScrollView answersView;

    // Local variable:
    boolean isAnswered;
    boolean verified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the correct layout:
        setContentView(R.layout.fragment_answer_quiz);
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Retrieve all passed objects:
        verified = getIntent().getBooleanExtra("verified", true);
        location = getIntent().getParcelableExtra("location");
        quiz = getIntent().getParcelableExtra("quiz");
        answerQuiz = getIntent().getParcelableExtra("answerQuiz");
        userAnswered = getIntent().getParcelableExtra("user");

        // If there was not yet an answerQuiz object created (and passed) create one:
        if (answerQuiz == null) {
            answerQuiz = new AnswerQuiz();
        }

        // Retrieve the currentQuestion:
        currentQuestion = quiz.getNext();

        // Retrieve the next and quit buttons:
        next = (Button) findViewById(R.id.next_question_answer);

        quit = (Button) findViewById(R.id.quit_answer);

        // If there is no next question, set the nextquestion button text to "End quiz!":
        if (!quiz.nextQuestionExists()) {
            next.setText("End quiz!");
        }

        // Retrieve the question text, the question number and the number of correct questions:
        currentQuestionNumber = (TextView) findViewById(R.id.current_question_answer);
        numberCorrect = (TextView) findViewById(R.id.score_answer);
        currentQuestionText = (TextView) findViewById(R.id.question_answer);

        // Set the current question number:
        String current = currentQuestion.getId() + " / " + quiz.getNumberOfQuestions();
        currentQuestionNumber.setText(current);

        // Set the number of correctly answered questions:
        String score = answerQuiz.getScore() + " / " + (currentQuestion.getId() - 1);
        numberCorrect.setText(score);

        // Set the current question text:
        currentQuestionText.setText(currentQuestion.getQuestion());

        // Retrieve the scrollview for the ansers:
        answersView = (ScrollView) findViewById(R.id.answer_quizzes_scroll);

        // Create a new linearLayout for the scrollview and set the orientation to vertical:
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        // Initialize the array for the buttons:
        buttons = new Button[currentQuestion.getNumberOfAnswers()];
        // Retrieve the possible answers:
        String[] answers = currentQuestion.getAnswers();
        // Add the different answers to different buttons and add this to the scrollview:
        for (int i = 0; i < currentQuestion.getNumberOfAnswers(); i++) {
            // Create a new button and set the text, which is the specific answer:
            buttons[i] = new Button(this);
            String string = answers[i];
            buttons[i].setText(string);
            buttons[i].setTag(i);
            // Add the button to the LinearLayout:
            linearLayout.addView(buttons[i]);
            // Create a clicklistener:
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int) v.getTag();
                    // If the answer has not yet been answered:
                    if (!isAnswered) {
                        // If the answer is correct, set the color of this button to green and
                        // update the score:
                        if (currentQuestion.getCorrect(tag + 1)) {
                            buttons[tag].setBackgroundColor(Color.GREEN);
                            answerQuiz.updateScore(true);
                        // If the answer is incorrect, set the color of this button to red,
                        // set the correct question to green and update the score:
                        } else {
                            buttons[tag].setBackgroundColor(Color.RED);
                            int correct = currentQuestion.getCorrectInt();
                            buttons[correct - 1].setBackgroundColor(Color.GREEN);
                            answerQuiz.updateScore(false);
                        }
                        // The question has been answered:
                        isAnswered = true;
                    } else {
                        // Create a pop up that the question has already been answered:
                        // inflate the layout of the popup window
                        LayoutInflater inflater = (LayoutInflater)
                                v.getContext().getSystemService
                                        (v.getContext().LAYOUT_INFLATER_SERVICE);
                        View popupView =
                                inflater.inflate(R.layout.popup_already_answered, null);

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
            });

        }
        // Add the LinearLayout to the scrollview:
        answersView.addView(linearLayout);

        // Adds an actionlistener to be able to quit the quiz:
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quit();
            }
        });


        // Adds an actonlistener for the next question:
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If the question is answered, go to the next question, or finish the quiz if
                // there is no next question:
                if (isAnswered) {
                    if (! quiz.nextQuestionExists()) {
                        finishQuiz();
                    } else {
                        answerNext();
                    }
                } else {
                    // Create a pop up that the question should first be answered:
                    // inflate the layout of the popup window
                    LayoutInflater inflater = (LayoutInflater)
                            v.getContext().getSystemService
                                    (v.getContext().LAYOUT_INFLATER_SERVICE);
                    View container =
                            inflater.inflate(R.layout.popup_not_answered, null);

                    // create the popup window
                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    // Sets that you can press outside the popup to close it:
                    boolean focusable = true;
                    PopupWindow popupWindow = new PopupWindow(container, width, height, focusable);

                    // show the popup window
                    // which view you pass in doesn't matter, it is only used for the window token
                    popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                    // dismiss the popup window when touched
                    container.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });
                }
            }
        });
    }


    /**
     * Go to the next question and push all required objects.
     */
    public void answerNext() {
        Intent intent = new Intent(this, AnswerQuizActivity.class);
        intent.putExtra("verified", verified);
        intent.putExtra("location", location);
        intent.putExtra("quiz", quiz);
        intent.putExtra("answerQuiz", answerQuiz);
        intent.putExtra("user", userAnswered);
        startActivity(intent);
    }

    /**
     * Quit the quiz and push all required objects:
     */
    public void quit() {
        quiz.quitQuiz();
        Intent intent = new Intent(this, PlayQuizActivity.class);
        intent.putExtra("verified", verified);
        intent.putExtra("location", location);
        intent.putExtra("user", userAnswered);
        startActivity(intent);
    }

    /**
     * Method for transition to the finished quiz screen.
     */
    public void finishQuiz () {
        Intent intent = new Intent(this, FinishQuizActivity.class);
        intent.putExtra("verified", verified);
        intent.putExtra("location", location);
        intent.putExtra("answerQuiz", answerQuiz);
        intent.putExtra("quiz", quiz);
        intent.putExtra("user", userAnswered);
        startActivity(intent);
    }
}
