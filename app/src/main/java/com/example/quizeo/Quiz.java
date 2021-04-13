package com.example.quizeo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.UUID;

public class Quiz implements Parcelable {

    /** ArrayList in which all Questions are saved: */
    private ArrayList<Question> questions;

    /** global quizId of this quiz: */
    private UUID quizId;

    /** Name of the quiz: */
    private String quizName;

    /** Integer for the current rating and the total number of ratings: */
    private double rating;
    private int nrOfRatings;

    /** Float for the percentage required to pass and integer of the total score
     * required to pass: */
    private float percentageToPass;
    private int scoreToPass;

    /** The total number of questions: */
    private int numberOfQuestions;

    /** Keeps track of the current returned question: */
    private int index;

    /** Location belonging to this quiz: */
    private LocationQuizeo location;

    /** User who created this quiz: */
    private User userCreated;

    /**
     * Initializes all necessary values.
     */
    public Quiz() {
        // Initialize the ArrayList:
        questions = new ArrayList<>();

        // Set some variables to default values:
        numberOfQuestions = 0;
        rating = -1;
        nrOfRatings = 0;
        percentageToPass = 75;
    }

    /**
     * Initializes the quiz and sets some variables to the correct values.
     *
     * @param quizId id of the quiz.
     * @param quizName name of the quiz.
     * @param location location belonging to the current quiz.
     */
    public Quiz(UUID quizId, String quizName, LocationQuizeo location) {
        // Set the passed variables to the correct value:
        this.quizId = quizId;
        this.quizName = quizName;

        // Initialize the ArrayList:
        questions = new ArrayList<>();

        // Set default values for the rest of the variables:
        numberOfQuestions = 0;
        rating = -1;
        nrOfRatings = 0;
        this.location = location;
        percentageToPass = 75;
    }

    /**
     * Initializes the quiz and sets some variables to the correct values.
     *
     * @param quizId id of the quiz.
     * @param quizName name of the quiz.
     * @param location location belonging to the current quiz.
     * @param percentageToPass percentage required to pass the current quiz.
     */
    public Quiz(UUID quizId, String quizName, LocationQuizeo location, int percentageToPass) {
        this.quizId = quizId;
        this.quizName = quizName;
        questions = new ArrayList<>();
        numberOfQuestions = 0;
        rating = -1;
        nrOfRatings = 0;
        this.location = location;
        this.percentageToPass = percentageToPass;
    }

    /**
     * Method for the parcelable, required to pass this object between classes.
     */
    protected Quiz(Parcel in) {
        quizName = in.readString();
        rating = in.readDouble();
        nrOfRatings = in.readInt();
        percentageToPass = in.readFloat();
        scoreToPass = in.readInt();
        numberOfQuestions = in.readInt();
        index = in.readInt();
        questions = in.readArrayList(Question.class.getClassLoader());
        quizId = (UUID) in.readSerializable();
        location = in.readParcelable(LocationQuizeo.class.getClassLoader());
        userCreated = in.readParcelable(User.class.getClassLoader());
    }

    /**
     * Method for the parcelable, required to pass this object between classes.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(quizName);
        dest.writeDouble(rating);
        dest.writeInt(nrOfRatings);
        dest.writeFloat(percentageToPass);
        dest.writeInt(scoreToPass);
        dest.writeInt(numberOfQuestions);
        dest.writeInt(index);
        dest.writeList(questions);
        dest.writeSerializable(quizId);
        dest.writeParcelable(location, flags);
        dest.writeParcelable(userCreated, flags);
    }

    /**
     * Method for the parcelable, required to pass this object between classes.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Creator for the parcelable, required to pass this object between classes.
     */
    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    /**
     * Add a question to the current quiz.
     *
     * @param question question to add to the current quiz.
     */
    public void addQuestion(Question question) {
        // Put the index to 0:
        index = 0;
        // Add the question:
        questions.add(question);

        // Increase the total number of questions by 1 and recalculate the score required to pass:
        numberOfQuestions++;
        float temp = (percentageToPass / 100) * numberOfQuestions;
        scoreToPass = (int) Math.ceil(temp);
    }

    /**
     * Add a list of questions to the current quiz, used when one retrieves quizzes from the
     * database. In the correct order.
     * 
     * @param list of questions to add to the current quiz.
     */
    public void addQuestions(ArrayList<Question> list) {
        // Make new ArrayList:
        questions = new ArrayList<>();
        // Loop over all questions in the list:
        for (int i = 1; i < list.size() + 1; i++) {
            for (Question question : list) {
                // If this is the correct position for this question, continue with the next:
                if (question.getId() == i) {
                    questions.add(question);
                    break;
                }
            }
        }

    }

