package com.example.flappybirdclone;

import java.util.Random;

public class NeuralNetwork {
    int inputNodes, hiddenNodes, outputNodes;
    float[][] weights_ih;
    float[][] weights_ho;
    float[] bias_h;
    float[] bias_o;
    Random random = new Random();

    public NeuralNetwork(int input, int hidden, int output) {
        this.inputNodes = input;
        this.hiddenNodes = hidden;
        this.outputNodes = output;

        weights_ih = new float[hiddenNodes][inputNodes];
        weights_ho = new float[outputNodes][hiddenNodes];
        bias_h = new float[hiddenNodes];
        bias_o = new float[outputNodes];

        randomize(weights_ih);
        randomize(weights_ho);
        randomize(bias_h);
        randomize(bias_o);
    }

    private void randomize(float[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = (random.nextFloat() * 2) - 1; // Random từ -1 đến 1
            }
        }
    }

    private void randomize(float[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = (random.nextFloat() * 2) - 1;
        }
    }

    private float sigmoid(float x) {
        return (float) (1 / (1 + Math.exp(-x)));
    }

    public float[] predict(float[] inputArray) {
        // Tính toán lớp Hidden
        float[] hidden = new float[hiddenNodes];
        for (int i = 0; i < hiddenNodes; i++) {
            float sum = 0;
            for (int j = 0; j < inputNodes; j++) {
                sum += inputArray[j] * weights_ih[i][j];
            }
            hidden[i] = sigmoid(sum + bias_h[i]);
        }

        // Tính toán lớp Output
        float[] output = new float[outputNodes];
        for (int i = 0; i < outputNodes; i++) {
            float sum = 0;
            for (int j = 0; j < hiddenNodes; j++) {
                sum += hidden[j] * weights_ho[i][j];
            }
            output[i] = sigmoid(sum + bias_o[i]);
        }
        return output;
    }

    // Sao chép bộ gen cho thế hệ sau
    public NeuralNetwork copy() {
        NeuralNetwork clone = new NeuralNetwork(inputNodes, hiddenNodes, outputNodes);
        for (int i = 0; i < weights_ih.length; i++) System.arraycopy(this.weights_ih[i], 0, clone.weights_ih[i], 0, weights_ih[i].length);
        for (int i = 0; i < weights_ho.length; i++) System.arraycopy(this.weights_ho[i], 0, clone.weights_ho[i], 0, weights_ho[i].length);
        System.arraycopy(this.bias_h, 0, clone.bias_h, 0, bias_h.length);
        System.arraycopy(this.bias_o, 0, clone.bias_o, 0, bias_o.length);
        return clone;
    }

    // Đột biến gen để AI thông minh hơn
    public void mutate(float rate) {
        mutateMatrix(weights_ih, rate);
        mutateMatrix(weights_ho, rate);
        mutateArray(bias_h, rate);
        mutateArray(bias_o, rate);
    }

    private void mutateMatrix(float[][] matrix, float rate) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (random.nextFloat() < rate) matrix[i][j] += (random.nextFloat() * 0.5f) - 0.25f;
            }
        }
    }

    private void mutateArray(float[] array, float rate) {
        for (int i = 0; i < array.length; i++) {
            if (random.nextFloat() < rate) array[i] += (random.nextFloat() * 0.5f) - 0.25f;
        }
    }
}