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

    Quiz quiz;
    Question currentQuestion;
    AnswerQuiz answerQuiz;
    User userAnswered;

    Button[] buttons;
    Button next;
    Button quit;

    TextView currentQuestionNumber;
    TextView numberCorrect;
    TextView currentQuestionText;

    LocationQuizeo location;

    ScrollView answersView;

    boolean isAnswered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_answer_quiz);
    }
    @Override
    protected void onStart() {
        super.onStart();
        location = getIntent().getParcelableExtra("location");
        quiz = getIntent().getParcelableExtra("quiz");
        answerQuiz = getIntent().getParcelableExtra("answerQuiz");
        userAnswered = getIntent().getParcelableExtra("user");
        if (answerQuiz == null) {
            answerQuiz = new AnswerQuiz();
        }

        currentQuestion = quiz.getNext();

        next = (Button) findViewById(R.id.next_question_answer);

        quit = (Button) findViewById(R.id.quit_answer);

        if (!quiz.nextQuestionExists()) {
            next.setText("End quiz!");
        }

        currentQuestionNumber = (TextView) findViewById(R.id.current_question_answer);
        numberCorrect = (TextView) findViewById(R.id.score_answer);
        currentQuestionText = (TextView) findViewById(R.id.question_answer);

        answersView = (ScrollView) findViewById(R.id.answer_quizzes_scroll);

        String current = currentQuestion.getId() + " / " + quiz.getNumberOfQuestions();
        currentQuestionNumber.setText(current);

        String score = answerQuiz.getScore() + " / " + (currentQuestion.getId() - 1);

        numberCorrect.setText(score);

        currentQuestionText.setText(currentQuestion.getQuestion());

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        buttons = new Button[currentQuestion.getNumberOfAnswers()];
        String[] answers = currentQuestion.getAnswers();
        for (int i = 0; i < currentQuestion.getNumberOfAnswers(); i++) {
            buttons[i] = new Button(this);
            String string = answers[i];
            buttons[i].setText(string);
            buttons[i].setTag(i);
            linearLayout.addView(buttons[i]);
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int) v.getTag();
                    if (!isAnswered) {
                        if (currentQuestion.getCorrect(tag + 1)) {
                            buttons[tag].setBackgroundColor(Color.GREEN);
                            answerQuiz.updateScore(true);
                        } else {
                            buttons[tag].setBackgroundColor(Color.RED);
                            int correct = currentQuestion.getCorrectInt();
                            buttons[correct - 1].setBackgroundColor(Color.GREEN);
                            answerQuiz.updateScore(false);
                        }
                        isAnswered = true;
                    } else {
                        // inflate the layout of the popup window
                        LayoutInflater inflater = (LayoutInflater)
                                v.getContext().getSystemService(v.getContext().LAYOUT_INFLATER_SERVICE);
                        //getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        View popupView = inflater.inflate(R.layout.popup_already_answered, null);

                        // create the popup window
                        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                        boolean focusable = true; // lets taps outside the popup also dismiss it
                        PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                        // show the popup window
                        // which view you pass in doesn't matter, it is only used for the window token
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
        answersView.addView(linearLayout);

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quit();
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAnswered) {
                    if (! quiz.nextQuestionExists()) {
                        finishQuiz();
                    } else {
                        answerNext();
                    }
                } else {
                    // inflate the layout of the popup window
                    LayoutInflater inflater = (LayoutInflater)
                            getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    ViewGroup container = (ViewGroup) inflater.inflate(R.layout.popup_not_answered, null);

                    // create the popup window
                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    PopupWindow popupWindow = new PopupWindow(container, width, height, focusable);

                    // show the popup window
                    // which view you pass in doesn't matter, it is only used for the window token
                    popupWindow.showAtLocation(container, Gravity.CENTER, 0, 0);

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




    public void answerNext() {
        Intent intent = new Intent(this, AnswerQuizActivity.class);
        intent.putExtra("location", location);
        intent.putExtra("quiz", quiz);
        intent.putExtra("answerQuiz", answerQuiz);
        intent.putExtra("user", userAnswered);
        startActivity(intent);
    }

    public void quit() {
        quiz.quitQuiz();
        Intent intent = new Intent(this, PlayQuizActivity.class);
        intent.putExtra("location", location);
        intent.putExtra("user", userAnswered);
        startActivity(intent);
    }

    /**
     * Method for transition to the finished quiz screen.
     */
    public void finishQuiz () {
        Intent intent = new Intent(this, FinishQuizActivity.class);
        intent.putExtra("location", location);
        intent.putExtra("answerQuiz", answerQuiz);
        intent.putExtra("quiz", quiz);
        intent.putExtra("user", userAnswered);
        startActivity(intent);
    }
}
