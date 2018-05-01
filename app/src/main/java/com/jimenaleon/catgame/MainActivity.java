package com.jimenaleon.catgame;

import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView scoreLabel;
    private TextView startLabel;
    private TextView nameLabel;
    private ImageView cat;
    private ImageView fishYellow;
    private ImageView fishBlue;
    private ImageView fishDead;
    private int posX;
    private int fishYellowX;
    private int fishYellowY;
    private int fishBlueX;
    private int fishBlueY;
    private int fishDeadX;
    private int fishDeadY;

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    private boolean action_flg = false;
    private boolean start_flg = false;

    private int frameWidth;
    private int catWidth;
    private int screenHeight;
    private int screenWidth;

    private int score = 0;

    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = getIntent().getStringExtra("NAME");
        nameLabel = (TextView) findViewById(R.id.nameLabel);
        nameLabel.setText("Bienvenido: " + name);

        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        startLabel = (TextView) findViewById(R.id.startLabel);
        cat = (ImageView) findViewById(R.id.cat);
        fishYellow = (ImageView) findViewById(R.id.fishYellow);
        fishBlue = (ImageView) findViewById(R.id.fishBlue);
        fishDead = (ImageView) findViewById(R.id.fishDead);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        fishYellow.setX(-100);
        fishYellow.setY(screenHeight + 100);
        fishBlue.setX(-300);
        fishBlue.setY(screenHeight + 100);
        fishDead.setX(-150);
        fishDead.setY(screenHeight + 100);

        scoreLabel.setText("Puntaje: 0");
    }

    public void changePos(){

        hitCheck();

        // yellow
        fishYellowY += 22;
        if(fishYellowY > screenHeight){
            fishYellowY = 0;
            fishYellowX = (int) Math.floor(Math.random() * (frameWidth - fishYellow.getWidth()));
        }
        fishYellow.setX(fishYellowX);
        fishYellow.setY(fishYellowY);

        // dead
        fishDeadY += 28;
        if(fishDeadY > screenHeight){
            fishDeadY = 0;
            fishDeadX = (int) Math.floor(Math.random() * (frameWidth - fishDead.getWidth()));
        }
        fishDead.setX(fishDeadX);
        fishDead.setY(fishDeadY);

        // blue
        fishBlueY += 35;
        if(fishBlueY > screenHeight){
            fishBlueY = -3530;
            fishBlueX = (int) Math.floor(Math.random() * (frameWidth - fishBlue.getWidth()));
        }
        fishBlue.setX(fishBlueX);
        fishBlue.setY(fishBlueY);


        if(action_flg){
            posX -= 20;
        }else{
            posX += 20;
        }

        if(posX < 0) posX = 0;
        if(posX > frameWidth - catWidth) posX = frameWidth - catWidth;

        cat.setX(posX);
        scoreLabel.setText("Puntaje: " + score);
    }

    public void hitCheck(){

        // yellow
        int fishYellowCenterX = fishYellowX + fishYellow.getWidth() / 2;
        int fishYellowCenterY = fishYellowY + fishYellow.getHeight() / 2;

        if(screenHeight - cat.getHeight() <= fishYellowCenterY + fishYellow.getHeight()
                && fishYellowY >= cat.getHeight() && posX <= fishYellowCenterX && fishYellowCenterX <= posX + catWidth){
            fishYellowY = screenHeight + 30;
            score += 15;
        }

        //blue
        int fishBlueCenterX = fishBlueX + fishBlue.getWidth() / 2;
        int fishBlueCenterY = fishBlueY + fishBlue.getHeight() / 2;

        if(screenHeight - cat.getHeight() <= fishBlueCenterY + fishBlue.getHeight()
                && fishBlueY >= cat.getHeight() && posX <= fishBlueCenterX && fishBlueCenterX <= posX + catWidth){

            fishBlueY = screenHeight + 20;
            score += 10;
        }

        // dead
        int fishDeadCenterX = fishDeadX + fishDead.getWidth() / 2;
        int fishDeadCenterY = fishDeadY + fishDead.getHeight() / 2;

        if(screenHeight - cat.getHeight() <= fishDeadCenterY + fishDead.getHeight()
                && fishDeadY >= cat.getHeight() && posX <= fishDeadCenterX && fishDeadCenterX <= posX + catWidth){

            //fishDeadY = screenHeight + 10;
            //score -= 10;
            timer.cancel();
            timer = null;
            goToFinish();
        }

    }

    private void goToFinish(){
        Intent intent = new Intent(getApplicationContext(), EndActivity.class);
        intent.putExtra("SCORE", score);
        startActivity(intent);
    }

    public boolean onTouchEvent(MotionEvent motionEvent){

        if(start_flg == false){
            start_flg = true;

            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameWidth = frame.getWidth();
            catWidth = cat.getWidth();
            posX = (int) cat.getX();

            startLabel.setVisibility(View.GONE);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20);

        }else{
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                action_flg = true;
            }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                action_flg = false;
            }
        }
        return true;
    }
}
