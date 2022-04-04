package cerdas.rth.web.id;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Random;

import helper.AudioPlayer;
import helper.ShowDialog;

public class GeserActivity extends AppCompatActivity {

    private ViewGroup mainLayout;
    private ImageView imageMonkeyGirl, imageArrow,
            imageMonkey, imageBanana, imageGinger,
            imageCarrot, imageTrash, imageMonkeyBoth;

    private int xDelta;
    private int yDelta;

    TextView textViewInfo, textViewMessage, textViewInfo2;

    AudioPlayer mp3Engine, mp3FX, mp3Trash;
    boolean fullTrash = false;

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    public void clearTrash(View v){

        String tag = v.getTag().toString();

        if(tag.equalsIgnoreCase("full")){

            fullTrash = !fullTrash;

            if(fullTrash==false){
                imageTrash.setImageResource(R.drawable.trash_empty);
                textViewMessage.setText("Trash cleared....");
                imageTrash.setTag("");
            }

            mp3Trash.play();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_geser);

        requestPermission();

        textViewMessage = (TextView) findViewById(R.id.textViewMessage);

        mp3Engine = new AudioPlayer(this, true, AudioPlayer.SOUND_MUSIC);
        mp3Engine.play();

        mp3FX = new AudioPlayer(this, false, AudioPlayer.BOING);
        mp3Trash = new AudioPlayer(this, false, AudioPlayer.CLEAN_TRASH);

        textViewInfo = (TextView) findViewById(R.id.textViewInfo);
        textViewInfo2 = (TextView) findViewById(R.id.textViewInfo2);
        mainLayout = (RelativeLayout) findViewById(R.id.main);
        imageMonkey = (ImageView) findViewById(R.id.imageMonkey);

        imageMonkeyBoth = (ImageView) findViewById(R.id.imageMonkeyBoth);
        // first time no motor bike shown
        imageMonkeyBoth.setImageResource(0);

        imageBanana = (ImageView) findViewById(R.id.imageBanana);
        imageCarrot = (ImageView) findViewById(R.id.imageCarrot);
        imageGinger = (ImageView) findViewById(R.id.imageGinger);
        imageArrow = (ImageView) findViewById(R.id.imageArrow);

        imageTrash = (ImageView) findViewById(R.id.imageTrash);

        imageMonkeyGirl = (ImageView) findViewById(R.id.imageMonkeyGirl);
        // first time no girl shown
        imageMonkeyGirl.setImageResource(0);


        imageBanana.setOnTouchListener(onTouchListener());
        imageCarrot.setOnTouchListener(onTouchListener());
        imageGinger.setOnTouchListener(onTouchListener());

        centerTitleApp();

    }

