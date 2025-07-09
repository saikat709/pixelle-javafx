package com.saikat.pixelle.editor;

import javafx.scene.image.ImageView;

public class Gray implements EditorCommand {


    @Override
    public void applyToFile(ImageView imageView) {
        long start = System.nanoTime();

        long end = System.nanoTime();
        System.out.println("Time taken: " + (end - start) / 1_000_000 + " ms");
    }


    @Override
    public void removeAppliedEdit(ImageView imageView) {

    }

    @Override
    public String toString() {
        return "Gray Edit.";
    }
}