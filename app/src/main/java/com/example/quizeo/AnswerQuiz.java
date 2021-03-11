package com.example.quizeo;

public class AnswerQuiz {

    private int score;

    private User userAnswered;

    public AnswerQuiz() {
        score = 0;
    }

    public AnswerQuiz(User userAnswered) {
        score = 0;
        this.userAnswered = userAnswered;
    }

    public void setUserAnswered(User userAnswered) {
        this.userAnswered = userAnswered;
    }

    public void updateScore(boolean correct) {
        if (correct) {
            score++;
        }
    }

    public User getUserAnswered() {
        return userAnswered;
    }

    public int getScore() {
        return score;
    }
}
