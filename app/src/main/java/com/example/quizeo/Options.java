package com.example.quizeo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
    }

    public void cancelOptions(View v) {
        // check if any options changed

        // return to home screen
        openHome();
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
        if (enable) {  // only allow verified quizzes
            // add code
            Toast.makeText(getApplicationContext(),"Only verified quizzes", Toast.LENGTH_SHORT).show();
        } else {        // allow all quizzes
            // add code
            Toast.makeText(getApplicationContext(),"All quizzes", Toast.LENGTH_SHORT).show();
        }
    }

    public void toggleDarkMode(View v) {
        // find the corresponding check mark
        ImageView i = this.findViewById(R.id.darkModeCheck);
        // call method that shows or hides the check mark
        boolean enable = checkMark(i);
        if (enable) {  // enable dark mode
            // add code
            Toast.makeText(getApplicationContext(),"Enabled dark mode", Toast.LENGTH_SHORT).show();
        } else {        // disable dark mode
            // add code
            Toast.makeText(getApplicationContext(),"Disabled dark mode", Toast.LENGTH_SHORT).show();
        }
    }

    public void toggleFriendRequests(View v) {
        // find the corresponding check mark
        ImageView i = this.findViewById(R.id.friendRequestCheck);
        // call method that shows or hides the check mark
        boolean enable = checkMark(i);
        if (enable) {  // enable friend requests
            // add code
            Toast.makeText(getApplicationContext(),"Enabled friend requests", Toast.LENGTH_SHORT).show();
        } else {        // disable friend requests
            // add code
            Toast.makeText(getApplicationContext(),"Disabled friend requests", Toast.LENGTH_SHORT).show();
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
