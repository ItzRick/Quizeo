package com.example.quizeo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

/* TODO:
    -Upload Questions
    -Upload Quiz
        (Check for Duplicates)
    Remove Questions
    Remove Quiz
    Edit Questions
    Edit Quiz
    -Get New ID
    -Query for quiz with location
    Query for quiz with user
    Find Questions of the quiz
    -IsTaken method handling of failures
    ~Login
*/

//Database Interface Singleton
public final class Database {

    /** Range we want to query for nearby quizzes in kilometer and degrees */
    private final int RANGE = 5;
    private final double RANGE_IN_DEGREES = (RANGE / 110.574) % 360;

    private static final String TAG = "";
    static private Database instance;

    final private FirebaseFirestore firestore;
    final private CollectionReference questionRef;
    final private CollectionReference quizRef;

    private Database() {
        firestore = FirebaseFirestore.getInstance();
        questionRef = firestore.collection("Questions");
        quizRef = firestore.collection("Quizzes");
    }

    /**
     * Returns database singleton object
     *
     * @return database singleton object
     */
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    //------------------------- DOWNLOAD -----------------------

    /**
     * Returns a list of quizzes that are close to location
     *
     * @param location the location you want to query
     * @param callback the callback object
     */
    public void getQuizzes(Location location, DownloadQuizzesCallback callback) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        double minLong = (longitude - (RANGE_IN_DEGREES / 2)) % 360;
        double maxLong = (longitude + (RANGE_IN_DEGREES / 2)) % 360;

        double minLat = (latitude - (RANGE_IN_DEGREES / 2)) % 360;
        double maxLat = (latitude + (RANGE_IN_DEGREES / 2)) % 360;

