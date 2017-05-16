package com.example.izabelawojciak.breakoutarkanoid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

/**
 * Created by izabelawojciak on 04.05.2017.
 */

public class Paddle extends ShapeDrawable {

    private RectF paddle;

    private int paddleWidth;
    private int paddleHeight;

    private int paddleOffset;
    private int paddleMoveOffset;

    private int screenWidth;
    private int screenHeight;

    private Paint paint;
    private Bitmap bitmap;

    public Paddle(int screenWidth, int screenHeight, Context context){

        super(new RectShape());

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        paint = new Paint();

        bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.paddle);

        paddle = new RectF();
        init(screenWidth,screenHeight);

    }

    public void init(int screenWidth, int screenHeight){

        paddleWidth = screenWidth / 10;
        paddleHeight = screenHeight / 72;
        paddleOffset = screenHeight / 3;
        paddleMoveOffset = screenWidth / 15;

        paddle.left = (screenWidth / 2) - paddleWidth;
        paddle.right = (screenWidth / 2) + paddleWidth;
        paddle.top = (screenHeight / 2 + paddleOffset) - paddleHeight;
        paddle.bottom = (screenHeight / 2 + paddleOffset) + paddleHeight;
    }

    public void movePaddle(int touchedX){

        if(touchedX >= paddle.left && touchedX <= paddle.right) {
            paddle.left = touchedX - paddleWidth;
            paddle.right = touchedX + paddleWidth;
        } else if (touchedX > paddle.right){
            paddle.left += paddleMoveOffset;
            paddle.right += paddleMoveOffset;
        } else if (touchedX < paddle.left){
            paddle.left -= paddleMoveOffset;
            paddle.right -= paddleMoveOffset;
        }

        if (paddle.left < 0){
            paddle.left = 0;
            paddle.right = paddleWidth * 2;
        }

        if (paddle.right > screenWidth){
            paddle.right = screenWidth;
            paddle.left = screenWidth - (paddleWidth * 2);
        }

    }

    public void drawPaddle(Canvas canvas, Context context){
        canvas.drawBitmap(bitmap, null, paddle, paint);
    }

    public RectF getPaddle(){
        return paddle;
    }
}
