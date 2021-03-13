package com.example.quizeo;

public class AnswerQuiz {

    /** Variable to keep track of the score: */
    private int score;

    /** User who answered this quiz: */
    private User userAnswered;

    /**
     * Initialize the score to 0.
     */
    public AnswerQuiz() {
        score = 0;
    }

    /**
     * Initialization with an user added, set the score to 0.
     *
     * @param userAnswered user who answers this quiz and should therefore be added to the
     *                     userAnswered variable.
     */
    public AnswerQuiz(User userAnswered) {
        // Initialize the score:
        score = 0;
        // Set the correct user who answered this quiz:
        this.userAnswered = userAnswered;
    }

    /**
     * Set the user who answered this quiz.
     *
     * @param userAnswered user who answered this quiz.
     */
    public void setUserAnswered(User userAnswered) {
        this.userAnswered = userAnswered;
    }

    /**
     * Update the score depending on whether the answer was correct.
     *
     * @param correct Boolean value to indicate whether the question was correctly answered.
     */
    public void updateScore(boolean correct) {
        if (correct) {
            score++;
        }
    }

    /**
     * Retrieve the user who answered this quiz.
     *
     * @return user who answered this quiz.
     */
    public User getUserAnswered() {
        return userAnswered;
    }

    /**
     * Retrieve the score associated with this answering of the quiz.
     *
     * @return score of this quiz.
     */
    public int getScore() {
        return score;
    }
}
