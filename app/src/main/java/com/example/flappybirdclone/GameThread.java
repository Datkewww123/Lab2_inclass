package com.example.flappybirdclone;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread{
    private final SurfaceHolder surfaceHolder;

    boolean isRunning;

    long startTime, loopTime;
    public static final int DELAY_MS = 33;
    long DELAY = DELAY_MS;
    public GameThread(SurfaceHolder surfaceHolder){
        this.surfaceHolder = surfaceHolder;
        isRunning = true;
    }

        @Override
        public void run(){
        while (isRunning){
                    startTime = SystemClock.uptimeMillis();
                    Canvas canvas = null;
                    try {
                        canvas = surfaceHolder.lockCanvas();
                        if (canvas != null) {
                            synchronized (surfaceHolder) {
                                AppConstants.getGameEngine().updateAndDrawableBackgroundImage(canvas);
                                AppConstants.getGameEngine().updateAndDrawBird(canvas);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("GameThread", "Error in game loop", e);
                    } finally {
                        if (canvas != null) {
                            try {
                                surfaceHolder.unlockCanvasAndPost(canvas);
                            } catch (Exception e) {
                                Log.e("GameThread", "unlockCanvasAndPost failed", e);
                            }
                        }
                    }
            loopTime = SystemClock.uptimeMillis() - startTime;
            if(loopTime < DELAY){
                try{
                    Thread.sleep(DELAY - loopTime);
                }
                catch(InterruptedException e){
                    Log.e("Interruped", "Interrupted while sleeping");
                }
            }
        }
    }

    public boolean isRunning(){
        return isRunning;
    }
    public void setRunning(boolean state){
        isRunning = state;
    }
}
