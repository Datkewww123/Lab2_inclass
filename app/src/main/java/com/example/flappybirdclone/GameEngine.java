package com.example.flappybirdclone;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import java.util.ArrayList;
import java.util.Iterator;

public class GameEngine {

    BackgroundImage backgroundImage;
    Bird bird;
    ArrayList<Pipe> pipes;
    int spawnTimer;
    int spawnIntervalMs;
    int pipeSpeed;
    int pipeGapHeight;
    int score;
    Paint textPaint;

    private int gameState;

    public GameEngine() {
        backgroundImage = new BackgroundImage();
        bird = new Bird();
        gameState = 0;
        pipes = new ArrayList<>();
        spawnTimer = 0;
        spawnIntervalMs = 1600;
        pipeSpeed = 10;
        pipeGapHeight = (int) (AppConstants.SCREEN_HEIGHT * 0.28f);
        score = 0;
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(64);
        textPaint.setTextAlign(Paint.Align.LEFT);
    }

    public void setGameState(int state) {
        this.gameState = state;
    }

    public int getGameState() {
        return this.gameState;
    }

    public void updateAndDrawableBackgroundImage(Canvas canvas) {
        backgroundImage.setX(backgroundImage.getX() - backgroundImage.getVelocity());
        if (backgroundImage.getX() <- AppConstants.getBitmapBank().getBackgroundWidth()) {
            backgroundImage.setX(0);
        }
        canvas.drawBitmap(AppConstants.getBitmapBank().getBackground_game(),backgroundImage.getX(), backgroundImage.getY(), null);
        if(backgroundImage.getX() <- AppConstants.getBitmapBank().getBackgroundWidth() - AppConstants.SCREEN_WIDTH){
            canvas.drawBitmap(AppConstants.getBitmapBank().getBackground_game(), backgroundImage.getX() + AppConstants.getBitmapBank().getBackgroundWidth(), backgroundImage.getY(), null);
        }
    }
    public void updateAndDrawBird(Canvas canvas) {
        final boolean DEBUG_HITBOX = false;
        Paint debugPaint = null;
        Paint birdDebugPaint = null;
        if (DEBUG_HITBOX) {
            debugPaint = new Paint();
            debugPaint.setColor(Color.argb(100, 255, 0, 0));
            birdDebugPaint = new Paint();
            birdDebugPaint.setColor(Color.argb(100, 0, 255, 0));
        }
        if (gameState == 1) {
            
            spawnTimer += GameThread.DELAY_MS;
            if (spawnTimer >= spawnIntervalMs) {
                spawnTimer = 0;
                int startX = AppConstants.SCREEN_WIDTH + 100;
                pipes.add(new Pipe(startX, pipeGapHeight, pipeSpeed));
            }

            Iterator<Pipe> iter = pipes.iterator();
            while (iter.hasNext()) {
                Pipe p = iter.next();
                p.update();
                
                if (!p.isPassed() && p.getX() + AppConstants.getBitmapBank().getPipeWidth() < bird.getBirdX()) {
                    p.markPassed();
                    score++;
                }
                if (p.isOffScreen()) {
                    iter.remove();
                } else if (p.collides(bird.getHitbox())) {
                    setGameState(0);
                }
            }
        }

        for (Pipe p : pipes) {
            p.draw(canvas);
            if (DEBUG_HITBOX) {
                canvas.drawRect(p.getTopRect(), debugPaint);
                canvas.drawRect(p.getBottomRect(), debugPaint);
            }
        }

        if (gameState == 1) {
            bird.setVelocity(bird.getVelocity() + AppConstants.gravity);
            bird.setBirdY(bird.getBirdY() + bird.getVelocity());
        }

        // clamp top of screen
        if (bird.getBirdY() < 0) {
            bird.setBirdY(0);
        }

        // ground collision: if bird hits bottom of screen, game over
        int birdHeight = AppConstants.getBitmapBank().getBirdHeight();
        int birdBottom = bird.getBirdY() + birdHeight;
        int screenBottom = AppConstants.SCREEN_HEIGHT;
        if (birdBottom >= screenBottom) {
            bird.setBirdY(screenBottom - birdHeight);
            setGameState(0);
        }
        int currentFrame = bird.getCurrentFrame();
        canvas.drawBitmap(AppConstants.getBitmapBank().getBird(currentFrame), bird.getBirdX(), bird.getBirdY(), null);
        currentFrame++;
        if(currentFrame > Bird.maxFrame) {
            currentFrame = 0;
        }
        bird.setCurrentFrame(currentFrame);
        canvas.drawText("Score: " + score, 20, 80, textPaint);

        
        if (getGameState() == 0 && score > 0) {
            Paint goPaint = new Paint();
            goPaint.setColor(Color.YELLOW);
            goPaint.setTextSize(96);
            goPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("Game Over", AppConstants.SCREEN_WIDTH / 2f, AppConstants.SCREEN_HEIGHT / 2f, goPaint);
            canvas.drawText("Tap to Restart", AppConstants.SCREEN_WIDTH / 2f, AppConstants.SCREEN_HEIGHT / 2f + 120f, textPaint);
        }
        if (DEBUG_HITBOX) {
            canvas.drawRect(bird.getHitbox(), birdDebugPaint);
        }
    }

    public void resetGame() {
        pipes.clear();
        score = 0;
        bird = new Bird();
        spawnTimer = 0;
        setGameState(0);
    }
}