    /**
     * Get all the questions in the current quiz.
     *
     * @return Array of questions with all questions in the current quiz.
     */
    public Question[] getQuestions() {
        // Create an array to save the questiosn in:
        Question[] toReturn = new Question[questions.size()];
        // Add the questions to the array:
        for (int i = 0; i < questions.size(); i++) {
            toReturn[i] = questions.get(i);
        }
        // Return the array:
        return toReturn;
    }

    /**
     * Get the first question in this quiz.
     *
     * @return The first question (on index 0) of this quiz.
     */
    public Question getFirst() {
        return questions.get(0);
    }

    /**
     * Get the next question each time, while starting at the first question.
     *
     * @return the next question each time.
     */
    public Question getNext() {
        if (index > questions.size()) {
            throw new IndexOutOfBoundsException();
        }
        // Save the old index and increment the index:
        int oldIndex = index;
        index++;
        // Retrieve the question:
        return questions.get(oldIndex);
    }

    /**
     * Get the name of the quiz.
     *
     * @return the quizName.
     */
    public String getQuizName() {
        return quizName;
    }

    /**
     * Set the index back to 0, so start over again with playing this quiz.
     */
    public void quitQuiz() {
        index = 0;
    }

    /**
     * Retrieve the quizId.
     *
     * @return the quizId.
     */
    public UUID getQuizId() {
        return quizId;
    }

    /**
     * Retrieve the number of questions in this quiz.
     *
     * @return the number of Questions in this quiz.
     */
    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    /**
     * Retrieve the location of this quiz.
     *
     * @return the location associated with the quiz.
     */
    public LocationQuizeo getLocation() {
        return location;
    }

    /**
     * Retrieve the user who created this quiz.
     *
     * @return the user who created the quiz.
     */
    public User getUserCreated() {
        return userCreated;
    }

    /**
     * Update the rating, by using an additional rating in this quiz.
     *
     * @param rating1 the rating which should be used to update this rating.
     * @throws IllegalArgumentException if the rating is out of bounds, that is less than 0 or
     *                                  more than 10.
     */
    public void setAdditionalRating(double rating1) throws IllegalArgumentException {
        // If the rating is out of bounds, throw an IllegalArgumentException.
        if (rating1 < 0 || rating1 > 10) {
            throw new IllegalArgumentException();
        }

        // Update the rating, if there is no rating yet, set the rating which was supplied as
        // the new rating. Otherwise, update with a formula:
        if (nrOfRatings == 0) {
            rating = rating1;
        } else {
            rating = (rating * nrOfRatings + rating1) / (nrOfRatings + 1);
        }
        nrOfRatings++;
    }

    /**
     * Retrieve the rating corresponding to this quiz.
     *
     * @return the rating corresponding to this quiz.
     */
    public double getRating() {
        return  rating;
    }

    /**
     * Retrieve the number of ratings corresponding to this quiz.
     *
     * @return the number of ratings corresponding to this quiz.
     */
    public int getNumberOfRatings() {
        return nrOfRatings;
    }

    /**
     * Retrieve the score required to pass this quiz.
     *
     * @return the score required to pass the quiz.
     */
    public int getScoreToPass() {
        return scoreToPass;
    }

    /**
     * Set the name of this quiz.
     *
     * @param quizName the name of this quiz.
     */
    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    /**
     * Set the quiz id of this quiz.
     *
     * @param quizId the id of the quiz.
     */
    public void setQuizId(UUID quizId) {
        this.quizId = quizId;
    }

    /**
     * Set the location corresponding to this quiz.
     *
     * @param location belonging to this quiz.
     */
    public void setLocation(LocationQuizeo location) {
        this.location = location;
    }

    /**
     * Set the percentage required to pass this quiz.
     *
     * @param percentageToPass percentage required to pass this quiz (from 0 to 100).
     */
    public void setPercentageToPass(int percentageToPass) {
        this.percentageToPass = percentageToPass;
    }

    /**
     * Set the user who created this quiz.
     *
     * @param userCreated the user who created this quiz.
     */
    public void setUserCreated(User userCreated) {
        this.userCreated = userCreated;
    }

    /**
     * Check if there is a next question in this quiz.
     *
     * @return true if the next question exists, otherwise false.
     */
    public Boolean nextQuestionExists() {
        return (index < questions.size());
    }

    /**
     * Set the number of questions of this quiz
     *
     * @param value the number of questions
     */
    public void setNumberOfQuestions(int value) {
        this.numberOfQuestions = value;
    }

    /**
     * Set the number of ratings of this quiz
     *
     * @param value number of ratings
     */
    public void setNrOfRatings(int value) {
        this.nrOfRatings = value;
    }

    /**
     * Set the rating of this quiz
     *
     * @param rating the new rating
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * Set the score to pass of this quiz
     *
     * @param value the new score to pass
     */
    public void setScoreToPass(int value) {
        this.scoreToPass = value;
    }

    /**
     * Retrieve the percentage required to pass this quiz.
     *
     * @return the percentage required to pass the quiz.
     */
    public float getPercentageToPass() {
        return percentageToPass;
    }
}
