package com.example.quizeo;

import java.util.ArrayList;

public class Quiz {

    ArrayList<Question> questions;

    int quizId;

    String quizName;

    double rating;
    int nrOfRatings;

    float percentageToPass;
    int scoreToPass;

    int numberOfQuestions;

    // Keeps track of the currently returned question:
    int index;

    Location location;

    public Quiz() {
        questions = new ArrayList<>();
        numberOfQuestions = 0;
        rating = -1;
        nrOfRatings = 0;
        percentageToPass = 75;
    }

    public Quiz(int quizId, String quizName, Location location) {
        this.quizId = quizId;
        this.quizName = quizName;
        questions = new ArrayList<>();
        numberOfQuestions = 0;
        rating = -1;
        nrOfRatings = 0;
        this.location = location;
        percentageToPass = 75;
    }

    public Quiz(int quizId, String quizName, Location location, int percentageToPass) {
        this.quizId = quizId;
        this.quizName = quizName;
        questions = new ArrayList<>();
        numberOfQuestions = 0;
        rating = -1;
        nrOfRatings = 0;
        this.location = location;
        this.percentageToPass = percentageToPass;
    }

    public void addQuestion(Question question) {
        index = 0;
        questions.add(question);
        numberOfQuestions++;
        float temp = (percentageToPass / 100) * numberOfQuestions;
        scoreToPass = (int) Math.ceil(temp);
    }

    public Question[] getQuestions() {
        Question[] toReturn = new Question[questions.size()];
        for (int i = 0; i < questions.size(); i++) {
            toReturn[i] = questions.get(i);
        }
        return toReturn;
    }

    public Question getFirst() {
        return questions.get(0);
    }

    public Question getNext() {
        int oldIndex = index;
        index++;
        return questions.get(oldIndex);
    }

    public String getQuizName() {
        return quizName;
    }

    public void quitQuiz() {
        index = 0;
    }

    public int getQuizId() {
        return quizId;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public Location getLocation() {
        return location;
    }

    public void setAdditionalRating(double rating1) throws IllegalArgumentException {
        if (rating1 < 0 || rating1 > 10) {
            throw new IllegalArgumentException();
        }
        if (nrOfRatings == 0) {
            rating = rating1;
        } else {
            rating = (rating * nrOfRatings + rating1) / (nrOfRatings + 1);
        }
        nrOfRatings++;
    }

    public double getRating() {
        return  rating;
    }

    public int getScoreToPass() {
        return scoreToPass;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setPercentageToPass(int percentageToPass) {
        this.percentageToPass = percentageToPass;
    }
}
