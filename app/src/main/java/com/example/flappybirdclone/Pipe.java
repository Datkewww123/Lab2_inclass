package com.example.flappybirdclone;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

    // AI: Công cụ để tô màu phần ống bị cụt
    private Paint pipeBodyPaint;

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

        // Setup chổi sơn màu xanh để nối ống
        pipeBodyPaint = new Paint();
        // Tôi đang để tạm mã màu #74BF2E (màu xanh chuẩn của Flappy Bird gốc)
        pipeBodyPaint.setColor(Color.parseColor("#74BF2E"));
        pipeBodyPaint.setStyle(Paint.Style.FILL);

        reset(startX);
        updateRects();
    }

    public void reset(int startX) {
        this.x = startX;
        int minDistance = (int) (screenHeight * 0.15f);
        int minGapY = minDistance;
        int maxGapY = screenHeight - gapHeight - minDistance;

        if (maxGapY <= minGapY) {
            this.gapY = (screenHeight - gapHeight) / 2;
        } else {
            this.gapY = minGapY + RANDOM.nextInt(maxGapY - minGapY + 1);
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
        if (yTop > 0) {
            canvas.drawRect(x, 0, x + width, yTop, pipeBodyPaint);
        }
        int yBottom = gapY + gapHeight;
        canvas.drawBitmap(AppConstants.getBitmapBank().getBottomPipe(), x, yBottom, null);
        int bottomPipeEnd = yBottom + pipeHeight;
        if (bottomPipeEnd < screenHeight) {
            canvas.drawRect(x, bottomPipeEnd, x + width, screenHeight, pipeBodyPaint);
        }
    }

    public int getX() {
        return x;
    }
}