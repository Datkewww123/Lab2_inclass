package com.example.flappybirdclone;

import android.graphics.Rect;

public class Bird {
    private int birdX, birdY, currentFrame, velocity;
    public static int maxFrame;
    private Rect hitbox;

    // --- AI Variables ---
    public NeuralNetwork brain;
    public float fitness;

    public Bird() {
        birdX = AppConstants.SCREEN_WIDTH / 3 - AppConstants.getBitmapBank().getBirdWidth() / 2;
        birdY = AppConstants.SCREEN_HEIGHT / 2 - AppConstants.getBitmapBank().getBirdHeight() / 2;
        currentFrame = 0;
        maxFrame = 3;
        velocity = 0;
        hitbox = new Rect(birdX, birdY, birdX + AppConstants.getBitmapBank().getBirdWidth(), birdY + AppConstants.getBitmapBank().getBirdHeight());

        // Khởi tạo bộ não: 4 input, 4 hidden nodes, 1 output
        brain = new NeuralNetwork(4, 4, 1);
        fitness = 0;
    }

    // --- Nạp bộ não từ chim xuất sắc của thế hệ trước (dùng cho Lai tạo) ---
    public Bird(NeuralNetwork brainClone) {
        this(); // Gọi constructor mặc định bên trên để setup tọa độ
        this.brain = brainClone.copy();
    }

    // --- AI suy nghĩ và ra quyết định ---
    public void think(Pipe closestPipe) {
        if (closestPipe != null) {
            float[] inputs = new float[4];

            // Chuẩn hóa dữ liệu đầu vào (chia cho độ phân giải màn hình để giá trị nằm trong khoảng 0-1)
            inputs[0] = (float) birdY / AppConstants.SCREEN_HEIGHT;
            inputs[1] = (float) Math.max(0, closestPipe.getX() - birdX) / AppConstants.SCREEN_WIDTH;

            // Lấy tọa độ Y từ hitbox của ống nước có sẵn trong file Pipe của bạn
            // .bottom của ống trên là mép dưới của nó
            inputs[2] = (float) closestPipe.getTopRect().bottom / AppConstants.SCREEN_HEIGHT;
            // .top của ống dưới là mép trên của nó
            inputs[3] = (float) closestPipe.getBottomRect().top / AppConstants.SCREEN_HEIGHT;

            // Truyền thông số vào mạng nơ-ron
            float[] output = brain.predict(inputs);

            // Nếu output > 0.5 thì AI quyết định nhảy
            if (output[0] > 0.5f) {
                flap();
            }
        }
    }

    // --- Hàm cộng điểm sinh tồn ---
    public void addFitness(float amount) {
        fitness += amount;
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