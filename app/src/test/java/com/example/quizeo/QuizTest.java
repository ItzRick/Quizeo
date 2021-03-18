package com.example.quizeo;

import androidx.core.widget.TextViewCompat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class QuizTest {

    Quiz quiz;

    Question question;

    @Before
    public void setUp() {
        // Create the quiz:
        quiz = new Quiz();
        // Create a question with all required fields and add this to the quiz:
        String string = "this is a question";
        String[] array = new String[]{
                "first", "two", "three", "four", "five"
        };
        String correct = "first";
        String explanation = "this is an explanation!";
        int id = 1;
        UUID globalId = UUID.randomUUID();
        question = new Question(string, array, correct, explanation, id, globalId);
        quiz.addQuestion(question);
    }

    /** Test the addQuestion() and getNext() method. */
    @Test
    public void addQuestion() {
        System.out.println("addQuestion()");
        // Add the question another time to the quiz:
        quiz.addQuestion(question);

        // Retrieve the questions and check if they actually exist.
        Question question1 = quiz.getNext();
        Question question2 = quiz.getNext();
        Assert.assertNotNull(question1);
        Assert.assertNotNull(question2);
    }

    /** Test the addQuestion() and getNext() exception. */
    @Test(expected = IndexOutOfBoundsException.class)
    public void getNextException() {
        System.out.println("getNextException()");
        // Add the question another time to the quiz:

        // Retrieve the questions and check if they actually exist.
        quiz.getNext();
        quiz.getNext();
    }

    /** Test the getQuestions() method. */
    @Test
    public void getQuestions() {
        System.out.println("getQuestions()");
        // Add another question this quiz:
        quiz.addQuestion(question);

        // Retrieve the questions and check that these questions exist:
        Question[] questions = quiz.getQuestions();
        for (Question question1 : questions) {
            Assert.assertNotNull(question1);
        }
    }

    /** Test the getFirst() method. */
    @Test
    public void getFirst() {
        System.out.println("getFirst()");

        // Check that there exists a question returned by the getFirst() method:
        Assert.assertNotNull(quiz.getFirst());
    }

    /** Test the getNext() method with 2 questions. */
    @Test
    public void getNext() {
        System.out.println("getNext()");
        // Add another different question to the quiz:
        Question question1 = new Question();
        question1.setQuestion("second");
        quiz.addQuestion(question1);

        // Test that the two questions exist and are not the same:
        Question question2 = quiz.getNext();
        Question question3 = quiz.getNext();
        Assert.assertNotNull(question2);
        Assert.assertNotNull(question3);
        Assert.assertNotEquals(question2.getQuestion(), question3.getQuestion());
    }

    /** Test the getQuizeName and setQuizeName() methods. */
    @Test
    public void getQuizName() {
        System.out.println("getQuizName()");

        // Add a quizName to this quiz:
        String quizName = "This is a quiz";

        // Check if this quizName is actually the same:
        quiz.setQuizName(quizName);
        Assert.assertEquals(quizName, quiz.getQuizName());
    }

    /** test the quitQuiz() method. */
    @Test
    public void quitQuiz() {
        System.out.println("quitQuiz()");

        // Retrieve a question and call quitQuiz():
        Question question1 = quiz.getNext();
        quiz.quitQuiz();
        // Retrieve the question another time and check that these two are the same.
        Question question2 = quiz.getNext();
        Assert.assertEquals(question1.getQuestion(), question2.getQuestion());
    }

    /** Test the getQuizId() and setQuizId methods. */
    @Test
    public void getQuizId() {
        System.out.println("getQuizId()");

        // Create an Id and add this to the quiz:
        UUID id = UUID.randomUUID();
        quiz.setQuizId(id);

        // Retrieve this Id and make sure this is equal to the Id that was added:
        Assert.assertEquals(id, quiz.getQuizId());
    }

    /** Test the getNumberOfQuestions() method. */
    @Test
    public void getNumberOfQuestions() {
        System.out.println("getNumberOfQuestions() 1");
        // Test if the numberOfQuestions is correct.
        Assert.assertEquals(1, quiz.getNumberOfQuestions());
    }

    /** Test the getNumberOfQuestions() method. */
    @Test
    public void getNumberOfQuestions1() {
        System.out.println("getNumberOfQuestions() 2");
        // Add another question and test if the numberOfQuestions updates.
        quiz.addQuestion(question);
        Assert.assertEquals(2, quiz.getNumberOfQuestions());
    }

    /** Test the getNumberOfQuestions() method. */
    @Test
    public void getNumberOfQuestions2() {
        System.out.println("getNumberOfQuestions() 3");
        // Add another two questions and test if the numberOfQuestions updates.
        quiz.addQuestion(question);
        quiz.addQuestion(question);
        Assert.assertEquals(3, quiz.getNumberOfQuestions());
    }

    /** Test the getLocation() and setLocation() methods. */
    @Test
    public void getLocation() {
        System.out.println("getLocation()");

        // Add a new location to this quiz:
        Location location = new Location(1, 1);
        quiz.setLocation(location);

        // Retrieve this location and confirm this is the same location:
        Location location1 = quiz.getLocation();
        Assert.assertEquals((int)location.getLatitude(), (int)location1.getLatitude());
        Assert.assertEquals((int)location.getLongitude(), (int)location1.getLongitude());
        Assert.assertEquals(location, location1);
    }

    /** test the getRating() method with no ratings added. */
    @Test
    public void getRating() {
        System.out.println("getRating() 1");

        // Retrieve the rating and check if the rating is correct:
        double rating = quiz.getRating();
        Assert.assertEquals(-1, (int)rating);
    }

    /** test the getRating() method with 1 rating added. */
    @Test
    public void getRating1() {
        System.out.println("getRating() 2");

        // Add a rating to this quiz:
        quiz.setAdditionalRating(10);

        // Retrieve the rating and check if the rating is correct:
        double rating = quiz.getRating();
        Assert.assertEquals(10, (int)rating);
    }

    /** test the getRating() method with 2 ratings added. */
    @Test
    public void getRating2() {
        System.out.println("getRating() 3");

        // Add 2 ratings to this quiz:
        quiz.setAdditionalRating(10);
        quiz.setAdditionalRating(0);

        // Retrieve the rating and check if the rating is correct:
        double rating = quiz.getRating();
        Assert.assertEquals(5, (int)rating);
    }

    /** Test if the getRating method throws an IllegalArgumentException when the rating is less
     * than 0. */
    @Test(expected = IllegalArgumentException.class)
    public void getRating3() {
        System.out.println("getRating() exception 1");
        // Add a rating that is less than 0:
        quiz.setAdditionalRating(-0.1);
    }

    /** Test if the getRating method throws an IllegalArgumentException when the rating is more
     * than 10. */
    @Test(expected = IllegalArgumentException.class)
    public void getRating4() {
        System.out.println("getRating() exception 2");
        // Add a rating that is more than 10:
        quiz.setAdditionalRating(10.1);
    }

    /** Test the getScoreToPass() method with 1 question: */
    @Test
    public void getScoreToPass() {
        System.out.println("getScoreToPass() 1");
        Assert.assertEquals(1, quiz.getScoreToPass());
    }

    /** Test the getScoreToPass() method with 4 questions: */
    @Test
    public void getScoreToPass1() {
        System.out.println("getScoreToPass() 2");
        // Add 3 questions:
        quiz.addQuestion(question);
        quiz.addQuestion(question);
        quiz.addQuestion(question);

        // Check if the getScoreToPass() is correct:
        Assert.assertEquals(3, quiz.getScoreToPass());
    }

    /** Test the setPercentageToPass() method: */
    @Test
    public void setPercentageToPass() {
        System.out.println("setPercentageToPass() 1");
        // Set the percentage to pass to 50%:
        quiz.setPercentageToPass(50);

        // Add 3 questions:
        quiz.addQuestion(question);
        quiz.addQuestion(question);
        quiz.addQuestion(question);

        // Test if the getScoreToPass() with this new percentage to pass is correct:
        Assert.assertEquals(2, quiz.getScoreToPass());
    }

    /** Test the setPercentageToPass() method: */
    @Test
    public void setPercentageToPass1() {
        System.out.println("setPercentageToPass() 2");
        // Set the percentage to pass to 100%:
        quiz.setPercentageToPass(100);

        // Add 3 questions:
        quiz.addQuestion(question);
        quiz.addQuestion(question);
        quiz.addQuestion(question);

        // Test if the getScoreToPass() with this new percentage to pass is correct:
        Assert.assertEquals(4, quiz.getScoreToPass());
    }

    /** Test the setUserCreated() and getUserCreated() methods. */
    @Test
    public void getUserCreated() {
        System.out.println("getUserCreated()");

        // Create a new user and add this to the quiz:
        User user = new User("user", UUID.randomUUID().toString());
        quiz.setUserCreated(user);

        // Check that the getUserCreated() method returns the correct user:
        Assert.assertEquals(user.getNickName(), quiz.getUserCreated().getNickName());
        Assert.assertEquals(user.getUserId(), quiz.getUserCreated().getUserId());
    }

    /** Test if the initialization correctly works. */
    @Test
    public void Initialization0() {
        System.out.println("Initialization() 1");

        // Create an Id, quizName and location and initialize the quiz:
        UUID id = UUID.randomUUID();
        String quizName = "This is a quiz";
        Location location = new Location(1, 1);
        Quiz quiz1 = new Quiz(id, quizName, location);

        // Check if these values were correctly added to the quiz:
        Assert.assertEquals(id, quiz1.getQuizId());
        Assert.assertEquals(quizName, quiz1.getQuizName());
        Location location1 = quiz1.getLocation();
        Assert.assertEquals((int)location.getLatitude(), (int)location1.getLatitude());
        Assert.assertEquals((int)location.getLongitude(), (int)location1.getLongitude());
        Assert.assertEquals(location, location1);

    }

    /** Test if the initialization correctly works. */
    @Test
    public void Initialization1() {
        System.out.println("Initialization() 1");

        // Create an Id, quizName, location and percentageToPass and initialize the quiz:
        UUID id = UUID.randomUUID();
        String quizName = "This is a quiz";
        Location location = new Location(1, 1);
        int percentageToPass = 50;
        Quiz quiz1 = new Quiz(id, quizName, location, percentageToPass);
        quiz1.addQuestion(question);

        // Check if these values were correctly added to the quiz:
        Assert.assertEquals(id, quiz1.getQuizId());
        Assert.assertEquals(quizName, quiz1.getQuizName());
        Location location1 = quiz1.getLocation();
        Assert.assertEquals((int)location.getLatitude(), (int)location1.getLatitude());
        Assert.assertEquals((int)location.getLongitude(), (int)location1.getLongitude());
        Assert.assertEquals(location, location1);
        Assert.assertEquals(1, quiz1.getScoreToPass());
    }

    @Test
    public void nextQuestionExists() {
        System.out.println("nextQuestionExists() 1");
        Assert.assertTrue(quiz.nextQuestionExists());
    }

    @Test
    public void nextQuestionExists1() {
        System.out.println("nextQuestionExists() 2");
        quiz.getNext();
        Assert.assertFalse(quiz.nextQuestionExists());
    }
}