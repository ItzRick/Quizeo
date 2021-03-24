package com.example.quizeo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class Question implements Parcelable {

    /** Id of the question inside the quiz, that is which number in the quiz it is: */
    private int id;

    /** The question text: */
    private String question;

    /** ArrayList in which all the answers are saved: */
    private ArrayList<String> answers;

    /** The string of the correct answer: */
    private String correct;

    /** Index in the ArrayList of the correct question: */
    private int correctInt;

    /** Explanation for why this answer is correct: */
    private String explanation;

    /** Global ID for this question: */
    private UUID globalId;

    /** The user who created this question. */
    private User userCreated;

    /**
     * Initializes the the answers arrayList.
     */
    public Question() {
        // Initializes the ArrayList:
        answers = new ArrayList<>();
    }

    /**
     * Initializes the arrayList and sets some variables to the correct value.
     *
     * @param question the value of the question string which should be set.
     * @param answers1 The answers of this question, which should be added to the arrayList.
     * @param correct The Correct answer as String to save.
     * @param explanation The explanation for this question, to save.
     * @param id The ID of this question, that is, the index of it in the accompanying quiz.
     * @param globalId The globalID of this question tgo save.
     */
    public Question(String question, String[] answers1, String correct, String explanation,
                    int id, UUID globalId) {
        // Initialize the answers ArrayList:
        answers = new ArrayList<>();
        // Add the question:
        this.question = question;
        // Add the answers to the ArrayList:
        answers.addAll(Arrays.asList(answers1));

        // set the Correct:
        this.correct = correct;

        // Save the Id of the correct answer:
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

    /**
     * Initializes the arrayList and sets some variables to the correct value.
     *
     * @param question the value of the question string which should be set.
     * @param answers1 The answers of this question, which should be added to the arrayList.
     * @param correctInt The index in the answers array of the correct answer.
     * @param explanation The explanation for this question, to save.
     * @param id The ID of this question, that is, the index of it in the accompanying quiz.
     * @param globalId The globalID of this question tgo save.
     */
    public Question(String question, String[] answers1, int correctInt, String explanation,
                    int id, UUID globalId) {
        answers = new ArrayList<>();
        // Add the question:
        this.question = question;
        // Add the answers to the ArrayList:
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

    protected Question(Parcel in) {
        id = in.readInt();
        question = in.readString();
        answers = in.createStringArrayList();
        correct = in.readString();
        correctInt = in.readInt();
        explanation = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    /**
     * Sets the question String of this question.
     *
     * @param question String to set the Question value to.
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Adds the answers to the arrayList.
     *
     * @param answers1 Array of Strings to add to the answers.
     */
    public void setAnswers(String[] answers1) {
        answers.addAll(Arrays.asList(answers1));
    }

    /**
     * Sets the Correct answer to the String given and sets the correctInt to the corresponding
     * value.
     *
     * @param correct The correct answer of this question.
     */
    // NoteL correctInt starts at 1.
    public void setCorrect(String correct) {
        // Set the correct String to the correct answer:
        this.correct = correct;

        /* Retrieve the index of this String and save it to correctInt. This starts at 1 for the
        first element in the ArrayList. */
        for (int i = 0; i < answers.size(); i++) {
            if (answers.get(i).equals(correct)) {
                correctInt = i + 1;
                break;
            }
        }
    }

    /**
     * Set the correct answer by giving the index of this answer.
     *
     * @param correctInt Index of the ArrayList for the correct answer, counting starts at 1.
     */
    public void setCorrect(int correctInt) {
        // Save the correctInt value:
        this.correctInt = correctInt;

        // Retrieve and save the accompanying string:
        correct = answers.get(correctInt - 1);
    }

    /**
     * Set the explanation for this question.
     *
     * @param explanation The explanation for this question in a String.
     */
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    /**
     * Set the id, that is index in the quiz for this question.
     *
     * @param id index that this question should have in the accompying quiz.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set the global ID of this question.
     *
     * @param globalId that should be set for this question.
     */
    public void setGlobalId(UUID globalId) {
        this.globalId = globalId;
    }

    /**
     * Set the user that created the question.
     *
     * @param userCreated that created this question.
     */
    public void setUserCreated(User userCreated) {
        this.userCreated = userCreated;
    }

    /**
     * Returns the String for the question.
     *
     * @return String for the question.
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Returns the answers.
     *
     * @return Array of Strings with all answers.
     */
    public String[] getAnswers() {
        String[] returnAnswers = new String[answers.size()];
        for (int i = 0; i < answers.size(); i++) {
            returnAnswers[i] = answers.get(i);
        }
        return returnAnswers;
    }

    /**
     * Returns the explanation.
     *
     * @return explanation (as String).
     */
    public String getExplanation() {
        return explanation;
    }

    /**
     * Returns the Id, that is index in the quiz of this question.
     *
     * @return the Id.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the global Id of this question.
     *
     * @return globalId.
     */
    public UUID getGlobalId() {
        return globalId;
    }

    /**
     * Check if the question was answered correctly.
     *
     * @param answered String of the option that is answered.
     * @return True if the answer was correct, otherwise False.
     */
    public boolean getCorrect(String answered) {
        return answered.equals(correct);
    }

    /**
     * Check if the answer was chosen correctly.
     *
     * @param chosen Index of the answered question (starting at 1).
     * @return True if the answer was correct, otherwise False.
     */
    public boolean getCorrect(int chosen) {
        return correctInt == chosen;
    }

    /**
     * Get the correct answer (as String).
     *
     * @return String of the correct answer.
     */
    public String getCorrect() {
        return correct;
    }

    /**
     * Get the correct answer (as int).
     *
     * @return int of the correct answer.
     */
    public int getCorrectInt() {
        return correctInt;
    }

    /**
     * Get the user that has created this question.
     *
     * @return User that created this quiz.
     */
    public User getUserCreated() {
        return userCreated;
    }

    public int getNumberOfAnswers() {
        return answers.size();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(question);
        dest.writeStringList(answers);
        dest.writeString(correct);
        dest.writeInt(correctInt);
        dest.writeString(explanation);
    }
}
