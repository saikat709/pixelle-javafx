package com.saikat.pixelle.editor;

public class Pixel {
    private int positionX;
    private int positionY;
    private int value;

    public Pixel(int x, int y, int value){
        this.positionX = x;
        this.positionY = y;
        this.value = value;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public int getValue() {
        return value;
    }
}
