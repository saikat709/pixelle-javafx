package com.saikat.pixelle.listeners;

public interface OnImageGeneratedListener {
    public void onImageGenerated(String imagePath);
    public void onError(String errorMessage);
}
