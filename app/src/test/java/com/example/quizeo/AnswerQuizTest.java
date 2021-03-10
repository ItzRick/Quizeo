package com.example.quizeo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnswerQuizTest {

    AnswerQuiz answerQuiz;

    @Before
    public void setUp() {
        answerQuiz = new AnswerQuiz();
    }

    @Test
    public void setUserAnswered() {
        System.out.println("setUserAnswered()");
        User user = new User("user", 12356);
        answerQuiz.setUserAnswered(user);
        Assert.assertEquals(user.getUserId(), answerQuiz.getUserAnswered().getUserId());
        Assert.assertEquals(user.getNickName(), answerQuiz.getUserAnswered().getNickName());
    }

    @Test
    public void updateScore0() {
        System.out.println("updateScore() 1");
        answerQuiz.updateScore(true);
        Assert.assertEquals(1, answerQuiz.getScore());
    }

    @Test
    public void updateScore1() {
        System.out.println("updateScore() 2");
        answerQuiz.updateScore(true);
        answerQuiz.updateScore(false);
        answerQuiz.updateScore(true);
        answerQuiz.updateScore(true);
        Assert.assertEquals(3, answerQuiz.getScore());
    }

    @Test
    public void updateScore2() {
        System.out.println("updateScore() 3");
        answerQuiz.updateScore(false);
        Assert.assertEquals(0, answerQuiz.getScore());
    }

    @Test
    public void initialization() {
        System.out.println("Initialization()");
        User user = new User("user", 12356);
        AnswerQuiz answerQuiz1 = new AnswerQuiz(user);
        Assert.assertEquals(user.getUserId(), answerQuiz1.getUserAnswered().getUserId());
        Assert.assertEquals(user.getNickName(), answerQuiz1.getUserAnswered().getNickName());
    }
}