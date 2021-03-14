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

    int sound;
    int verified;
    int darkmode;
    int request;

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
        ImageView darkModeCheck = findViewById(R.id.darkModeCheck);
        if (i.getBooleanExtra("Sign", false)) {
            darkmode = darkModeCheck.getVisibility();
            intent.putExtra("darkmode", darkmode);
            intent.putExtra("Sign", false);
        } else {
            darkmode = i.getIntExtra("darkmode", View.INVISIBLE);
        }

        //ImageView darkModeCheck = findViewById(R.id.darkModeCheck);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            darkModeCheck.setVisibility(View.VISIBLE);
        } else {
            darkModeCheck.setVisibility(View.INVISIBLE);
        }

        ImageView soundIcon = findViewById(R.id.soundIcon);
        sound = soundIcon.getVisibility();

        ImageView verifiedCheck = findViewById(R.id.verifiedCheck);
        verified = verifiedCheck.getVisibility();

        ImageView requestCheck = findViewById(R.id.friendRequestCheck);
        request = requestCheck.getVisibility();
    }

    public void cancelOptions(View v) {
        // check if any options changed
        Intent i = new Intent(this, MainActivity.class);
        boolean change = false;
        if (findViewById(R.id.soundIcon).getVisibility() != sound) {
            alterSound(findViewById(R.id.muteButton));
        }
        if (findViewById(R.id.verifiedCheck).getVisibility() != verified) {
            toggleVerified(findViewById(R.id.verifiedButton));
        }
        if (findViewById(R.id.friendRequestCheck).getVisibility() != request) {
            toggleFriendRequests(findViewById(R.id.friendRequestButton));
        }
        if (findViewById(R.id.darkModeCheck).getVisibility() != darkmode) {
            changeDarkModeThenExit();
        }
        startActivity(i);
    }

    public void changeDarkModeThenExit() {
        Intent i = new Intent(this, OptionsActivity.class);
        i.putExtra("exit", true);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    public void saveOptions(View v) {
        // save current options

        // return to home screen
        openHome();
    }

    public void openHome() {
        Intent i = new Intent(this, MainActivity.class);
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
        if (enable) {
            //add code to enable

        } else {
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
        if (enable) {
            //add code to enable
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            // add code to disable
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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
        if (enable) {
            //add code to enable

        } else {
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
