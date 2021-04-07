package com.example.quizeo;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Music {

    // TURN SOUND OFF FOR TEST PURPOSES
    private static final boolean sound = false;
    public static boolean sfx = true;
    // List of soundtracks
    private static final List<Integer> music = new ArrayList<>();
    // Variables used for selecting and playing music
    private static int count = 0;
    private static int size = 0;
    private static int pos = 0;
    private Context c;
    private static boolean play;
    // MediaPlayer for music
    private static MediaPlayer mp = null;
    // MediaPlayer for sound effects
    private static MediaPlayer effects = null;
    // Maximum volume for the music and sound effects, do not change
    private final static int MAX_VOLUME = 100;
    // Set volume for the music and sound effects, can change
    public static int soundVolume = 50;
    public static int effectsVolume = 100;
    // Converted volume
    private static float volume = (float) (1 - (Math.log(MAX_VOLUME - soundVolume) / Math.log(MAX_VOLUME)));
    private static float sfxVolume = (float) (1 - (Math.log(MAX_VOLUME - soundVolume) / Math.log(MAX_VOLUME)));

    private final Runnable r = new Runnable() {

        @Override
        public void run() {
            play = true;
            while (isPlaying()) {
            }
            if (play) {
                next(c);
                start(c);
            }
        }
    };

    public void start(Context context) {
        c = context;
        Thread t = new Thread(r);
        t.start();
    }

    // Puts all the soundtracks in the ArrayList
    public static void instantiate() {
        // ADD MUSIC HERE
        /*  Happy Commercial by MaxKoMusic | https://maxkomusic.com/
        Music promoted by https://www.chosic.com
        Creative Commons Attribution-ShareAlike 3.0 Unported
        https://creativecommons.org/licenses/by-sa/3.0/deed.en_US   */
        music.add(R.raw.soundtrack01);

        /*  Inspiring Optimistic Upbeat Energetic Guitar Rhythm by Free Music | https://soundcloud.com/fm_freemusic
        Music promoted by https://www.chosic.com/
        Creative Commons Attribution 3.0 Unported License
        https://creativecommons.org/licenses/by/3.0/deed.en_US  */
        music.add(R.raw.soundtrack02);

        /*  Your Lucky Day by Free Music | https://soundcloud.com/fm_freemusic
        Music promoted by https://www.chosic.com/
        Creative Commons Attribution 3.0 Unported License
        https://creativecommons.org/licenses/by/3.0/deed.en_US  */
        music.add(R.raw.soundtrack03);

        /*  Sweet Dreams by BatchBug | https://soundcloud.com/batchbug
        Music promoted by https://www.chosic.com
        Creative Commons Attribution 3.0 Unported License
        https://creativecommons.org/licenses/by/3.0/deed.en_US  */
        music.add(R.raw.soundtrack04);

        //music.add(R.raw.test);
        size = music.size();
        setVolume(soundVolume);
    }

    // Calls playNext with current music from soundtrack
    public static void play(Context context) {
        if (!music.isEmpty()) {
            playNext(context, music.get(count));
        }
    }

    // Plays music that is provided in parameter resource
    private static void playNext(Context context, int resource) {
        if (sound) {
            mp = MediaPlayer.create(context, resource);
            mp.setVolume(volume, volume);
            //mp.setLooping(true);
            mp.start();
            mp.seekTo(pos);
        }
    }

    // Stops music that is playing
    public static void stop(Context context) {
        play = false;
        if (mp != null) {
            mp.pause();
            pos = mp.getCurrentPosition();
            mp.stop();
        }
    }

    // Returns whether music is currently playing
    public static boolean isPlaying() {
        if (mp == null) {
            return false;
        }
        return mp.isPlaying();
    }

    // Plays the next music in soundtrack
    public static void next(Context context) {
        play = false;
        if (!music.isEmpty()) {
            count = (count + 1) % size;
            stop(context);
            pos = 0;
            if (count == 2) {
                setVolume(70);
            } else if (count == 3) {
                setVolume(65);
            } else {
                setVolume(50);
            }
            playNext(context, music.get(count));
        }
    }

    public static void random(Context context) {
        if (!music.isEmpty()) {
            Random r = new Random();
            count = r.nextInt(size);
            next(context);
        }
    }

    // Sets the volume to the input integer
    public static void setVolume(float newVolume) {
        if (newVolume >= 0 && newVolume <= 100) {
            volume = (float) (1 - (Math.log(MAX_VOLUME - newVolume) / Math.log(MAX_VOLUME)));
        }
    }

    // Stores the sound that plays when a user correctly answers a question
    public static void correctSound(Context context) {
        effects = MediaPlayer.create(context, R.raw.soundeffect02);
        // Play the sound
        playSound(95);
    }

    // Stores the sound that plays when a user incorrectly answers a question
    public static void wrongSound(Context context) {
        effects = MediaPlayer.create(context, R.raw.soundeffect01);
        // Play the sound
        playSound(100);;
    }

    // Stores the sound that plays when a user passed a quiz
    public static void passedSound(Context context) {
        effects = MediaPlayer.create(context, R.raw.soundeffect03);
        // Play the sound
        playSound(100);
    }

    // Stores the sound that plays when a user failed a quiz
    public static void failedSound(Context context) {
        effects = MediaPlayer.create(context, R.raw.soundeffect04);
        // Play the sound
        playSound(100);
    }

    // Plays the sound that is currently stored
    private static void playSound(float volumeSet) {
        if (sfx) {
            setSfxVolume(volumeSet);
            effects.setVolume(sfxVolume, sfxVolume);
            effects.start();
        }
    }

    // Sets the volume to the input integer
    public static void setSfxVolume(float newVolume) {
        if (newVolume >= 0 && newVolume <= 100) {
            sfxVolume = (float) (1 - (Math.log(MAX_VOLUME - newVolume) / Math.log(MAX_VOLUME)));
        }
    }
}
