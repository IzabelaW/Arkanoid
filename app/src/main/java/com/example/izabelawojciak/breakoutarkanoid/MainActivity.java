package com.example.izabelawojciak.breakoutarkanoid;

import android.graphics.Point;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int screenWidth = size.x;
        int screenHeight = size.y;

        gameView = new GameView(this, screenWidth, screenHeight);
        setContentView(gameView);

        final Thread gameThread = new Thread(new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gameView.updateGameState();
                        gameView.invalidate();
                        handler.postDelayed(this, 0);
                    }
                }, 0);
            }
        });

        gameThread.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.sensorManager.registerListener(gameView, gameView.sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.sensorManager.unregisterListener(gameView);
    }
}

