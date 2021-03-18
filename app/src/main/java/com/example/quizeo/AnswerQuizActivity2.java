package com.example.quizeo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class AnswerQuizActivity2 extends AppCompatActivity {

    Quiz quiz;
    Question currentQuestion;
    AnswerQuiz answerQuiz;
    User userAnswered;

    Button buttonAnswer1;
    Button buttonAnswer2;
    Button next;

    TextView currentQuestionNumber;
    TextView numberCorrect;
    TextView currentQuestionText;

    boolean isAnswered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_answer_quiz_2);
        quiz = getIntent().getParcelableExtra("quiz");
        answerQuiz = getIntent().getParcelableExtra("answerQuiz");
        userAnswered = getIntent().getParcelableExtra("user");
        if (answerQuiz == null) {
            answerQuiz = new AnswerQuiz();
        }

        currentQuestion = quiz.getCurrent();

        buttonAnswer1 = (Button) findViewById(R.id.answer_2_1);
        buttonAnswer2 = (Button) findViewById(R.id.answer_2_2);
        next = (Button) findViewById(R.id.next_question_2);


        currentQuestionNumber = (TextView) findViewById(R.id.current_question_2);
        numberCorrect = (TextView) findViewById(R.id.score_2);
        currentQuestionText = (TextView) findViewById(R.id.question_2);

        String current = currentQuestion.getId() + " / " + quiz.getNumberOfQuestions();
        currentQuestionText.setText(current);

        String score = answerQuiz.getScore() + " / " + quiz.getNumberOfQuestions();
        numberCorrect.setText(score);

        currentQuestionText.setText(currentQuestion.getQuestion());

        String[] answers = currentQuestion.getAnswers();
        buttonAnswer1.setText(answers[0]);
        buttonAnswer2.setText(answers[1]);

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
                    Context context = getApplicationContext();
                    CharSequence text = "The question is already answered!";
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(context, text, duration).show();
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
                    Context context = getApplicationContext();
                    CharSequence text = "The question is already answered!";
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(context, text, duration).show();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAnswered){
                    if (quiz.getNext().getNumberOfAnswers() == 2) {
                        answerNext2();
                    } else if (quiz.getNext().getNumberOfAnswers() == 3) {
                        answerNext3();
                    } else if (quiz.getNext().getNumberOfAnswers() == 4) {
                        answerNext4();
                    } else if (quiz.getNext().getNumberOfAnswers() == 5) {
                        answerNext5();
                    }
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Answer the question first!";
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(context, text, duration).show();
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
}
