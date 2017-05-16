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
 * Created by izabelawojciak on 06.05.2017.
 */

public class Heart extends ShapeDrawable {

    private RectF heart;
    private Paint paint;
    private Bitmap bitmap;

    public Heart(int x, int y, int width, int height, Context context){
        super(new RectShape());

        paint = new Paint();
        heart = new RectF(x,y,width,height);
        bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.heart);
    }

    public void drawHeart(Canvas canvas){
        canvas.drawBitmap(bitmap, null, heart, paint);
    }


}
