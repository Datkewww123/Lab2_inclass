package com.example.flappybirdclone;

import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public class Pipe {
    private int x;
    private int width;
    private int gapY;
    private int gapHeight;
    private int speed;
    private Rect topRect;
    private Rect bottomRect;
    private int screenHeight;
    private int pipeHeight;
    private int pipeWidth;
    private boolean passed;

    private static final Random RANDOM = new Random();

    public Pipe(int startX, int gapHeight, int speed) {
        this.screenHeight = AppConstants.SCREEN_HEIGHT;
        this.pipeHeight = AppConstants.getBitmapBank().getPipeHeight();
        this.pipeWidth = AppConstants.getBitmapBank().getPipeWidth();
        this.width = pipeWidth;
        this.gapHeight = gapHeight;
        this.speed = speed;
        topRect = new Rect();
        bottomRect = new Rect();
        reset(startX);
        updateRects();
    }

    public void reset(int startX) {
        this.x = startX;
        int topLimit = Math.max(0, pipeHeight + 20);
        int groundMargin = (int) (screenHeight * 0.12f);
        int bottomLimit = Math.max(0, screenHeight - pipeHeight - gapHeight - groundMargin);
        if (bottomLimit <= topLimit) {
            this.gapY = (screenHeight - gapHeight) / 2;
        } else {
            this.gapY = topLimit + RANDOM.nextInt(bottomLimit - topLimit + 1);
        }
        this.passed = false;
        updateRects();
    }

    public void update() {
        x -= speed;
        updateRects();
    }

    private void updateRects() {
        topRect.left = x;
        topRect.right = x + width;
        topRect.top = 0;
        topRect.bottom = gapY;

        bottomRect.left = x;
        bottomRect.right = x + width;
        bottomRect.top = gapY + gapHeight;
        bottomRect.bottom = screenHeight;
    }

    public boolean isOffScreen() {
        return x + width < 0;
    }

    public boolean collides(Rect other) {
        return Rect.intersects(topRect, other) || Rect.intersects(bottomRect, other);
    }

    public Rect getTopRect() {
        return topRect;
    }

    public Rect getBottomRect() {
        return bottomRect;
    }

    public boolean isPassed() {
        return passed;
    }

    public void markPassed() {
        this.passed = true;
    }

    public void draw(Canvas canvas) {
        int yTop = gapY - pipeHeight;
        canvas.drawBitmap(AppConstants.getBitmapBank().getTopPipe(), x, yTop, null);
        int yBottom = gapY + gapHeight;
        canvas.drawBitmap(AppConstants.getBitmapBank().getBottomPipe(), x, yBottom, null);
    }

    public int getX() {
        return x;
    }
}


