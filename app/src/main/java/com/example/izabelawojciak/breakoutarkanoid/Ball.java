package com.example.izabelawojciak.breakoutarkanoid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by izabelawojciak on 04.05.2017.
 */

public class Ball extends ShapeDrawable {

    //ball
    private RectF ball;

    // ball dimensions
    private int radius;

    //ball speed
    private int velocityX;
    private int velocityY;

    //checks for collisions
    private boolean brickCollision;
    private boolean paddleCollision;

    //screen dimensions
    private int screenWidth;
    private int screenHeight;

    private Paint paint;
    private Bitmap bitmap;

    private SoundPool soundPool;
    private int paddleSoundId;
    private int blockSoundId;
    private int bottomSoundId;


    // timer when ball hits screen bottom
    private final int resetBallTimer = 1000;

    public Ball(int screenWidth, int screenHeight, Context context){
        super(new OvalShape());

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        paddleSoundId = soundPool.load(context, R.raw.paddle, 0);
        blockSoundId = soundPool.load(context, R.raw.block, 0);
        bottomSoundId = soundPool.load(context, R.raw.bottom, 0);

        paint = new Paint();
        bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.ball);


        ball = new RectF();
        init(screenWidth,screenHeight);
    }

    public void init(int screenWidth, int screenHeight){
        brickCollision = false;
        paddleCollision = false;

        Random random = new Random();

        radius = screenWidth / 72;
        velocityX = radius;
        velocityY = radius * 2;

        ball.left = (screenWidth / 2) - radius - 10;
        ball.right = (screenWidth / 2) + radius + 10;
        ball.top = (screenHeight / 2) -  radius - 10;
        ball.bottom = (screenHeight / 2) + radius + 10;

        int startingXDirection = random.nextInt(2);

        if (startingXDirection > 0){
            reverseXVelocity();
        }
    }

    public void reverseXVelocity(){
        velocityX = - velocityX;
    }

    public void reverseYVelocity(){
        velocityY = - velocityY;
    }

    public boolean setVelocity(Paddle paddle) {
        boolean bottomHit = false;

        if (brickCollision) {
            reverseYVelocity();
            brickCollision = false; // reset
        }

        // paddle collision
        if (paddleCollision && velocityY > 0) {
            int paddleSplit = (int) (paddle.getPaddle().right - paddle.getPaddle().left) / 4;
            int ballCenter = (int) ball.centerX();

            if (ballCenter < paddle.getPaddle().left + paddleSplit) {
                velocityX = -(radius * 3);
            } else if (ballCenter < paddle.getPaddle().left + (paddleSplit * 2)) {
                velocityX = -(radius * 2);
            } else if (ballCenter < paddle.getPaddle().centerX() + paddleSplit) {
                velocityX = radius * 2;
            } else {
                velocityX = radius * 3;
            }
            reverseYVelocity();
            paddleCollision = false;
        }

        // side walls collision
        if (ball.right >= screenWidth) {
            reverseXVelocity();
        } else if (ball.left <= 0) {
            ball.left = 0;
            ball.right = 2 * radius + 20;
            reverseXVelocity();
        }

        // screen top/bottom collisions
        if (ball.top <= 0) {
            reverseYVelocity();
        } else if (ball.top > screenHeight) {
            bottomHit = true;
            soundPool.play(bottomSoundId, 1, 1, 1, 0, 1);
            try {
                Thread.sleep(resetBallTimer);
                init(screenWidth, screenHeight); // reset ball
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // move ball
        ball.left += velocityX;
        ball.right += velocityX;
        ball.top += velocityY;
        ball.bottom += velocityY;

        return bottomHit;
    }

    public boolean checkPaddleCollision(Paddle paddle){
       if (RectF.intersects(paddle.getPaddle(), ball)){
           paddleCollision = true;
           soundPool.play(paddleSoundId, 1, 1, 1, 0, 1);
           return true;
       }
       else
           return false;
    }

    public int checkBrickCollision(ArrayList<Brick> bricks){
        int points = 0;
        for (int i = 0; i < bricks.size(); i++) {
            if (RectF.intersects(bricks.get(i).getBrick(), ball)) {
                brickCollision = true;
                soundPool.play(blockSoundId, 1, 1, 1, 0, 1);
                bricks.remove(i);
                points += 10;
            }
        }
        return points;
    }

    public void drawBall(Canvas canvas){
        canvas.drawBitmap(bitmap, null, ball, paint);
    }

    public RectF getBall() {
        return ball;
    }
}
