package cerdas.rth.web.id;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import helper.AudioPlayer;

public class BerhitungActivity extends AppCompatActivity {

    TextView txtSoal1, txtSoal2, textViewOperator;
    EditText jawabSoal;

    AudioPlayer adp, adp2;

    ImageView imageMonkeyExpression;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berhitung);

        imageMonkeyExpression = (ImageView) findViewById(R.id.imageMonkeyExpression);
        imageMonkeyExpression.setImageResource(0);

        textViewOperator = (TextView) findViewById(R.id.textViewOperator);
        txtSoal1 = (TextView) findViewById(R.id.textViewSoalA);
        txtSoal2 = (TextView) findViewById(R.id.textViewSoalB);

        jawabSoal = (EditText) findViewById(R.id.editTextJawaban);
        //jawabSoal.setTextDirection(View.TEXT_DIRECTION_RTL);
        generateRandomQuestion();

        adp = new AudioPlayer(this, false, AudioPlayer.GAME_OVER);
        adp2 = new AudioPlayer(this, false, AudioPlayer.WRONG);

        centerTitleApp();

    }

    private void centerTitleApp() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }

    private void shake(){

        txtSoal2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shaking));
        txtSoal1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shaking));
    }

    int max = 0, min, hasil;
    int operator_chosen;

    final int OPS_ADDITION = 1;

    // if 1 then +
    // if 2 then -
    private void generateRandomQuestion() {
        max = 87;
        min = 2;

        int ss1 = new Random().nextInt(max - min + 1) + min;
        int ss2 = new Random().nextInt(max - min + 1) + min;

        String soal1 = asNumberText(ss1);
        String soal2 = asNumberText(ss2);

        if (ss1 > ss2) {
            txtSoal1.setText(soal1);
            txtSoal2.setText(soal2);
        } else {
            txtSoal1.setText(soal2);
            txtSoal2.setText(soal1);
        }

        min = 1;
        max = 2;
         hasil = 0;
        operator_chosen = new Random().nextInt(max - min + 1) + min;

        if (operator_chosen == OPS_ADDITION) {
            hasil = ss1 + ss2;
            textViewOperator.setText("+");
        } else {
            textViewOperator.setText("-");
            if (ss1 > ss2)
                hasil = ss1 - ss2;

            if (ss2 > ss1)
                hasil = ss2 - ss1;
        }

    }

    public void checkAns(View v){
        if(jawabSoal.getText().toString().length()>0){
            String textna = jawabSoal.getText().toString();

            int h = Integer.parseInt(textna);

            // when the guessing is TRUE
            if (h == hasil){
                adp.play();
                imageMonkeyExpression.setImageResource(R.drawable.yeah);
                generateRandomQuestion();
                jawabSoal.setText("");
                hideMonkey();
            }else{
                // WHEN the guessing is WRONG
                shake();
                adp2.play();
                imageMonkeyExpression.setImageResource(R.drawable.no_words);
            }
        }
    }

    final int TIME_WAIT = 1000;
    private void hideMonkey(){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageMonkeyExpression.setImageResource(0);

            }
        }, TIME_WAIT * 3);
    }

    private String asNumberText(int val) {
        String res = "";

        if (val < 10) {
            // double space
            res = "  " + val;
        } else if (val >= 10 && val < 100) {
            // single space
            res = " " + val;
        } else {
            res = "" + val;
        }

        return res;
    }
}