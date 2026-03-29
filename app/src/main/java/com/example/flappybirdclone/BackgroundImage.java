package com.example.flappybirdclone;

public class BackgroundImage {

    private int backgroundImageX, backgroundImageY, getBackGroundVelocity;
    public BackgroundImage() {
        backgroundImageX = 0;
        backgroundImageY = 0;
        getBackGroundVelocity = 3;
    }
    public int getX(){
        return backgroundImageX;
    }
    public int getY(){
        return backgroundImageY;
    }
    public void setX (int backgroundImageX){
        this.backgroundImageX = backgroundImageX;
    }
    public void setY (int backgroundImageY){
        this.backgroundImageY = backgroundImageY;
    }
    public int getVelocity(){
        return getBackGroundVelocity;
    }
}
