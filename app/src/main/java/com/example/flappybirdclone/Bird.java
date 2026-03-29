package com.example.flappybirdclone;

import android.graphics.Rect;

public class Bird {
    private int birdX, birdY, currentFrame, velocity;
    public static int maxFrame;
    private Rect hitbox;

    public Bird() {
        birdX = AppConstants.SCREEN_WIDTH / 3 - AppConstants.getBitmapBank().getBirdWidth() / 2;
        birdY = AppConstants.SCREEN_HEIGHT / 2 - AppConstants.getBitmapBank().getBirdHeight() / 2;
        currentFrame = 0;
        maxFrame = 3;
        velocity = 0;
        hitbox = new Rect(birdX, birdY, birdX + AppConstants.getBitmapBank().getBirdWidth(), birdY + AppConstants.getBitmapBank().getBirdHeight());
    }
    public int getVelocity(){
        return velocity;
    }
    public void setVelocity(int velocity){
        this.velocity = velocity;
    }
    public int getCurrentFrame(){
        return currentFrame;
    }
    public void setCurrentFrame(int currentFrame){
        this.currentFrame = currentFrame;
    }

    public int getBirdX() {
        return birdX;
    }
    public void setBirdX(int birdX) {
        this.birdX = birdX;
    }

    public int getBirdY() {
        return birdY;
    }
    public void setBirdY(int birdY) {
        this.birdY = birdY;
        updateHitbox();
    }

    public Rect getHitbox(){
        return hitbox;
    }

    private void updateHitbox(){
        int bw = AppConstants.getBitmapBank().getBirdWidth();
        int bh = AppConstants.getBitmapBank().getBirdHeight();
        int insetX = Math.max(4, bw / 6);
        int insetY = Math.max(4, bh / 6);
        hitbox.left = birdX + insetX;
        hitbox.top = birdY + insetY;
        hitbox.right = birdX + bw - insetX;
        hitbox.bottom = birdY + bh - insetY;
    }

    public void flap(){
        this.velocity = AppConstants.VELOCITY_WHEN_JUMPED;
        updateHitbox();
    }
}
