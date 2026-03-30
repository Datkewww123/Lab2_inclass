package com.example.flappybirdclone;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import java.util.ArrayList;
import java.util.Iterator;

public class GameEngine {

    BackgroundImage backgroundImage;
    public ArrayList<Bird> birds;
    public ArrayList<Bird> savedBirds;
    ArrayList<Pipe> pipes;
    int spawnTimer;
    int spawnIntervalMs;
    int pipeSpeed;
    int pipeGapHeight;
    int score;
    Paint textPaint;

    // AI Constants
    public final int TOTAL_BIRDS = 50;
    public int generation = 1;
    private int gameState;

    public GameEngine() {
        backgroundImage = new BackgroundImage();
        pipes = new ArrayList<>();
        birds = new ArrayList<>();
        savedBirds = new ArrayList<>();

        // Tạo bầy chim thế hệ đầu tiên
        for (int i = 0; i < TOTAL_BIRDS; i++) {
            birds.add(new Bird());
        }

        gameState = 1; // Luôn chạy
        spawnIntervalMs = 1600;
        spawnTimer = spawnIntervalMs; // Đẻ ống ngay lập tức ở frame đầu

        pipeSpeed = 10;
        pipeGapHeight = (int) (AppConstants.SCREEN_HEIGHT * 0.28f);
        score = 0;

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(64);
        textPaint.setTextAlign(Paint.Align.LEFT);
    }

    public void updateAndDrawableBackgroundImage(Canvas canvas) {
        backgroundImage.setX(backgroundImage.getX() - backgroundImage.getVelocity());
        if (backgroundImage.getX() < -AppConstants.getBitmapBank().getBackgroundWidth()) {
            backgroundImage.setX(0);
        }
        canvas.drawBitmap(AppConstants.getBitmapBank().getBackground_game(), backgroundImage.getX(), backgroundImage.getY(), null);
        if (backgroundImage.getX() < -AppConstants.getBitmapBank().getBackgroundWidth() + AppConstants.SCREEN_WIDTH) {
            canvas.drawBitmap(AppConstants.getBitmapBank().getBackground_game(), backgroundImage.getX() + AppConstants.getBitmapBank().getBackgroundWidth(), backgroundImage.getY(), null);
        }
    }

    public void updateAndDrawBird(Canvas canvas) {
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
            p.draw(canvas);

            if (!p.isPassed() && !birds.isEmpty() && p.getX() + AppConstants.getBitmapBank().getPipeWidth() < birds.get(0).getBirdX()) {
                p.markPassed();
                score++;
                for (Bird b : birds) {
                    b.addFitness(20.0f);
                }
            }
            if (p.isOffScreen()) {
                iter.remove();
            }
        }

        Pipe closestPipe = null;
        for (Pipe p : pipes) {
            if (!p.isPassed() && (birds.isEmpty() || p.getX() + AppConstants.getBitmapBank().getPipeWidth() > birds.get(0).getBirdX())) {
                closestPipe = p;
                break;
            }
        }
        Iterator<Bird> birdIter = birds.iterator();
        while (birdIter.hasNext()) {
            Bird b = birdIter.next();
            b.think(closestPipe);
            b.setVelocity(b.getVelocity() + AppConstants.gravity);
            b.setBirdY(b.getBirdY() + b.getVelocity());
            boolean hitCeiling = b.getBirdY() <= 0;
            int birdHeight = AppConstants.getBitmapBank().getBirdHeight();
            boolean hitGround = (b.getBirdY() + birdHeight) >= AppConstants.SCREEN_HEIGHT;
            boolean hitPipe = false;

            for (Pipe p : pipes) {
                if (p.collides(b.getHitbox())) {
                    hitPipe = true;
                    break;
                }
            }
            if (hitGround || hitPipe || hitCeiling) {

                if (closestPipe != null) {

                    float gapCenterY = (closestPipe.getTopRect().bottom + closestPipe.getBottomRect().top) / 2.0f;

                    float distanceToGap = Math.abs(b.getBirdY() - gapCenterY);

                    b.fitness -= (distanceToGap / 50.0f);
                }

                savedBirds.add(b);
                birdIter.remove();
            } else {
                int currentFrame = b.getCurrentFrame();
                canvas.drawBitmap(AppConstants.getBitmapBank().getBird(currentFrame), b.getBirdX(), b.getBirdY(), null);
                b.setCurrentFrame((currentFrame + 1 > Bird.maxFrame) ? 0 : currentFrame + 1);

                b.addFitness(0.1f);
            }
        }
        canvas.drawText("Score: " + score, 20, 80, textPaint);
        canvas.drawText("Gen: " + generation, 20, 150, textPaint);
        canvas.drawText("Alive: " + birds.size(), 20, 220, textPaint);
        if (birds.isEmpty()) {
            nextGeneration();
        }
    }

    public void nextGeneration() {
        generation++;
        pipes.clear();
        score = 0;
        spawnTimer = spawnIntervalMs;
        Bird bestBird = savedBirds.get(0);
        for (Bird b : savedBirds) {
            if (b.fitness > bestBird.fitness) {
                bestBird = b;
            }
        }

        birds.clear();
        birds.add(new Bird(bestBird.brain));
        for (int i = 1; i < TOTAL_BIRDS; i++) {
            Bird child = new Bird(bestBird.brain);
            child.brain.mutate(0.15f);
            birds.add(child);
        }

        savedBirds.clear();
    }
}