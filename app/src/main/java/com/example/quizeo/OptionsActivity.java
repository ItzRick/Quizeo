package com.example.quizeo;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class OptionsActivity extends AppCompatActivity {

    boolean sound;
    boolean verified;
    boolean darkmode;
    boolean request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
        Intent intent = new Intent(this, OptionsActivity.class);
        Intent i = getIntent();

        if (i.getBooleanExtra("exit", false)) {
            Intent intent2 = new Intent(this, MainActivity.class);
            startActivity(intent2);
        }

        getOptions(i);

        ImageView darkModeCheck = findViewById(R.id.darkModeCheck);
        /*if (i.getBooleanExtra("Sign", false)) {
            darkmode = darkModeCheck.getVisibility() == View.VISIBLE;
            intent.putExtra("darkmode", darkmode);
            intent.putExtra("Sign", false);
        } else {
            darkmode = i.getBooleanExtra("darkmode", false);
        }*/

        /*if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            Log.d("lol", "HEY");
            darkModeCheck.setVisibility(View.VISIBLE);
        } else {
            darkModeCheck.setVisibility(View.INVISIBLE);
        }*/

    }

    public void getOptions(Intent i) {
        sound = i.getBooleanExtra("sound", true);
        if (sound) {
            unmute(findViewById(R.id.muteIcon));
        } else {
            mute(findViewById(R.id.soundIcon));
        }
        verified = i.getBooleanExtra("verified", true);
        verifiedQuizzes(verified);
        request = i.getBooleanExtra("request", false);
        friendRequests(request);
        darkmode = i.getBooleanExtra("darkmode", false);
        darkMode(darkmode);
    }

    public void cancelOptions(View v) {
        // check if any options changed
        Intent i = new Intent(this, MainActivity.class);
        boolean change = false;
        if ((findViewById(R.id.soundIcon).getVisibility() == View.VISIBLE) != sound) {
            alterSound(findViewById(R.id.muteButton));
        }
        if ((findViewById(R.id.verifiedCheck).getVisibility() == View.VISIBLE) != verified) {
            toggleVerified(findViewById(R.id.verifiedButton));
        }
        if ((findViewById(R.id.friendRequestCheck).getVisibility() == View.VISIBLE) != request) {
            toggleFriendRequests(findViewById(R.id.friendRequestButton));
        }
        if ((findViewById(R.id.darkModeCheck).getVisibility() == View.VISIBLE) != darkmode) {
            toggleDarkMode(findViewById(R.id.darkModeButton));
            //changeDarkModeThenExit();
        }

        openHome(sound, verified, darkmode, request);
    }

    public void changeDarkModeThenExit() {
        Intent i = new Intent(this, OptionsActivity.class);
        i.putExtra("exit", true);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    public void saveOptions(View v) {
        // save current options
        boolean newSound = findViewById(R.id.soundIcon).getVisibility() == View.VISIBLE;
        boolean newVerified = findViewById(R.id.verifiedCheck).getVisibility() == View.VISIBLE;
        boolean newDarkmode = findViewById(R.id.darkModeCheck).getVisibility() == View.VISIBLE;
        boolean newRequest = findViewById(R.id.friendRequestCheck).getVisibility() == View.VISIBLE;
        // return to home screen
        openHome(newSound, newVerified, newDarkmode, newRequest);
    }

    public void openHome(boolean sound, boolean verified, boolean darkmode, boolean request) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("sound", sound);
        i.putExtra("verified", verified);
        i.putExtra("darkmode", darkmode);
        i.putExtra("request", request);
        startActivity(i);
    }

    public void alterSound(View v) {
        // get the current text of the button
        String s = ((Button)v).getText().toString();
        // if the text says "Mute Sounds", mute, otherwise unmute
        if (s.equals("Mute Sounds")) {
            mute(this.findViewById(R.id.soundIcon));
        } else if (s.equals("Play Sounds")) {
            unmute(this.findViewById(R.id.muteIcon));
        }
    }

    public void mute(View v) {
        // hide sound icon
        ImageView sound = (ImageView) v;
        sound.setVisibility(View.INVISIBLE);
        sound.setClickable(false);
        sound.setFocusable(false);
        // show mute button
        ImageView mute = this.findViewById(R.id.muteIcon);
        mute.setVisibility(View.VISIBLE);
        mute.setClickable(true);
        mute.setFocusable(true);
        // change button text
        ((Button) this.findViewById(R.id.muteButton)).setText("Play Sounds");
        // add code to actually mutez the app

    }

    public void unmute(View v) {
        // hide mute icon
        ImageView mute = (ImageView) v;
        mute.setVisibility(View.INVISIBLE);
        mute.setClickable(false);
        mute.setFocusable(false);
        // show sound icon
        ImageView sound = this.findViewById(R.id.soundIcon);
        sound.setVisibility(View.VISIBLE);
        sound.setClickable(true);
        sound.setFocusable(true);
        // change button text
        ((Button) this.findViewById(R.id.muteButton)).setText("Mute Sounds");
        // add code to actually unmutes the app

    }

    public void toggleVerified(View v) {
        // find the corresponding check mark
        ImageView i = this.findViewById(R.id.verifiedCheck);
        // call method that shows or hides the check mark
        boolean enable = checkMark(i);
        verifiedQuizzes(enable);
    }

    public void verifiedQuizzes(boolean enable) {
        ImageView i = this.findViewById(R.id.verifiedCheck);
        if (enable) {
            i.setVisibility(View.VISIBLE);
            //add code to enable

        } else {
            i.setVisibility(View.INVISIBLE);
            // add code to disable

        }
    }

    public void toggleDarkMode(View v) {
        // find the corresponding check mark
        ImageView i = this.findViewById(R.id.darkModeCheck);
        // call method that shows or hides the check mark
        boolean enable = checkMark(i);
        darkMode(enable);
    }

    public void darkMode(boolean enable) {
        ImageView i = this.findViewById(R.id.darkModeCheck);
        if (enable) {
            i.setVisibility(View.VISIBLE);
            //add code to enable
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            i.setVisibility(View.INVISIBLE);
            // add code to disable
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void toggleFriendRequests(View v) {
        // find the corresponding check mark
        ImageView i = this.findViewById(R.id.friendRequestCheck);
        // call method that shows or hides the check mark
        boolean enable = checkMark(i);
        friendRequests(enable);
    }

    public void friendRequests(boolean enable) {
        ImageView i = this.findViewById(R.id.friendRequestCheck);
        if (enable) {
            i.setVisibility(View.VISIBLE);
            //add code to enable

        } else {
            i.setVisibility(View.INVISIBLE);
            // add code to disable

        }
    }

    public boolean checkMark(ImageView i) {
        if (i.getVisibility() == View.VISIBLE) {
            i.setVisibility(View.INVISIBLE);
            return false;
        } else {
            i.setVisibility(View.VISIBLE);
            return true;
        }
    }

    public void exitClick(View v) {
        System.exit(0);
    }

}
