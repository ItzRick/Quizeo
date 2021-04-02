package com.example.quizeo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class OptionsActivity extends AppCompatActivity {

    // The default values for the options will be stored in these variables
    boolean sound;
    boolean verified;
    boolean darkmode;
    boolean request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        setContentView(R.layout.options);
        // get options from home activity
        getOptions(i);

        if (savedInstanceState == null) {       // if the activity is opened for the first time
            // set the options to the default options
            if (sound) {
                unmute(findViewById(R.id.muteIcon));
            } else {
                mute(findViewById(R.id.soundIcon));
            }
            verifiedQuizzes(verified);
            friendRequests(request);
            darkMode(darkmode);
        } else {                                // if the activity is reopened
            // set the options to the stored options
            if (i.getBooleanExtra("soundNow", sound)) {
                unmute(findViewById(R.id.muteIcon));
            } else {
                mute(findViewById(R.id.soundIcon));
            }
            verifiedQuizzes(i.getBooleanExtra("verifiedNow", verified));
            friendRequests(i.getBooleanExtra("requestNow", request));
            darkMode(i.getBooleanExtra("darkmodeNow", darkmode));
        }
    }

    @Override
    // Back button on device acts the same as cancel button
    public void onBackPressed() {
        cancelOptions(findViewById(R.id.cancelButton));
    }

    // Get the options set by the home activity
    public void getOptions(Intent i) {
        sound = i.getBooleanExtra("sound", true);
        verified = i.getBooleanExtra("verified", true);
        request = i.getBooleanExtra("request", false);
        darkmode = i.getBooleanExtra("darkmode", false);
    }

    // When the user pressed the cancel button, changes to the options are not saved
    public void cancelOptions(View v) {
        // check if any options changed
        boolean soundchange = (findViewById(R.id.soundIcon).getVisibility() == View.VISIBLE) != sound;
        boolean verifiedchange = (findViewById(R.id.verifiedCheck).getVisibility() == View.VISIBLE) != verified;
        boolean requestchange = (findViewById(R.id.friendRequestCheck).getVisibility() == View.VISIBLE) != request;
        boolean darkmodechange = (findViewById(R.id.darkModeCheck).getVisibility() == View.VISIBLE) != darkmode;
        // if any option changed
        if (soundchange || verifiedchange || requestchange || darkmodechange) {
            // warn user that changes are not saved
            alertUser(v);
        } else {    // if no options changed, return to home
            openHome(sound, verified, darkmode, request);
        }
    }

    // Change all options back to the default state and return to the home activity
    public void changeOptionsExit() {
        // change options back
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
        }
        // return to home activity
        openHome(sound, verified, darkmode, request);
    }

    // Alerts user that the changes are not saved
    public void alertUser (View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Changes are not saved").setPositiveButton("Quit anyway", dialogClickListener2)
                .setNegativeButton("Return to options", dialogClickListener2).show();
    }

    DialogInterface.OnClickListener dialogClickListener2 = (dialog, which) -> {
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:   // quit anyway button clicked
                // change options back and return to home activity
                changeOptionsExit();

            case DialogInterface.BUTTON_NEGATIVE:   // return to option button clicked
                // return to options activity
                break;
        }
    };

    // When the user pressed the save button, the changes are saved and the user returns to home
    public void saveOptions(View v) {
        // save current options
        boolean newSound = findViewById(R.id.soundIcon).getVisibility() == View.VISIBLE;
        boolean newVerified = findViewById(R.id.verifiedCheck).getVisibility() == View.VISIBLE;
        boolean newDarkmode = findViewById(R.id.darkModeCheck).getVisibility() == View.VISIBLE;
        boolean newRequest = findViewById(R.id.friendRequestCheck).getVisibility() == View.VISIBLE;
        // return to home activity
        openHome(newSound, newVerified, newDarkmode, newRequest);
    }

    // Reads the input options and sends them to the home activity
    public void openHome(boolean sound, boolean verified, boolean darkmode, boolean request) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("sound", sound);
        i.putExtra("verified", verified);
        i.putExtra("darkmode", darkmode);
        i.putExtra("request", request);
        startActivity(i);
    }

    // Checks whether sound is on or off and mutes or unmutes accordingly
    public void alterSound(View v) {
        // get the current text of the button
        String s = ((Button)v).getText().toString();
        if (s.equals("Mute Sounds")) {           // if the text says "Mute Sounds"
            // call mute method
            mute(this.findViewById(R.id.soundIcon));
        } else if (s.equals("Play Sounds")) {    // if the text says "Play Sounds"
            // call unmute method
            unmute(this.findViewById(R.id.muteIcon));
        }
    }

    // Mutes the application and shows the right text and image
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
        ((Button) this.findViewById(R.id.muteButton)).setText(R.string.play_sounds);
        // add code to actually mutes the app
        /*AudioManager audioManager = (AudioManager)OptionsActivity.this.getSystemService(Context.AUDIO_SERVICE);
        while (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) > 0) {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, 1);
        }*/
        // Store this option
        if (Music.isPlaying()) {
            Music.stop(this);
        }
        tempOptionSave("soundNow", false);
    }

    // Unmutes the application and shows the right text and image
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
        ((Button) this.findViewById(R.id.muteButton)).setText(R.string.mute_sounds);
        // add code to actually unmutes the app
        /*AudioManager audioManager = (AudioManager)OptionsActivity.this.getSystemService(Context.AUDIO_SERVICE);
        while (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) < audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 1);
        }*/
        // Store this option
        if (!Music.isPlaying()) {
            Music.play(this);
            Music m = new Music();
            m.start(this);
        }
        tempOptionSave("soundNow", true);
    }

    // Toggles whether only verified quizzes is on or off
    public void toggleVerified(View v) {
        // find the corresponding check mark
        ImageView i = this.findViewById(R.id.verifiedCheck);
        // call method that turns this options on or off based on the check mark
        verifiedQuizzes(!(i.getVisibility() == View.VISIBLE));
    }

    // Turns only verified quizzes on or off
    public void verifiedQuizzes(boolean enable) {
        // find the right check mark
        ImageView i = this.findViewById(R.id.verifiedCheck);
        if (enable) {       // if the options should be enabled
            // make the check mark visible
            i.setVisibility(View.VISIBLE);
            //add code to enable

        } else {            // if the options should be disabled
            // make the check mark invisible
            i.setVisibility(View.INVISIBLE);
            // add code to disable

        }
        // Store this option
        tempOptionSave("verifiedNow", enable);
    }

    // Toggles whether dark mode is on or off
    public void toggleDarkMode(View v) {
        // find the corresponding check mark
        ImageView i = this.findViewById(R.id.darkModeCheck);
        // call method that turns this options on or off based on the check mark
        darkMode(!(i.getVisibility() == View.VISIBLE));
    }

    // Turns dark mode on or off
    public void darkMode(boolean enable) {
        // find the right check mark
        ImageView i = this.findViewById(R.id.darkModeCheck);
        // store this option
        tempOptionSave("darkmodeNow", enable);
        if (enable) {       // if the options should be turned on
            // make the check mark visible
            i.setVisibility(View.VISIBLE);
            // change the background globe
            findViewById(R.id.globeOptions).setVisibility(View.INVISIBLE);
            findViewById(R.id.globeOptionsDark).setVisibility(View.VISIBLE);
            // if dark mode is not on, turn dark mode on
            if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        } else {            // if the option should be turned off
            // make the check mark invisible
            i.setVisibility(View.INVISIBLE);
            // change the background globe
            findViewById(R.id.globeOptionsDark).setVisibility(View.INVISIBLE);
            findViewById(R.id.globeOptions).setVisibility(View.VISIBLE);
            // if dark mode is turned on, turn dark mode off
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
    }

    // Toggles whether friend requests are on or off
    public void toggleFriendRequests(View v) {
        Music.next(this);
        Music m = new Music();
        m.start(this);
        // find the corresponding check mark
        ImageView i = this.findViewById(R.id.friendRequestCheck);
        // call method that turns this options on or off based on the check mark
        friendRequests(!(i.getVisibility() == View.VISIBLE));
    }

    // Turns friend requests on or off
    public void friendRequests(boolean enable) {
        // find the right check mark
        ImageView i = this.findViewById(R.id.friendRequestCheck);
        if (enable) {       // if the option should be turned on
            // make the check mark visible
            i.setVisibility(View.VISIBLE);
            //add code to enable

        } else {            // if the option should be turned off
            // make the check mark invisible
            i.setVisibility(View.INVISIBLE);
            // add code to disable

        }
        // Store this option
        tempOptionSave("requestNow", enable);
    }

    // Stores an option that has been changed in the intent
    public void tempOptionSave(String string, boolean enabled) {
        Intent i = getIntent();
        i.putExtra(string, enabled);
    }

    // Asks the user if they want to quit the application
    public void exitClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Do you want to quit?")
                .setPositiveButton("Quit", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener).show();
    }

    DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:       // Quit button clicked
                // close the application
                finishAffinity();
                break;

            case DialogInterface.BUTTON_NEGATIVE:       // Cancel button clicked
                // Return to options activity
                break;
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        // Stop the music
        Music.stop(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Resume music
        if (!Music.isPlaying() && (findViewById(R.id.soundIcon).getVisibility() == View.VISIBLE)) {
            Music.play(this);
        }
    }

}
