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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.UUID;

public class AnswerQuizActivity5 extends AppCompatActivity {

    Quiz quiz;
    Question currentQuestion;
    AnswerQuiz answerQuiz;
    User userAnswered;

    Button buttonAnswer1;
    Button buttonAnswer2;
    Button buttonAnswer3;
    Button buttonAnswer4;
    Button buttonAnswer5;
    Button next;
    Button quit;

    TextView currentQuestionNumber;
    TextView numberCorrect;
    TextView currentQuestionText;

    LocationQuizeo location;

    boolean isAnswered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_answer_quiz_5);
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

        currentQuestion = quiz.getCurrent();

        buttonAnswer1 = (Button) findViewById(R.id.answer_5_1);
        buttonAnswer2 = (Button) findViewById(R.id.answer_5_2);
        buttonAnswer3 = (Button) findViewById(R.id.answer_5_3);
        buttonAnswer4 = (Button) findViewById(R.id.answer_5_4);
        buttonAnswer5 = (Button) findViewById(R.id.answer_5_5);

        next = (Button) findViewById(R.id.next_question_5);

        quit = (Button) findViewById(R.id.quit_5);

        if (!quiz.nextQuestionExists()) {
            next.setText("End quiz!");
        }

        currentQuestionNumber = (TextView) findViewById(R.id.current_question_5);
        numberCorrect = (TextView) findViewById(R.id.score_5);
        currentQuestionText = (TextView) findViewById(R.id.question_5);

        String current = currentQuestion.getId() + " / " + quiz.getNumberOfQuestions();
        currentQuestionNumber.setText(current);

        String score = answerQuiz.getScore() + " / " + (currentQuestion.getId() - 1);

        numberCorrect.setText(score);

        currentQuestionText.setText(currentQuestion.getQuestion());

        String[] answers = currentQuestion.getAnswers();
        buttonAnswer1.setText(answers[0]);
        buttonAnswer2.setText(answers[1]);
        buttonAnswer3.setText(answers[2]);
        buttonAnswer4.setText(answers[3]);
        buttonAnswer5.setText(answers[4]);

        buttonAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAnswered) {
                    if (currentQuestion.getCorrect(1)) {
                        buttonAnswer1.setBackgroundColor(Color.GREEN);
                        answerQuiz.updateScore(true);
                    } else {
                        buttonAnswer1.setBackgroundColor(Color.RED);
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

        buttonAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAnswered) {
                    if (currentQuestion.getCorrect(2)) {
                        buttonAnswer2.setBackgroundColor(Color.GREEN);
                        answerQuiz.updateScore(true);
                    } else {
                        buttonAnswer2.setBackgroundColor(Color.RED);
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

        buttonAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAnswered) {
                    if (currentQuestion.getCorrect(3)) {
                        buttonAnswer3.setBackgroundColor(Color.GREEN);
                        answerQuiz.updateScore(true);
                    } else {
                        buttonAnswer3.setBackgroundColor(Color.RED);
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

        buttonAnswer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAnswered) {
                    if (currentQuestion.getCorrect(4)) {
                        buttonAnswer4.setBackgroundColor(Color.GREEN);
                        answerQuiz.updateScore(true);
                    } else {
                        buttonAnswer4.setBackgroundColor(Color.RED);
                        answerQuiz.updateScore(false);
                    }
                    isAnswered = true;
                } else {
                    // inflate the layout of the popup window
                    LayoutInflater inflater = (LayoutInflater)
                            getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    ViewGroup container = (ViewGroup) inflater.inflate(R.layout.popup_already_answered, null);

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

        buttonAnswer5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAnswered) {
                    if (currentQuestion.getCorrect(5)) {
                        buttonAnswer5.setBackgroundColor(Color.GREEN);
                        answerQuiz.updateScore(true);
                    } else {
                        buttonAnswer5.setBackgroundColor(Color.RED);
                        answerQuiz.updateScore(false);
                    }
                    isAnswered = true;
                } else {
                    // inflate the layout of the popup window
                    LayoutInflater inflater = (LayoutInflater)
                            getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    ViewGroup container = (ViewGroup) inflater.inflate(R.layout.popup_already_answered, null);

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

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAnswered) {
                    if (! quiz.nextQuestionExists()) {
                        finishQuiz();
                    } else if (quiz.getNext().getNumberOfAnswers() == 2) {
                        answerNext2();
                    } else if (quiz.getNext().getNumberOfAnswers() == 3) {
                        answerNext3();
                    } else if (quiz.getNext().getNumberOfAnswers() == 4) {
                        answerNext4();
                    } else if (quiz.getNext().getNumberOfAnswers() == 5) {
                        answerNext5();
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

    public void answerNext2() {
        Intent intent = new Intent(this, AnswerQuizActivity2.class);
        intent.putExtra("quiz", quiz);
        intent.putExtra("answerQuiz", answerQuiz);
        intent.putExtra("user", userAnswered);
        startActivity(intent);
    }

    public void answerNext3() {
        Intent intent = new Intent(this, AnswerQuizActivity3.class);
        intent.putExtra("quiz", quiz);
        intent.putExtra("answerQuiz", answerQuiz);
        intent.putExtra("user", userAnswered);
        startActivity(intent);
    }

    public void answerNext4() {
        Intent intent = new Intent(this, AnswerQuizActivity4.class);
        intent.putExtra("quiz", quiz);
        intent.putExtra("answerQuiz", answerQuiz);
        intent.putExtra("user", userAnswered);
        startActivity(intent);
    }

    public void answerNext5() {
        Intent intent = new Intent(this, AnswerQuizActivity5.class);
        intent.putExtra("quiz", quiz);
        intent.putExtra("answerQuiz", answerQuiz);
        intent.putExtra("user", userAnswered);
        startActivity(intent);
    }

    /**
     * Method for transition to the finished quiz screen.
     */
    public void finishQuiz () {
        Intent intent = new Intent(this, FinishQuizActivity.class);
        intent.putExtra("answerQuiz", answerQuiz);
        intent.putExtra("quiz", quiz);
        intent.putExtra("user", userAnswered);
        startActivity(intent);
    }
}
