package com.example.quizeo;

import java.util.ArrayList;
import java.util.Arrays;

public class Question {

    private int id;

    private String question;

    private ArrayList<String> answers;

    private String correct;

    private int correctInt;

    private String explanation;

    private int globalId;

    public Question() {
        question = "";
        answers = new ArrayList<>();
        id = 0;
        globalId = 0;
        correct = "";
    }

    public Question(String question, String[] answers1, String correct, String explanation,
                    int id, int globalId) {
        answers = new ArrayList<>();
        // Add the question:
        this.question = question;
        // Set the answers:
        answers.addAll(Arrays.asList(answers1));

        // set the Correct:
        this.correct = correct;
        for (int i = 0; i < answers.size(); i++) {
            if (answers.get(i).equals(correct)) {
                correctInt = i + 1;
                break;
            }
        }

        // Set the explanation:
        this.explanation = explanation;
        // Set the id:
        this.id = id;

        //set the global id:
        this.globalId = globalId;
    }

    public Question(String question, String[] answers1, int correctInt, String explanation,
                    int id, int globalId) {
        answers = new ArrayList<>();
        // Add the question:
        this.question = question;
        // Set the answers:
        answers.addAll(Arrays.asList(answers1));

        // set the Correct:
        this.correctInt = correctInt;
        correct = answers.get(correctInt - 1);

        // Set the explanation:
        this.explanation = explanation;
        // Set the id:
        this.id = id;

        //set the global id:
        this.globalId = globalId;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswers(String[] answers1) {
        answers.addAll(Arrays.asList(answers1));
    }

    // NoteL correctInt starts at 1.
    public void setCorrect(String correct) {
        this.correct = correct;
        for (int i = 0; i < answers.size(); i++) {
            if (answers.get(i).equals(correct)) {
                correctInt = i + 1;
                break;
            }
        }
    }

    public void setCorrect(int correctInt) {
        this.correctInt = correctInt;
        correct = answers.get(correctInt - 1);
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGlobalId(int globalId) {
        this.globalId = globalId;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getAnswers() {
        String[] returnAnswers = new String[answers.size()];
        for (int i = 0; i < answers.size(); i++) {
            returnAnswers[i] = answers.get(i);
        }
        return returnAnswers;
    }

    public String getExplanation() {
        return explanation;
    }

    public int getId() {
        return id;
    }

    public int getGlobalId() {
        return globalId;
    }

    public boolean getCorrect(String answered) {
        return answered.equals(correct);
    }

    public boolean getCorrect(int chosen) {
        return correctInt == chosen;
    }





}
