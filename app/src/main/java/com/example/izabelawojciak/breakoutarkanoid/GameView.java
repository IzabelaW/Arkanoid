package com.example.izabelawojciak.breakoutarkanoid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by izabelawojciak on 04.05.2017.
 */

public class GameView extends View implements SensorEventListener {

    private Paddle paddle;
    private Ball ball;
    private ArrayList<Brick> bricks;
    private ArrayList<Heart> hearts;
    private int numberOfBricks = 0;

    private int screenWidth;
    private int screenHeight;

    private int numberOfLives = 3;
    private int score = 0;

    private boolean touched = false;
    private boolean looser = false;
    private boolean winner = false;
    private boolean checkIfNewGame = false;

    private float touchedX;
    private int waitCount = 0;

    private Paint paint;
    private Paint getReadyPaint;

    private final int startTimer = 66;
    private final int frameRate = 33;

    private Bitmap bitmap;

    private Sensor sensor;
    private SensorManager sensorManager;

    public GameView(Context context, int screenWidth, int screenHeight) {
        super(context);

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        sensorManager = (SensorManager)getContext().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);

        paint = new Paint();

        Drawable background = (Drawable) getResources().getDrawable(R.drawable.background);
        setBackground(background);

        getReadyPaint = new Paint();
        getReadyPaint.setTextAlign(Paint.Align.CENTER);
        getReadyPaint.setColor(Color.WHITE);
        getReadyPaint.setFakeBoldText(true);

        createComponents();

    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        drawToCanvas(canvas);
        // Choose the brush color for drawing
        paint.setColor(Color.BLACK);

        // Draw the score
        paint.setTextSize(60);
        paint.setFakeBoldText(true);
        canvas.drawText("Score: " + score, screenWidth - 300, screenHeight / 20, paint);

        if (winner){
            getReadyPaint.setColor(Color.GREEN);
            canvas.drawText("CONGRATS", screenWidth / 2, (screenHeight / 2) - (ball.getBall().bottom - ball.getBall().top) - 140, getReadyPaint);
            getReadyPaint.setTextSize(70);
            canvas.drawText("YOU WON!!!", screenWidth / 2, (screenHeight / 2) - (ball.getBall().bottom - ball.getBall().top) - 70, getReadyPaint);
        } else if (looser){
            getReadyPaint.setColor(Color.RED);
            canvas.drawText("GAME OVER", screenWidth / 2, (screenHeight / 2) - (ball.getBall().bottom - ball.getBall().top) - 140, getReadyPaint);
            getReadyPaint.setTextSize(70);
            canvas.drawText("YOU LOST!!!", screenWidth / 2, (screenHeight / 2) - (ball.getBall().bottom - ball.getBall().top) - 70, getReadyPaint);
        }

        engine(canvas);
    }

    private void drawToCanvas(Canvas canvas) {
        drawHearts(canvas);
        drawBricks(canvas);
        paddle.drawPaddle(canvas,getContext());
        ball.drawBall(canvas);
    }

    private void drawBricks(Canvas canvas){
        for (int i = 0; i < bricks.size(); i++){
            bricks.get(i).drawBrick(canvas);
        }
    }

    private void drawHearts(Canvas canvas) {
        for (int i = 0; i < hearts.size(); i++){
            hearts.get(i).drawHeart(canvas);
        }
    }

    private void createComponents(){
        paddle = new Paddle(screenWidth,screenHeight,getContext());
        ball = new Ball(screenWidth, screenHeight, getContext());
        bricks = new ArrayList<>();
        hearts = new ArrayList<>();

        createHearts();
        createBricksAndRestart();
    }

    private void createHearts(){
        int heartHeight = screenWidth / 20;
        int spacing = screenWidth / 144;
        int topOffset = screenHeight / 30;
        int heartWidth = (screenWidth / 15) - spacing;
        for (int i = 0; i < 3; i++){
            int x_coordinate = i * (heartWidth + spacing);
            hearts.add(new Heart(x_coordinate, topOffset, x_coordinate + heartWidth, topOffset + heartHeight, getContext()));
        }
    }

    private void createBricksAndRestart(){

        int blockHeight = screenWidth / 20;
        int spacing = screenWidth / 144;
        int topOffset = screenHeight / 10;
        int blockWidth = (screenWidth / 8) - spacing;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                int y_coordinate = (i * (blockHeight + spacing)) + topOffset;
                int x_coordinate = j * (blockWidth + spacing);

                bricks.add(new Brick(x_coordinate, y_coordinate, x_coordinate + blockWidth, y_coordinate + blockHeight, getContext()));
                numberOfBricks++;
            }
        }

        if (numberOfLives == 0){
            numberOfLives = 3;
            score = 0;
        }
    }

    public void updateGameState() {
        try {
            Thread.sleep(frameRate);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (bricks.size() == 0) {
            winner = true;
            score = 0;
            checkIfNewGame = true;
        }

        if (numberOfLives <= 0) {
            looser = true;
            checkIfNewGame = true;
        }

        if (checkIfNewGame) {
            createComponents();
            waitCount = 0;
            checkIfNewGame = false;
        }
        waitCount++;

        if (touched) {
            paddle.movePaddle((int) touchedX);
        }
    }

    public void engine(Canvas canvas){
        if (waitCount > startTimer) {
            winner = false;
            looser = false;
            boolean hitBottom = ball.setVelocity(paddle);
            if (hitBottom) {
                numberOfLives--;
                hearts.remove(hearts.size()-1);
            }
            ball.checkPaddleCollision(paddle);
            score += ball.checkBrickCollision(bricks);
        } else {

            getReadyPaint.setColor(Color.WHITE);
            getReadyPaint.setTextSize(70);
            canvas.drawText("GET READY...", screenWidth / 2, (screenHeight / 2) - (ball.getBall().bottom - ball.getBall().top), getReadyPaint);
            getReadyPaint.setTextSize(100);

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            touched = true;
            touchedX = motionEvent.getX();
        }
        return touched;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        touched = true;
        touchedX = event.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
