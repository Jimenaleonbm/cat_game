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
import android.widget.Switch;
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
    private ImageView broc;
    private ImageView mouse;
    private int posX;
    private int fishYellowX;
    private int fishYellowY;
    private int fishBlueX;
    private int fishBlueY;
    private int brocX;
    private int brocY;
    private int mouseX;
    private int mouseY;
    int speed1;
    int speed2;
    int speed3;
    int speed4;

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
        broc = (ImageView) findViewById(R.id.broc);
        mouse = (ImageView) findViewById(R.id.mouse);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        fishYellow.setX(100);
        fishYellow.setY(screenHeight + 100);
        fishBlue.setX(300);
        fishBlue.setY(screenHeight + 100);
        broc.setX(150);
        broc.setY(screenHeight + 100);
        mouse.setX(150);
        mouse.setY(screenHeight + 100);

        scoreLabel.setText("Puntaje: 0");
    }

    public int chagenSpeed(int speed){
        if(score < 50){
            speed = (int) (speed * 1.6);
        }else if(score >= 50 && score <= 100){
            speed = (int) (speed * 2.1);
        }else if(score > 100 && score <= 400){
            speed = (int) (speed * 2.8);
        }else if(score > 400){
            speed = (int) (speed * 3.5);
        }

        return speed;
    }

    public void changePos(){

        speed1 = chagenSpeed(10);
        speed2 = chagenSpeed(9);
        speed3 = chagenSpeed(12);
        speed4 = chagenSpeed(11);

        hitCheck();

        // yellow
        fishYellowY += speed1;
        if(fishYellowY > screenHeight){
            fishYellowY = 0;
            fishYellowX = (int) Math.floor(Math.random() * (frameWidth - fishYellow.getWidth()));
        }
        fishYellow.setX(fishYellowX);
        fishYellow.setY(fishYellowY);

        // brocoli
        brocY += speed3;
        if(brocY > screenHeight){
            brocY = 0;
            brocX = (int) Math.floor(Math.random() * (frameWidth - broc.getWidth()));
        }
        broc.setX(brocX);
        broc.setY(brocY);

        // blue
        fishBlueY += speed2;
        if(fishBlueY > screenHeight){
            fishBlueY = -3530;
            fishBlueX = (int) Math.floor(Math.random() * (frameWidth - fishBlue.getWidth()));
        }
        fishBlue.setX(fishBlueX);
        fishBlue.setY(fishBlueY);

        // mouse
        mouseY += speed4;
        if(mouseY > screenHeight){
            mouseY = -6521;
            mouseX = (int) Math.floor(Math.random() * (frameWidth - mouse.getWidth()));
        }
        mouse.setX(mouseX);
        mouse.setY(mouseY);

        /*
        if(action_flg){
            posX -= 20;
        }else{
            posX += 20;
        }
        */

        if(posX < 0) posX = 0;
        if(posX > frameWidth - catWidth) posX = frameWidth - catWidth;

        cat.setX(posX);
        scoreLabel.setText("Puntaje: " + score);
    }

    public void hitCheck(){

        // yellow

        int fishYellowCenterX = fishYellowX + fishYellow.getWidth() / 2;
        int fishYellowCenterY = fishYellowY + fishYellow.getHeight() / 2;

        if(posX <= fishYellowCenterX && fishYellowCenterX <= posX + catWidth
                && fishYellowCenterY >= cat.getY() &&
                fishYellowY + fishYellow.getHeight() < cat.getY() + cat.getHeight()) {

                fishYellowY = screenHeight + 30;
                score += 15;
        }

        //blue

        int fishBlueCenterX = fishBlueX + fishBlue.getWidth() / 2;
        int fishBlueCenterY = fishBlueY + fishBlue.getHeight() / 2;

        if(posX <= fishBlueCenterX && fishBlueCenterX <= posX + catWidth
                && fishBlueCenterY >= cat.getY() &&
                fishBlueY + fishBlue.getHeight() < cat.getY() + cat.getHeight()) {

            fishBlueY = screenHeight + 0;
            score += 10;
        }


        // broc
        int brocCenterX = brocX + broc.getWidth() / 2;
        int brocCenterY = brocY + broc.getHeight() / 2;

        if(posX <= brocCenterX && brocCenterX <= posX + catWidth
                && brocCenterY >= cat.getY() &&
                brocY + broc.getHeight() < cat.getY() + cat.getHeight()){

            timer.cancel();
            timer = null;
            goToFinish();
        }


        // mouse

        int mouseCenterX = mouseX + mouse.getWidth() / 2;
        int mouseCenterY = mouseY + mouse.getHeight() / 2;

        if(posX <= mouseCenterX && mouseCenterX <= posX + catWidth
                && mouseCenterY >= cat.getY() &&
                mouseY + mouse.getHeight() < cat.getY() + cat.getHeight()) {

            mouseY = screenHeight + 20;
            score += 30;
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

            switch (motionEvent.getAction()){

                case MotionEvent.ACTION_DOWN:
                    posX = (int) motionEvent.getX() - (catWidth / 2);
                    break;

                case MotionEvent.ACTION_MOVE:
                    posX = (int) motionEvent.getX() - (catWidth / 2);
                    break;
            }

            /*
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){




                action_flg = true;
            }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){


                posX = 20;
                action_flg = false;
            }
            */
        }
        return true;
    }
}
