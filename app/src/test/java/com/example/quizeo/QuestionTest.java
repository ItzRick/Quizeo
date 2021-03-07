package com.example.quizeo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QuestionTest {

    private Question question;

    @Before
    public void setUp() {
        question = new Question();
    }

    @Test
    public void setQuestion() {
        System.out.println("setQuestion()");
        String string = "this is a question";
        question.setQuestion(string);
        Assert.assertEquals(question.getQuestion(), string);
    }

    @Test
    public void setAnswers() {
        System.out.println("setAnswers()");
        String[] array = new String[]{
          "first", "two", "three", "four", "five"
        };
        question.setAnswers(array);
        String[] array1 = question.getAnswers();
        for (int i = 0; i < array1.length; i++) {
            Assert.assertEquals(array[i], array1[i]);
        }
    }

    @Test
    public void setCorrect0() {
        System.out.println("setCorrect() 1");
        String correct = "first";
        String[] array = new String[]{
                "first", "two", "three", "four", "five"
        };
        question.setAnswers(array);
        question.setCorrect(correct);
        Assert.assertTrue(question.getCorrect(correct));
        Assert.assertTrue(question.getCorrect(1));
    }

    @Test
    public void setCorrect1() {
        System.out.println("setCorrect() 2");
        String correct = "first";
        String[] array = new String[]{
                "first", "two", "three", "four", "five"
        };
        question.setAnswers(array);
        question.setCorrect(1);
        Assert.assertTrue(question.getCorrect(correct));
        Assert.assertTrue(question.getCorrect(1));
    }

    @Test
    public void setExplanation() {
        System.out.println("setExplanation()");
        String explanation = "this is an explanation!";
        question.setExplanation(explanation);
        Assert.assertEquals(explanation, question.getExplanation());
    }

    @Test
    public void setId() {
        System.out.println("setId()");
        int id = 1;
        question.setId(id);
        Assert.assertEquals(id, question.getId());
    }

    @Test
    public void setGlobalId() {
        System.out.println("setGlobalId()");
        int globalId = 123;
        question.setGlobalId(globalId);
        Assert.assertEquals(globalId, question.getGlobalId());
    }

    @Test
    public void initialization() {
        System.out.println("initialization()");
        String string = "this is a question";
        String[] array = new String[]{
                "first", "two", "three", "four", "five"
        };
        String correct = "first";
        String explanation = "this is an explanation!";
        int id = 1;
        int globalId = 123;
        Question question1 = new Question(string, array, correct, explanation, id, globalId);
        Assert.assertEquals(question1.getQuestion(), string);
        String[] array1 = question1.getAnswers();
        for (int i = 0; i < array1.length; i++) {
            Assert.assertEquals(array[i], array1[i]);
        }
        Assert.assertTrue(question1.getCorrect(correct));
        Assert.assertTrue(question1.getCorrect(1));
        Assert.assertEquals(id, question1.getId());
        Assert.assertEquals(globalId, question1.getGlobalId());
    }

    @Test
    public void initialization1() {
        System.out.println("initialization() 1");
        String string = "this is a question";
        String[] array = new String[]{
                "first", "two", "three", "four", "five"
        };
        String correct = "first";
        String explanation = "this is an explanation!";
        int id = 1;
        int globalId = 123;
        Question question1 = new Question(string, array, 1, explanation, id, globalId);
        Assert.assertEquals(question1.getQuestion(), string);
        String[] array1 = question1.getAnswers();
        for (int i = 0; i < array1.length; i++) {
            Assert.assertEquals(array[i], array1[i]);
        }
        Assert.assertTrue(question1.getCorrect(correct));
        Assert.assertTrue(question1.getCorrect(1));
        Assert.assertEquals(id, question1.getId());
        Assert.assertEquals(globalId, question1.getGlobalId());
    }
}