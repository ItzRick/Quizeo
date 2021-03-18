package com.example.quizeo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class AnswerQuizTest {

    AnswerQuiz answerQuiz;

    @Before
    public void setUp() {
        answerQuiz = new AnswerQuiz();
    }

    /** Test the setUserAnswered() and getUserAnswered() methods. */
    @Test
    public void setUserAnswered() {
        System.out.println("setUserAnswered()");
        // Add a new user to this answerQuiz instance:
        User user = new User("user", UUID.randomUUID().toString());
        answerQuiz.setUserAnswered(user);

        // Check if the user was indeed correctly passed:
        Assert.assertEquals(user.getUserId(), answerQuiz.getUserAnswered().getUserId());
        Assert.assertEquals(user.getNickName(), answerQuiz.getUserAnswered().getNickName());
    }

    /** Test the updateScore() and getScore() once with 1 updateScore(). */
    @Test
    public void updateScore0() {
        System.out.println("updateScore() 1");
        // Update the score with an correctly answered question:
        answerQuiz.updateScore(true);
        // Check if the score was correct:
        Assert.assertEquals(1, answerQuiz.getScore());
    }

    /** Test the updateScore() and getScore() once with 4 updateScore(), of which 1 is false. */
    @Test
    public void updateScore1() {
        System.out.println("updateScore() 2");
        // Update the score with 3 correctly answered questions and 1 incorrectly answered question:
        answerQuiz.updateScore(true);
        answerQuiz.updateScore(false);
        answerQuiz.updateScore(true);
        answerQuiz.updateScore(true);
        // Check if the score was correct:
        Assert.assertEquals(3, answerQuiz.getScore());
    }

    /** Test the updateScore() and getScore() once with 1 false updateScore(). */
    @Test
    public void updateScore2() {
        System.out.println("updateScore() 3");
        // Update the score with an incorrectly answered question:
        answerQuiz.updateScore(false);
        // Check if the score was correct:
        Assert.assertEquals(0, answerQuiz.getScore());
    }

    /** Test if the initialization works while passing an user. */
    @Test
    public void initialization() {
        System.out.println("Initialization()");
        // Create a new User:
        User user = new User("user", UUID.randomUUID().toString());
        AnswerQuiz answerQuiz1 = new AnswerQuiz(user);
        // See if the initialization has been done correctly:
        Assert.assertEquals(user.getUserId(), answerQuiz1.getUserAnswered().getUserId());
        Assert.assertEquals(user.getNickName(), answerQuiz1.getUserAnswered().getNickName());
    }
}