    private void centerTitleApp() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }

    private void thrown(View v) {

        int lTrash = imageTrash.getLeft();
        int tTrash = imageTrash.getTop();

        int wTrash = imageTrash.getWidth();
        int hTrash = imageTrash.getHeight();


        int lView = v.getLeft();
        int tView = v.getTop();

        int wView = v.getWidth();
        int hView = v.getHeight();

        // we can throw only if the trashcan is empty
        if(!fullTrash) {

            if (lView >= lTrash - 30 && lView <= lTrash + wTrash &&
                    tView >= tTrash - 30 && tView <= tTrash + hTrash) {
                //ShowDialog.message(this, "placed!");

                // for later clearing usage
                imageTrash.setTag("full");
                fullTrash = true;


                imageTrash.setImageResource(R.drawable.trash_full);

                // temporarily hide
                v.setVisibility(View.INVISIBLE);

                comment("thrown... and comes a new one");

                resetBackImage(v);

            }
        }

        textViewInfo2.setText("trash l : " + lTrash + ", t :" + tTrash + ", width : " + wTrash + ", height :" + hTrash +
                "\nObject l :" + lView + ", t : " + tView + ", width : " + wView + ", height : " + hView);


    }

    private void given(View v) {


        int lMonkey = imageMonkey.getLeft();
        int tMonkey = imageMonkey.getTop();

        int wMonkey = imageMonkey.getWidth();
        int hMonkey = imageMonkey.getHeight();

        int lView = v.getLeft();
        int tView = v.getTop();

        int wView = v.getWidth();
        int hView = v.getHeight();

        if (lView >= lMonkey && lView <= lMonkey + wMonkey &&
                tView >= tMonkey && tView <= tMonkey + hMonkey) {
            //ShowDialog.message(this, "placed!");

            imageMonkey.setImageResource(R.drawable.eating);

            // check the stomatch after 3 seconds
            finishedEating(v);

            // temporarily hide
            v.setVisibility(View.INVISIBLE);

            comment("hope it's okay...");
        } else {
            // check whether this is thrown to trash?
            thrown(v);
        }

        textViewInfo.setText("monkey l : " + lMonkey + ", t :" + tMonkey + ", width : " + wMonkey + ", height :" + hMonkey +
                "\nObject l :" + lView + ", t : " + tView + ", width : " + wView + ", height : " + hView);

    }

    private void resetBackImage(View view) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                .getLayoutParams();
        layoutParams.leftMargin = 0;
        layoutParams.topMargin = 0;
        layoutParams.rightMargin = 0;
        layoutParams.bottomMargin = 0;
        view.setLayoutParams(layoutParams);

        setRandomFruit(view);
    }

    private void setRandomFruit(View v) {

        int fruitsList[] = {
                R.drawable.ginger48,
                R.drawable.banana48,
                R.drawable.carrot48,
                R.drawable.onion48,
                R.drawable.mushroom48
        };

        int max = fruitsList.length - 1;
        int min = 0;

        ImageView vImage = (ImageView) v;
        int imagePost = new Random().nextInt(max - min + 1) + min;
        vImage.setImageResource(fruitsList[imagePost]);
        vImage.setTag(makeTag(imagePost));
        vImage.setVisibility(View.VISIBLE);

    }

    private String makeTag(int num) {
        String name = "";
        // this is based on the array as fruitsList
        switch (num) {
            case 0:
                name = "ginger";
                break;
            case 1:
                name = "banana";
                break;
            case 2:
                name = "carrot";
                break;
            case 3:
                name = "onion";
                break;
            case 4:
                name = "mushroom";
                break;
        }

        return name;
    }


    int score = 0;
    String safeFoods[] = {"banana", "carrot"};

    private boolean isItSafe(View v) {
        // check the food is okay?
        // if so count the POSITIVE ++
        // if not count the POSITIVE --
        ImageView im = (ImageView) v;
        ShowDialog.message(this, "this is " + im.getTag());

        boolean found = false;

        for (String buah : safeFoods) {
            if (im.getTag().toString().equalsIgnoreCase(buah)) {

                found = true;
                break;
            }
        }

        return found;
    }

    public void helpHim(View v) {
        if (textViewMessage.getText().toString().contains("help")) {
            // we deliver the girl to come...
            imageMonkeyGirl.setImageResource(R.drawable.girl_come);

            // be prepared
            prepareGirl();

            score--;
            score--;
            renderAsScore();
        }
    }

    private void goAwayWithMotorbike() {
        imageMonkeyBoth.setImageResource(R.drawable.motorbike_couple);
        imageMonkeyGirl.setImageResource(0);
        imageMonkey.setImageResource(0);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.movement_right);
        imageMonkeyBoth.startAnimation(animation);

        textViewMessage.setText("everything is nice when loves come!\nThanks for playing...");
        // get back the score final score
        score = 9;

    }

    private void walkToHim() {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageMonkeyGirl.setImageResource(R.drawable.girl_walk);

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.movement_left);
                imageMonkeyGirl.startAnimation(animation);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        goAwayWithMotorbike();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        }, TIME_WAIT * 2);
    }

    private void prepareGirl() {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageMonkeyGirl.setImageResource(R.drawable.girl_prepare);
                walkToHim();
            }
        }, TIME_WAIT * 2);
    }

    final int TIME_WAIT = 2000; // 2 seconds

    private void finishedEating(final View v) {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isItSafe(v)) {
                    score++;

                    if (score > 3) {
                        comment("he loves it!");
                    } else {
                        comment("hmm...okay");
                    }

                } else {
                    score--;
                    if (score < 0 && score >= -2) {
                        comment("he doesn't like it.");
                        mp3FX.play();
                    } else if (score < -2) {
                        comment("he needs help!");
                        mp3FX.play();
                    }
                }

                resetBackImage(v);
                renderAsScore();
            }
        }, TIME_WAIT);
    }

    private void comment(String message) {
        textViewMessage.setText(message);
    }

    private void renderAsScore() {
        if (score >= 0 && score <= 3) {
            imageMonkey.setImageResource(R.drawable.happy_waiting);
        } else if (score < 0 && score >= -2) {
            imageMonkey.setImageResource(R.drawable.no_comments);
        } else if (score > 3) {
            imageMonkey.setImageResource(R.drawable.very_happy);
        } else if (score < -2 && score >= -4) {
            imageMonkey.setImageResource(R.drawable.sickness);
        } else if (score < -4) {
            imageMonkey.setImageResource(R.drawable.very_sick);
        }
    }

    private OnTouchListener onTouchListener() {
        return new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)
                                view.getLayoutParams();

                        xDelta = x - lParams.leftMargin;
                        yDelta = y - lParams.topMargin;

                        comment("he wants a food... right?");
                        imageMonkey.setImageResource(R.drawable.shining);
                        imageArrow.setVisibility(View.GONE);
                        break;

                    case MotionEvent.ACTION_UP:
                        // ShowDialog.message(GeserActivity.this, "released!");

                        // check is the image located on the same cordinate as monkey?
                        comment("let see...");
                        renderAsScore();
                        given(view);

                        imageArrow.setVisibility(View.VISIBLE);

                        break;

                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                                .getLayoutParams();
                        layoutParams.leftMargin = x - xDelta;
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        view.setLayoutParams(layoutParams);
                        break;
                }

                mainLayout.invalidate();
                return true;
            }
        };
    }
}