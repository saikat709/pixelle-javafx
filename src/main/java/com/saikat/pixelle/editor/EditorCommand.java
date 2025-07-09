package com.saikat.pixelle.editor;

import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;
import java.io.File;

public interface EditorCommand {
    public void applyToFile(ImageView file);
    public void removeAppliedEdit(ImageView file);

}
