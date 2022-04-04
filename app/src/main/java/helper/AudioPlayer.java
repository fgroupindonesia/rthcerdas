package helper;

import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;

import cerdas.rth.web.id.R;

public class AudioPlayer {

    MediaPlayer mediaPlayer;
    boolean alwaysLoop;

    public static final int SOUND_MUSIC = 1, BOING = 2, CLEAN_TRASH = 3,
            GAME_OVER = 4, WRONG = 5;

    private void selectTrack(AppCompatActivity act, int media) {
        if (media == SOUND_MUSIC) {
            mediaPlayer = MediaPlayer.create(act, R.raw.snack_green_orbs);
        } else if (media == BOING) {
            mediaPlayer = MediaPlayer.create(act, R.raw.boing);
        } else if (media == CLEAN_TRASH) {
            mediaPlayer = MediaPlayer.create(act, R.raw.out);
        } else if (media == GAME_OVER) {
            mediaPlayer = MediaPlayer.create(act, R.raw.game_over);
        }else if (media == WRONG) {
            mediaPlayer = MediaPlayer.create(act, R.raw.wrong_guess);
        }
    }

    public AudioPlayer(AppCompatActivity act, boolean b, int med) {
        alwaysLoop = b;

        this.selectTrack(act, med);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                if (alwaysLoop)
                    mediaPlayer.start();
            }

        });

    }

    public void play() {
        mediaPlayer.start();
    }

}
