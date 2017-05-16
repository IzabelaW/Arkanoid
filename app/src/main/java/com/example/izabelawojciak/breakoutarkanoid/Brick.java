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

public class Brick extends ShapeDrawable{

    private RectF brick;
    private Paint paint;
    private Bitmap bitmap;

    public Brick(int x, int y, int width, int height, Context context){
        super(new RectShape());

        paint = new Paint();
        brick = new RectF(x,y,width,height);
        bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.brick);

    }

    public RectF getBrick() {
        return brick;
    }

    public void drawBrick(Canvas canvas){
        canvas.drawBitmap(bitmap, null, brick, paint);
    }
}