        quizRef.whereGreaterThanOrEqualTo("Latitude", minLat).whereLessThanOrEqualTo("Latitude", maxLat)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    ArrayList<Quiz> list = new ArrayList<>();
                    for (DocumentSnapshot doc: snapshot.getDocuments()) {
                        double longitude = (double) doc.get("Longitude");
                        if (longitude >= minLong && longitude <= maxLong) {
                            list.add(new Quiz());
                            docToQuiz(doc, list.get(list.size()-1));
                        }
                    }
                    callback.onCallback(list);
                } else {
                    Log.d(TAG, "getting quizzes failed");
                }
            }
        });
    }

    /** Callback interface for downloading quizzes */
    public interface DownloadQuizzesCallback {
        void onCallback(ArrayList<Quiz> list);
    }


    /**
     * get list of questions that belong to a quiz
     *
     * @param quizID the question you want to get the questions from
     * @param callback the callback object
     */
    public void getQuestions(UUID quizID, DownloadQuestionListCallback callback) {

        questionRef.whereEqualTo("Part of", quizID.toString()).
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    if (snapshot.isEmpty()) {
                        System.out.println("snapshot is empty");
                    }

                    ArrayList<Question> list = new ArrayList<>();

                    for (DocumentSnapshot doc: snapshot.getDocuments()) {
                        list.add(new Question());
                        docToQuestion(doc, list.get(list.size()-1));
                    }
                    callback.onCallback(list);
                } else {
                    Log.d(TAG, "getting question failed");
                }
            }
        });
    }

    /** Callback interface for downloading questions */
    public interface DownloadQuestionListCallback {
        void onCallback(ArrayList<Question> list);
    }

    //I think this method is redundant now, but I will keep it here for now
    /**
     * Returns question object from the database
     *
     * @param questionID the id you want to retrieve from the database
     * @param callback callback object to which the question object will be send
     * @throws NoSuchElementException if question with questionId is not found
     */
    private void getQuestion(UUID questionID, DownloadQuestionCallback callback) throws NoSuchElementException {

        DocumentReference docRef = firestore.collection("Questions").document(questionID.toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        System.out.println("Download success");
                        Question result = new Question();
                        docToQuestion(document, result);
                        callback.onCallback(result);
                    } else {
                        Log.d(TAG, "No such document");
                        throw new NoSuchElementException("Question not found");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    /** Callback interface for downloading questions */
    public interface DownloadQuestionCallback {
        void onCallback(Question question);
    }


    /**
     * Converts DocumentSnapshot to Question object
     *
     * @param doc document to be converted
     * @param q question object to which will hold the new values
     */
    private void docToQuestion(DocumentSnapshot doc, Question q) {

        //getting the values
        UUID GlobalID = (UUID) UUID.fromString((String) doc.get("GlobalID"));
        String question = (String) doc.get("Question");
        String explanation = (String) doc.get("Explanation");
        ArrayList<String> answers = (ArrayList<String>) doc.get("Answers");
        String correct = (String) doc.get("Correct");
        int ID = ((Long) doc.get("Question Number")).intValue();

        String[] ans = new String[answers.size()];
        for (int i = 0; i < answers.size(); i++) {
            ans[i] = answers.get(i);
        }

        //setting in Question object
        q.setAnswers(ans);
        q.setCorrect(correct);
        q.setGlobalId(GlobalID);
        q.setId(ID);
        q.setQuestion(question);
        q.setExplanation(explanation);
    }

    /**
     * ----IMPORTANT: SOME VARIABLES HAVE NOT BEEN SET CORRECTLY --------
     * @param doc the document to convert
     * @param q the quiz object which will hold the new values
     */
    private void docToQuiz(DocumentSnapshot doc, Quiz q) {
        UUID GlobalID = (UUID) UUID.fromString((String) doc.get("GlobalID"));
        Location location = new Location((double) doc.get("Latitude"), (double) doc.get("Longitude"));
        String name = (String) doc.get("Name");
        int nrOfQuestions = ((Long) doc.get("Number of Questions")).intValue();
        if (doc.get("Number of Ratings") instanceof Double) {
            int nrOfRatings = ((Double) doc.get("Number of Ratings")).intValue();
        } else {
            int nrOfRatings = ((Long) doc.get("Number of Ratings")).intValue();
        }
        double rating = (double) doc.get("Rating");
        int scoreToPass = ((Long) doc.get("Score to Pass")).intValue();
        UUID user = (UUID) doc.get("User");

        q.setLocation(location);
        q.setQuizId(GlobalID);
        q.setQuizName(name);

    }


    //------------------------- UPLOAD -----------------------
    /**
     * Uploads a quiz and all its questions to the database
     *
     * @param quiz Quiz to uploaded
     */
    public void uploadQuiz(Quiz quiz) {
        // ----- Preparing for upload ------
        Location loc = quiz.getLocation(); //location of the quiz

        Map<String, Object> data = new HashMap<>();
        ArrayList<String> listOfQuestions = new ArrayList<>();

        for (Question question: quiz.getQuestions()) {
            listOfQuestions.add(question.getGlobalId().toString());
            uploadQuestion(question, quiz);
        }

        data.put("GlobalID", quiz.getQuizId().toString());
        data.put("Name", quiz.getQuizName());
        data.put("Rating", quiz.getRating());
        data.put("Number of Ratings", quiz.getNumberOfRatings());
        data.put("Number of Questions", quiz.getNumberOfQuestions());
        data.put("Score to Pass", quiz.getScoreToPass());
        data.put("User created", quiz.getUserCreated());
        data.put("Latitude", loc.getLatitude());
        data.put("Longitude", loc.getLongitude());
        data.put("Questions", listOfQuestions);

        // ------ upload quiz ------

        firestore.collection("Quizzes").document(quiz.getQuizId().toString())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("Upload Success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Upload failure");
                    }
                });
    }

    /**
     * Upload a question to the database, question is part of multiple quizzes
     *
     * @param question question to be uploaded
     * @return whether the upload was successful
     */
    public void uploadQuestion(Question question, Quiz quiz) {

        HashMap<String, Object> data = questionToMap(question, quiz);

        System.out.println("Starting upload");
        //upload to FireStore
        firestore.collection("Questions").document(question.getGlobalId().toString())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("Upload Success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Upload failure");
                    }
                });
    }

    /**
     * Converts a Question object into a Hash map
     *
     * @param question question object to be converted
     * @param quiz quiz the question is part of
     * @return question object as hash map
     */
    private HashMap<String, Object> questionToMap(Question question, Quiz quiz) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("GlobalID", question.getGlobalId().toString());
        data.put("Question", question.getQuestion());
        data.put("Correct", question.getCorrect());
        data.put("Explanation", question.getExplanation());
        data.put("Part of", quiz.getQuizId().toString());
        data.put("Question Number", question.getId());

        ArrayList<String> ans = new ArrayList<String>(Arrays.asList(question.getAnswers()));
        data.put("Answers", ans);

        return data;
    }

    //------------------------- EDIT -----------------------

    /**
     *
     * @param question
     * @param quiz
     */
    private void updateQuestion(Question question, Quiz quiz) {
    }


    /**
     *
     */
    private void updateQuiz() {
    }



    //------------------------- DELETE -----------------------

    /**
     *  Removes a question from a quiz,
     *  if the questions is part of no quizzes after it will deleted from the database
     *
     * @param question question to be removed
     * @param quiz quiz the question will be removed from
     */
    public void removeQuestionFromQuiz(Question question, Quiz quiz) {
        removeQuestionFromQuiz(question.getGlobalId(), quiz.getQuizId());
    }

    /**
     *  Removes a question from a quiz,
     *  if the questions is part of no quizzes after it will deleted from the database
     *
     * @param uuidOfQuestion uuid of the question to be removed
     * @param uuidOfQuiz uuid of the quiz the question will be removed from
     */
    public void removeQuestionFromQuiz(UUID uuidOfQuestion, UUID uuidOfQuiz) {
        //Map<String, Object> map = firestore.collection("Quizzes").
        //                          document(uuidOfQuiz.toString()).get().getResult().getData();
        //ArrayList<Object> list = (ArrayList<Object>) map.get("Questions");
        firestore.collection("Quizzes").document(uuidOfQuiz.toString()).
                update("Questions", FieldValue.arrayRemove(uuidOfQuestion));

    }

    //------------------------- NEW ID -----------------------
    /** A variable to transfer data from listener to uuidIsTaken method */
    private boolean isTaken;

    /**
     * Generates an new ID until it finds an unused ID
     *
     * @return new unused UUID
     */
    public UUID getNewID() {
        UUID newID;
        do {
            newID = UUID.randomUUID();
        } while (uuidIsTaken(newID, questionRef) || uuidIsTaken(newID, quizRef));
        return newID;
    }

    /**
     * Checks whether a given UUID is already taken in a certain collection
     *
     * @param uuid the id we want c=to check
     * @param cr the collection that we want to check
     * @return whether the UUID is already in the database
     */
    private boolean uuidIsTaken(UUID uuid, CollectionReference cr) {
        Query hasID = cr.whereEqualTo("GlobalID", uuid.toString());
        hasID.get().addOnCompleteListener(new newIdOnCompleteListener());
        return isTaken;
    }

    /**
     *  Set new value of isTaken boolean
     *
     * @param value the new value
     */
    public void setIsTaken(boolean value) {
        isTaken = value;
    }

    /**
     * OnCompletionListener for get new ID task
     */
    private class newIdOnCompleteListener implements OnCompleteListener<QuerySnapshot> {

        @Override
        public void onComplete(@NonNull  Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                Database.getInstance().setIsTaken(!task.getResult().isEmpty());
            } else { //Not a nice way of handling failure
                System.out.println("uuidIsTaken() failed");
            }
        }
    }

}