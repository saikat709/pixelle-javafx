package com.saikat.pixelle.editor;

import java.awt.image.BufferedImage;
import java.io.File;

public interface EditorCommand {
    public void applyToFile(BufferedImage file);
    public void removeAppliedEdit(BufferedImage file);

}
