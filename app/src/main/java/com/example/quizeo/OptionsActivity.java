package com.example.quizeo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

    boolean sound;
    boolean verified;
    boolean darkmode;
    boolean request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
        Intent i = getIntent();

        getOptions(i);
        if (!i.getBooleanExtra("darkmodeChange", false)) {
            if (sound) {
                unmute(findViewById(R.id.muteIcon));
            } else {
                mute(findViewById(R.id.soundIcon));
            }
            verifiedQuizzes(verified);
            friendRequests(request);
            darkMode(darkmode);
        } else {
            if (i.getBooleanExtra("soundNow", true)) {
                unmute(findViewById(R.id.muteIcon));
            } else {
                mute(findViewById(R.id.soundIcon));
            }
            verifiedQuizzes(i.getBooleanExtra("verifiedNow", true));
            friendRequests(i.getBooleanExtra("requestNow", false));
            darkMode(i.getBooleanExtra("darkmodeNow", false));

        }
    }

    @Override
   public void onBackPressed() {
        cancelOptions(findViewById(R.id.cancelButton));
    }

    public void getOptions(Intent i) {
        sound = i.getBooleanExtra("sound", true);

        verified = i.getBooleanExtra("verified", true);

        request = i.getBooleanExtra("request", false);

        darkmode = i.getBooleanExtra("darkmode", false);
    }

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
        openHome(sound, verified, darkmode, request);
    }

    public void alertUser (View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Changes are not saved").setPositiveButton("Quit anyway", dialogClickListener2)
                .setNegativeButton("Return to options", dialogClickListener2).show();
    }

    DialogInterface.OnClickListener dialogClickListener2 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    // Yes button clicked
                    changeOptionsExit();

                case DialogInterface.BUTTON_NEGATIVE:
                    // No button clicked
                    break;
            }
        }
    };

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
        // add code to actually mutes the app
        /*AudioManager audioManager = (AudioManager)OptionsActivity.this.getSystemService(Context.AUDIO_SERVICE);
        while (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) > 0) {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, 1);
        }*/
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
        /*AudioManager audioManager = (AudioManager)OptionsActivity.this.getSystemService(Context.AUDIO_SERVICE);
        while (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) < audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) {
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 1);
        }*/
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
        Intent in = getIntent();
        in.putExtra("darkmodeChange", true);
        in.putExtra("soundNow", findViewById(R.id.soundIcon).getVisibility() == View.VISIBLE);
        in.putExtra("verifiedNow", findViewById(R.id.verifiedCheck).getVisibility() == View.VISIBLE);
        in.putExtra("requestNow", findViewById(R.id.friendRequestCheck).getVisibility() == View.VISIBLE);
        if (enable) {
            i.setVisibility(View.VISIBLE);
            //add code to enable
            in.putExtra("darkmodeNow", true);
            findViewById(R.id.globeOptions).setVisibility(View.INVISIBLE);
            findViewById(R.id.globeOptionsDark).setVisibility(View.VISIBLE);
            if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        } else {
            i.setVisibility(View.INVISIBLE);
            // add code to disable
            in.putExtra("darkmodeNow", false);
            findViewById(R.id.globeOptionsDark).setVisibility(View.INVISIBLE);
            findViewById(R.id.globeOptions).setVisibility(View.VISIBLE);
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Do you want to quit?").setPositiveButton("Quit", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener).show();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    // Yes button clicked
                    finishAffinity();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    // No button clicked
                    break;
            }
        }
    };

}
