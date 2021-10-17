package com.monkeman.game.utils;

public class CollisionRect {
    float x;
    float y;
    int width;
    int height;

    public CollisionRect (float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void updatePosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean collisionWith (CollisionRect object) {
        return x < object.x + object.width && y < object.y + object.height && x + width > object.x && y + height > object.y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
