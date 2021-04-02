package com.example.quizeo;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;

//Authentication singleton class
//Used to authenticate the user
public class Authentication {

    /** Firebase Authenticator instance */
    private FirebaseAuth mAuth;

    /** Holds the Authentication singleton instance */
    private static Authentication instance;

    /** current user as User object */
    private User currentUser;

    /** Constructor, should only be called from getInstance method */
    private Authentication() {
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Returns the Authentication instance
     *
     * @return Authentication signleton object
     */
    public static Authentication getInstance() {
        if (instance == null) {
            instance = new Authentication();
        }
        return instance;
    }

    /**
     * Sign the user in using an anonymous token
     *
     * @param activity the current activity
     */
    public void loginAnonymously(AppCompatActivity activity) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.signInAnonymously()
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "signInAnonymously:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "signInAnonymously:failure", task.getException());
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    /**
     * Returns the currentUser and makes sure it is indeed the currentUser
     *
     * @return the currentUser
     */
    public User getCurrentUser() {
        if (currentUser.getUserId() != mAuth.getCurrentUser().getUid()) {
            convertToUserObject();
        }
        return currentUser;
    }


    /**
     * Set the current user
     *
     * @param user the new user
     */
    private void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * Sets the currentUser variable to the currentUser used by firebase authentication
     */
    private void convertToUserObject() {
        currentUser = new User(mAuth.getCurrentUser().getDisplayName(), mAuth.getCurrentUser().getUid());
    }

    /**
     * Will update the nick name of the user in the database
     *
     * @param user User object with the new information
     */
    public void updateUser(User user) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(user.getNickName())
                .build();

        mAuth.getCurrentUser().updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("UpdateProfile", "User profile updated.");
                        }
                    }
                });

    }

    /**
     * Removes all data of the current user form the database
     * INCLUDING Questions and Quizzes they made and their play history!
     */
    public void removeCurrentUserData() {
        Database database = Database.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            database.removeHistory(user.getUid());
            database.getQuizzes(user.getUid(), true, new Database.DownloadQuizzesCallback() {
                @Override
                public void onCallback(ArrayList<Quiz> list) {
                    for (Quiz quiz : list) {
                        database.removeQuiz(quiz);
                    }
                    user.delete();
                    FirebaseAuth.getInstance().signOut();
                }
            });
        }
    }
}



