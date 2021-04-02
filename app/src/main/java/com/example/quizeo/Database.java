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


//Database Interface Singleton
public final class Database {

    /** Range we want to query for nearby quizzes in kilometer and degrees */
    private final int RANGE = 5;
    private final double RANGE_IN_DEGREES = (RANGE / 110.574) % 360;

    /** Tag used for logs */
    private static final String TAG = "";

    /** Holds the database singleton instance */
    static private Database instance;

    /** FireStore instance */
    final private FirebaseFirestore firestore;

    /** The collection references for questions and quizzes */
    final private CollectionReference questionRef;
    final private CollectionReference quizRef;
    final private CollectionReference notPublishedRef;
    final private CollectionReference historyRef;

    /** Constructor, should only be called by getInstance method */
    private Database() {
        firestore = FirebaseFirestore.getInstance();
        questionRef = firestore.collection("Questions");
        quizRef = firestore.collection("Quizzes");
        notPublishedRef = firestore.collection("Not-Published");
        historyRef = firestore.collection("User-History");
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

    //region -- DOWNLOAD --
    /**
     * Returns a list of quizzes that are close to location
     *
     * @param location the location you want to query
     * @param callback the callback object
     */
    public void getQuizzes(LocationQuizeo location, DownloadQuizzesCallback callback) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        // minimum and maximum longitude we want to query
        double minLong = (longitude - (RANGE_IN_DEGREES / 2)) % 360;
        double maxLong = (longitude + (RANGE_IN_DEGREES / 2)) % 360;

        // minimum and maximum latitude we want to query
        double minLat = (latitude - (RANGE_IN_DEGREES / 2)) % 360;
        double maxLat = (latitude + (RANGE_IN_DEGREES / 2)) % 360;

        quizRef.whereGreaterThanOrEqualTo("Longitude", minLong)
                .whereLessThanOrEqualTo("Longitude", maxLong)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    ArrayList<Quiz> list = new ArrayList<>();
                    for (DocumentSnapshot doc: snapshot.getDocuments()) {
                        if (doc.get("Latitude") != null) {
                            double latitude1 = (Double) doc.get("Latitude");
                            if (latitude1 >= minLat && latitude1 <= maxLat) {
                                list.add(new Quiz());
                                docToQuiz(doc, list.get(list.size() - 1));
                            }
                        }
                    }
                    callback.onCallback(list);
                } else {
                    Log.d(TAG, "getting quizzes failed");
                }
            }
        });
    }

    /**
     * Gets all quizzes made by an user
     *
     * @param user the user object of the creator
     * @param callback the callback object
     * @param nonPublished whether to query not published quizzes aswell
     */
    public void getQuizzes(User user, boolean nonPublished, DownloadQuizzesCallback callback) {
        getQuizzes(user.getUserId(), nonPublished, callback);
    }

    /**
     * Gets all quizzes made by an user
     *
     * @param userID the creator of the quizzes
     * @param callback the callback object
     * @param nonPublished whether to query not published quizzes aswell
     */
    public void getQuizzes(String userID, boolean nonPublished, DownloadQuizzesCallback callback) {
        quizRef.whereEqualTo("UserId", userID)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    ArrayList<Quiz> list = new ArrayList<>();
                    //convert the documents to quiz objects and add them to the list
                    for (DocumentSnapshot doc: snapshot.getDocuments()) {
                        list.add(new Quiz());
                        docToQuiz(doc, list.get(list.size()-1));
                    }
                    if (nonPublished) { //Query non published quizzes aswell
                        notPublishedRef.whereEqualTo("UserId", userID)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot snapshot = task.getResult();
                                    //convert the documents to quiz objects and add them to the list
                                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                                        list.add(new Quiz());
                                        docToQuiz(doc, list.get(list.size() - 1));
                                    }
                                    //return the result via the callback object
                                    callback.onCallback(list);
                                } else {
                                    Log.d("QuizDownOnUser", "Query failed");
                                }
                            }
                        });
                    } else {
                        //return the result via the callback object
                        callback.onCallback(list);
                    }
                } else {
                    Log.d("QuizDownOnUser", "Query failed");
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
                    ArrayList<Question> list = new ArrayList<>();

                    //convert documents to questions and them to the list
                    for (DocumentSnapshot doc: snapshot.getDocuments()) {
                        list.add(new Question());
                        docToQuestion(doc, list.get(list.size()-1));
                    }
                    //return the result via the callback object
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

    /**
     * Returns single question object from the database
     *
     * @param questionID the id you want to retrieve from the database
     * @param callback callback object to which the question object will be send
     * @throws NoSuchElementException if question with questionId is not found
     */
    private void getQuestion(UUID questionID, DownloadQuestionCallback callback)
            throws NoSuchElementException {

        DocumentReference docRef = firestore.collection("Questions")
                .document(questionID.toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Question result = new Question();
                        docToQuestion(document, result);
                        callback.onCallback(result); //return the result via the callback object
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
     * Gets the user play history from the database
     *
     * @param user The user object of the user
     * @param callback callback object
     */
    public void getUserHistory(User user, DownloadHistoryCallback callback) {
        getUserHistory(user.getUserId(), callback);
    }

    /**
     * Gets the user play history from the database
     *
     * @param userId The id of the user
     * @param callback callback object
     */
    public void getUserHistory(String userId, DownloadHistoryCallback callback) {
        historyRef.document(userId).collection("Quizzes").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshot = task.getResult();
                    ArrayList<HashMap<String, Object>> result = new ArrayList<>();

                    //Convert the docs to hashmaps and add them to the list
                    for (DocumentSnapshot doc: snapshot.getDocuments()) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("QuizID", (UUID) UUID.fromString((String) doc.get("QuizID")));
                        map.put("QuizName", (String) doc.get("QuizName"));
                        Boolean temp = Boolean.valueOf((String) doc.get("Passed?"));
                        map.put("Passed", temp);
                        result.add(map);
                    }
                    //return the result via the callback object
                    callback.onCallback(result);
                } else {
                    Log.d(TAG, "getting question failed");
                }
            }
        });
    }

    /** Callback interface for downloading questions */
    public interface DownloadHistoryCallback {
        void onCallback(ArrayList<HashMap<String, Object>> result);
    }


    /**
     * Converts DocumentSnapshot to Question object
     *
     * @param doc document to be converted
     * @param q question object to which will hold the new values
     */
    private void docToQuestion(DocumentSnapshot doc, Question q) {

        //getting the values
        UUID GlobalID = UUID.fromString((String) doc.get("GlobalID"));
        String question = (String) doc.get("Question");
        String explanation = (String) doc.get("Explanation");
        ArrayList<String> answers = (ArrayList<String>) doc.get("Answers");
        String correct = (String) doc.get("Correct");
        int ID = ((Long) doc.get("Question Number")).intValue();
        String userId = (String) doc.get("UserId");
        String username = (String) doc.get("Username");

        User user = new User(username, userId);

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
        q.setUserCreated(user);
    }

    /**
     * Converts documentSnapshot object to a quiz object
     *
     * @param doc the document to convert
     * @param q the quiz object which will hold the new values
     */
    private void docToQuiz(DocumentSnapshot doc, Quiz q) {
        //getting values
        UUID GlobalID = UUID.fromString((String) doc.get("GlobalID"));
        LocationQuizeo location = new LocationQuizeo((double) doc.get("Latitude"),
                (double) doc.get("Longitude"));
        String name = (String) doc.get("Name");
        int nrOfQuestions = ((Long) doc.get("Number of Questions")).intValue();
        int nrOfRatings = ((Long) doc.get("Number of Ratings")).intValue();
        int scoreToPass = ((Long) doc.get("Score to Pass")).intValue();
        double rating = (double) doc.get("Rating");
        int percentageToPass = ((Double) doc.get("Percentage to pass")).intValue();
        String userId = (String) doc.get("UserId");
        String username = (String) doc.get("Username");

        User user = new User(username, userId);

        //setting values
        q.setLocation(location);
        q.setQuizId(GlobalID);
        q.setQuizName(name);
        q.setNrOfRatings(nrOfRatings);
        q.setNumberOfQuestions(nrOfQuestions);
        q.setScoreToPass(scoreToPass);
        q.setRating(rating);
        q.setPercentageToPass(percentageToPass);
        q.setUserCreated(user);

    }
    //endregion

    //region -- UPLOAD --
    /**
     * Uploads a quiz and all its questions to the database
     *
     * @param quiz Quiz to uploaded
     * @param publish whether the quiz is published to the public
     */
    public void uploadQuiz(Quiz quiz, boolean publish) {
        // ----- Preparing for upload ------
        LocationQuizeo loc = quiz.getLocation(); //location of the quiz

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
        data.put("Percentage to pass", quiz.getPercentageToPass());
        data.put("Score to Pass", quiz.getScoreToPass());
        data.put("Latitude", loc.getLatitude());
        data.put("Longitude", loc.getLongitude());
        data.put("Questions", listOfQuestions);
        data.put("UserId", quiz.getUserCreated().getUserId());
        data.put("Username", quiz.getUserCreated().getNickName());

        // ------ upload quiz ------
        CollectionReference cr;
        if (publish) {
            cr = quizRef;
        } else {
            cr = notPublishedRef;
        }

        cr.document(quiz.getQuizId().toString())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("","Upload Success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("","Upload failure");
                    }
                });
    }

    /**
     * Upload a question to the database, question is part of multiple quizzes
     *
     * @param question question to be uploaded
     */
    public void uploadQuestion(Question question, Quiz quiz) {

        HashMap<String, Object> data = questionToMap(question, quiz);

        System.out.println("Starting upload");
        //upload to FireStore
        questionRef.document(question.getGlobalId().toString())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("","Upload Success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("", "Upload failure");
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

        ArrayList<String> ans = new ArrayList<>(Arrays.asList(question.getAnswers()));
        data.put("Answers", ans);

        return data;
    }

    /**
     * Upload a quiz result to the database
     *
     * @param user the user object of the user that made the quiz
     * @param quiz the quiz object of the quiz they made
     * @param passed whether they passed the quiz
     */
    public void addQuizToHistory(User user, Quiz quiz, Boolean passed) {
        addQuizToHistory(user.getUserId(), quiz.getQuizId(), quiz.getQuizName(), passed);
    }

    /**
     * Upload a quiz result to the database
     *
     * @param userId the id of the user that played the quiz
     * @param quizId the id of the quiz they made
     * @param quizName the name of the quiz they made
     * @param passed whether they passed the quiz
     */
    public void addQuizToHistory(String userId, UUID quizId, String quizName, Boolean passed) {
        HashMap<String, String> data = new HashMap<>();

        data.put("QuizID", quizId.toString());
        data.put("QuizName", quizName);
        data.put("Passed?", passed.toString());

        historyRef.document(userId).collection("Quizzes")
                .document(UUID.randomUUID().toString())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("","Upload Success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("", "Upload failure");
                    }
                });
    }


    //endregion

    //region -- DELETE --
    /**
     * Deletes quiz and all its questions from the database
     *
     * @param quiz quiz object you want to delete
     */
    public void removeQuiz(Quiz quiz) {
        Question[] questions = quiz.getQuestions();

        ArrayList<UUID> list = new ArrayList<>();
        for (Question q: questions) {
            list.add(q.getGlobalId());
        }
        removeQuiz(quiz.getQuizId(), list);
    }

    /**
     * Deletes quiz and all its questions from the database
     *
     * @param quizID The uuid of the quiz that you want to remove
     * @param questionIDs the uuids of the question belonging to that quiz
     */
    public void removeQuiz(UUID quizID, ArrayList<UUID> questionIDs) {
        for (UUID questID: questionIDs) {
            questionRef.document(questionIDs.toString()).delete();
        }
        quizRef.document(quizID.toString()).delete();
        notPublishedRef.document(quizID.toString()).delete();
    }


    /**
     * Removes a question reference from a quiz and deletes question object from the database
     *
     * @param question question to be removed
     * @param quiz quiz the question will be removed from
     */
    public void removeQuestionFromQuiz(Question question, Quiz quiz) {
        removeQuestionFromQuiz(question.getGlobalId(), quiz.getQuizId());
    }

    /**
     *  Removes a question reference from a quiz and deletes question object from the database
     *
     * @param uuidOfQuestion uuid of the question to be removed
     * @param uuidOfQuiz uuid of the quiz the question will be removed from
     */
    public void removeQuestionFromQuiz(UUID uuidOfQuestion, UUID uuidOfQuiz) {
        quizRef.document(uuidOfQuiz.toString()).
                update("Questions", FieldValue.arrayRemove(uuidOfQuestion));
        questionRef.document(uuidOfQuestion.toString()).delete();
    }

    /**
     * Removes the complete play history of a specific user
     *
     * @param user the user whose history is to be removed
     */
    public void removeHistory(User user) {
        removeHistory(user.getUserId());
    }

    /**
     * Removes the complete play history of a specific user
     *
     * @param userId the id of the user whose history is to be removed
     */
    public void removeHistory(String userId) {
        historyRef.document(userId).delete();
    }
    //endregion

    //region -- NEW ID --
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
        } while (uuidIsTaken(newID, questionRef) || uuidIsTaken(newID, quizRef) ||
                           uuidIsTaken(newID, notPublishedRef));
        return newID;
    }

    /**
     * Checks whether a given UUID is already taken in a certain collection
     *
     * @param uuid the id we want to check
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

    /** OnCompletionListener for get new ID task */
    private class newIdOnCompleteListener implements OnCompleteListener<QuerySnapshot> {

        @Override
        public void onComplete(@NonNull  Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                Database.getInstance().setIsTaken(!task.getResult().isEmpty());
            } else { //Not a nice way of handling failure
                Log.d("", "Query failed");
            }
        }
    }
    //endregion
}
