package cerdas.rth.web.id;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import helper.OnSwipeListener;

public class WelcomeActivity extends AppCompatActivity {

    final int WAITING_TIME = 2000; // 2seconds
    ImageView imageViewLogoRTH;

    Handler handler = new Handler();

    final int MOVE_RIGHT = 1, MOVE_LEFT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        imageViewLogoRTH = (ImageView) findViewById(R.id.imageViewLogoRTH);

        applySwapListener();

        blinking();

        centerTitleApp();
        requestPermission();

    }

    private void blinking(){
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blinking);
        imageViewLogoRTH.startAnimation(animation);
    }

    private void applySwapListener(){
        imageViewLogoRTH .setOnTouchListener(new OnSwipeListener(WelcomeActivity.this) {
            public void onSwipeTop() {
            }
            public void onSwipeRight() {
                swapAnim(MOVE_RIGHT);
            }
            public void onSwipeLeft() {
                swapAnim(MOVE_LEFT);
            }
            public void onSwipeBottom() {
            }
        });
    }

    private void swapAnim(int area){

        Animation animation = null;

        if(area == MOVE_LEFT){
            animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.movement_left);
        }else {
            animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.movement_right);
        }

        imageViewLogoRTH.startAnimation(animation);

    }

    private void centerTitleApp(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }

    private boolean gotPermission(){
        return hasPermissions(this, PERMISSIONS);
    }

    private void proceed(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(gotPermission()){
                    gotoAnotherActivity();
                }else{
                    finish();
                }
            }
        }, WAITING_TIME);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_ALL) {
            proceed();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void gotoAnotherActivity(){

            Intent intent = new Intent(this, BerhitungActivity.class);
            startActivity(intent);

        finish();
    }

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private void requestPermission(){

        if (!gotPermission()) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }else{
            proceed();
        }


    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }



}