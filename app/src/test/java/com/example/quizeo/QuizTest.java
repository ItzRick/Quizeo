package com.example.quizeo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QuizTest {

    Quiz quiz;

    Question question;

    @Before
    public void setUp() {
        quiz = new Quiz();
        String string = "this is a question";
        String[] array = new String[]{
                "first", "two", "three", "four", "five"
        };
        String correct = "first";
        String explanation = "this is an explanation!";
        int id = 1;
        int globalId = 123;
        question = new Question(string, array, correct, explanation, id, globalId);
        quiz.addQuestion(question);
    }

    @Test
    public void addQuestion() {
        System.out.println("addQuestion()");
        quiz.addQuestion(question);
        Question question1 = quiz.getNext();
        Question question2 = quiz.getNext();
        Assert.assertNotNull(question1);
        Assert.assertNotNull(question2);
    }

    @Test
    public void getQuestions() {
        System.out.println("getQuestions()");
        quiz.addQuestion(question);
        Question[] questions = quiz.getQuestions();
        for (Question question1 : questions) {
            Assert.assertNotNull(question1);
        }
    }

    @Test
    public void getFirst() {
        System.out.println("getFirst()");
        Assert.assertNotNull(quiz.getFirst());
    }

    @Test
    public void getNext() {
        System.out.println("getNext()");
        Question question1 = new Question();
        question1.setQuestion("second");
        quiz.addQuestion(question1);
        Question question2 = quiz.getNext();
        Question question3 = quiz.getNext();
        Assert.assertNotNull(question2);
        Assert.assertNotNull(question3);
        Assert.assertNotEquals(question2.getQuestion(), question3.getQuestion());
    }

    @Test
    public void getQuizName() {
        System.out.println("getQuizName()");
        String quizName = "This is a quiz";
        quiz.setQuizName(quizName);
        Assert.assertEquals(quizName, quiz.getQuizName());
    }

    @Test
    public void quitQuiz() {
        System.out.println("quitQuiz()");
        Question question1 = quiz.getNext();
        quiz.quitQuiz();
        Question question2 = quiz.getNext();
        Assert.assertEquals(question1.getQuestion(), question2.getQuestion());
    }

    @Test
    public void getQuizId() {
        System.out.println("getQuizId()");
        int id = 123;
        quiz.setQuizId(id);
        Assert.assertEquals(id, quiz.getQuizId());
    }

    @Test
    public void getNumberOfQuestions() {
        System.out.println("getNumberOfQuestions() 1");
        Assert.assertEquals(1, quiz.getNumberOfQuestions());
    }

    @Test
    public void getNumberOfQuestions1() {
        System.out.println("getNumberOfQuestions() 2");
        quiz.addQuestion(question);
        Assert.assertEquals(2, quiz.getNumberOfQuestions());
    }

    @Test
    public void getNumberOfQuestions2() {
        System.out.println("getNumberOfQuestions() 3");
        quiz.addQuestion(question);
        quiz.addQuestion(question);
        Assert.assertEquals(3, quiz.getNumberOfQuestions());
    }

    @Test
    public void getLocation() {
        System.out.println("getLocation()");
        Location location = new Location(1, 1);
        quiz.setLocation(location);
        Location location1 = quiz.getLocation();
        Assert.assertEquals((int)location.getX(), (int)location1.getX());
        Assert.assertEquals((int)location.getY(), (int)location1.getY());
        Assert.assertEquals(location, location1);
    }

    @Test
    public void getRating() {
        System.out.println("getRating() 1");
        double rating = quiz.getRating();
        Assert.assertEquals(-1, (int)rating);
    }

    @Test
    public void getRating1() {
        System.out.println("getRating() 2");
        quiz.setAdditionalRating(10);
        double rating = quiz.getRating();
        Assert.assertEquals(10, (int)rating);
    }

    @Test
    public void getRating2() {
        System.out.println("getRating() 3");
        quiz.setAdditionalRating(10);
        quiz.setAdditionalRating(0);
        double rating = quiz.getRating();
        Assert.assertEquals(5, (int)rating);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getRating3() {
        System.out.println("getRating() exception 1");
        quiz.setAdditionalRating(-0.1);
        double rating = quiz.getRating();
        Assert.assertEquals(5, (int)rating);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getRating4() {
        System.out.println("getRating() exception 2");
        quiz.setAdditionalRating(10.1);
        double rating = quiz.getRating();
        Assert.assertEquals(5, (int)rating);
    }

    @Test
    public void getScoreToPass() {
        System.out.println("getScoreToPass() 1");
        Assert.assertEquals(1, quiz.getScoreToPass());
    }

    @Test
    public void getScoreToPass1() {
        System.out.println("getScoreToPass() 2");
        quiz.addQuestion(question);
        quiz.addQuestion(question);
        quiz.addQuestion(question);
        Assert.assertEquals(3, quiz.getScoreToPass());
    }

    @Test
    public void setPercentageToPass() {
        System.out.println("setPercentageToPass() 1");
        quiz.setPercentageToPass(50);
        quiz.addQuestion(question);
        quiz.addQuestion(question);
        quiz.addQuestion(question);
        Assert.assertEquals(2, quiz.getScoreToPass());
    }

    @Test
    public void setPercentageToPass1() {
        System.out.println("setPercentageToPass() 2");
        quiz.setPercentageToPass(100);
        quiz.addQuestion(question);
        quiz.addQuestion(question);
        quiz.addQuestion(question);
        Assert.assertEquals(4, quiz.getScoreToPass());
    }
}