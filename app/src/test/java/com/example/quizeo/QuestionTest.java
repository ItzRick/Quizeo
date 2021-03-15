package com.example.quizeo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class QuestionTest {

    private Question question;

    @Before
    public void setUp() {
        // Initialize a new question:
        question = new Question();
    }

    /** Test of the setQuestion() and getQuestion() methods. */
    @Test
    public void setQuestion() {
        System.out.println("setQuestion()");

        // Create a String for the question and set this String:
        String string = "this is a question";
        question.setQuestion(string);

        // Check if the String is indeed correctly saved.
        Assert.assertEquals(question.getQuestion(), string);
    }

    /** test of the setAnswers() and getAnswers() method. */
    @Test
    public void setAnswers() {
        System.out.println("setAnswers()");

        // Create an array with the answers and set these answers in the question:
        String[] array = new String[]{
          "first", "two", "three", "four", "five"
        };
        question.setAnswers(array);

        // Retrieve the answers with getAnswers():
        String[] array1 = question.getAnswers();

        // Check if these answers are indeed the same:
        for (int i = 0; i < array1.length; i++) {
            Assert.assertEquals(array[i], array1[i]);
        }
    }

    /** Test of the setCorrect(String) and getCorrect() methods. */
    @Test
    public void setCorrect0() {
        System.out.println("setCorrect() 1");
        // Set the correct answer (as String) and an array with all answers and
        // add these to the question:
        String correct = "first";
        String[] array = new String[]{
                "first", "two", "three", "four", "five"
        };
        question.setAnswers(array);
        question.setCorrect(correct);

        // Check that this Answer is indeed correctly saved and checked:
        Assert.assertTrue(question.getCorrect(correct));
        Assert.assertTrue(question.getCorrect(1));
    }

    /** Test of the setCorrect(Int) and getCorrect() methods. */
    @Test
    public void setCorrect1() {
        System.out.println("setCorrect() 2");
        // Set the correct answer (as Int) and an array with all answers
        // and add these to the question:
        String correct = "first";
        String[] array = new String[]{
                "first", "two", "three", "four", "five"
        };
        question.setAnswers(array);
        question.setCorrect(1);

        // Check that the answer is indeed correctly saved and checked:
        Assert.assertTrue(question.getCorrect(correct));
        Assert.assertTrue(question.getCorrect(1));
    }

    /** Test of the setCorrect(String) method and getCorrect String methods. */
    @Test
    public void setCorrect2() {
        System.out.println("setCorrect() 3");
        // Set the correct answer (as String) and an array with all answers
        // and add these to the question:
        String correct = "first";
        String[] array = new String[]{
                "first", "two", "three", "four", "five"
        };
        question.setAnswers(array);
        question.setCorrect(correct);

        // Check if the getCorrect() method indeed returns this correct String:
        Assert.assertEquals(correct, question.getCorrect());
    }

    /** Test the setExplanation() and getExplanation() methods. */
    @Test
    public void setExplanation() {
        System.out.println("setExplanation()");
        // Save and ad an explanation:
        String explanation = "this is an explanation!";
        question.setExplanation(explanation);

        // Check if we get the same explanation returned:
        Assert.assertEquals(explanation, question.getExplanation());
    }

    /** Test setId() and getId(). */
    @Test
    public void setId() {
        System.out.println("setId()");

        // Create and set an id:
        int id = 1;
        question.setId(id);

        // Check if we get the same Id returned:
        Assert.assertEquals(id, question.getId());
    }

    /** Test setGlobalId() and getGlobalId(). */
    @Test
    public void setGlobalId() {
        System.out.println("setGlobalId()");

        // Set a globalId and save this with the question:
        UUID globalId = UUID.randomUUID();
        question.setGlobalId(globalId);

        // Check if this globalId indeed is returned:
        Assert.assertEquals(globalId, question.getGlobalId());
    }

    /** Test the above features by means of an initialization. */
    @Test
    public void initialization() {
        System.out.println("initialization()");

        // Create the question String, answer array, correct question String, explanation String,
        // id and globalId and use the initialization to pass this to the question:
        String string = "this is a question";
        String[] array = new String[]{
                "first", "two", "three", "four", "five"
        };
        String correct = "first";
        String explanation = "this is an explanation!";
        int id = 1;
        UUID globalId = UUID.randomUUID();
        Question question1 = new Question(string, array, correct, explanation, id, globalId);

        // Check if all values where correctly passed:
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

    /** Test the above features by means of an initialization. */
    @Test
    public void initialization1() {
        System.out.println("initialization() 1");

        // Create the question String, answer array, correct question index, explanation String,
        // id and globalId and use the initialization to pass this to the question:
        String string = "this is a question";
        String[] array = new String[]{
                "first", "two", "three", "four", "five"
        };
        String correct = "first";
        String explanation = "this is an explanation!";
        int id = 1;
        UUID globalId = UUID.randomUUID();
        Question question1 = new Question(string, array, 1, explanation, id, globalId);

        // Check if all values where correctly passed:
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

    /** Test for the setUserCreated() and getUserCreated() method. */
    @Test
    public void setUserCreated() {
        System.out.println("setUserCreated()");

        // Create a new user and set this user for this question:
        User user = new User("user", UUID.randomUUID());
        question.setUserCreated(user);

        // Check if the user was correctly saved to this question:
        Assert.assertEquals(user.getNickName(), question.getUserCreated().getNickName());
        Assert.assertEquals(user.getUserId(), question.getUserCreated().getUserId());
    }
}