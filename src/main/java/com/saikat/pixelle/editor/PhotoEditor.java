package com.saikat.pixelle.editor;

import com.saikat.pixelle.utils.EffectUtil;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

public class PhotoEditor {
    private ImageView imageView;
    private StackPane imageContainer;

    private Stack<Type> undoStack;
    private Stack<Type> redoStack;
    private Type current;

    private Chain<Effect> effectChain;
    private Chain<Label>  labelChain;
    private Effect lastEffect;

    private double imageW, imageH;

    public PhotoEditor (ImageView imageView, StackPane stackPane){
        this.imageView = imageView;
        this.imageContainer = stackPane;

        this.undoStack    = new Stack<>();
        this.redoStack    = new Stack<>();
        this.effectChain  = new Chain<>();
        this.labelChain   = new Chain<>();

        this.imageW = 0.0;
        this.imageH = 0.0;
    }

    public void initializeImageSize(Image image){
        this.imageW = image.getWidth();
        this.imageH = image.getHeight();
    }

    public void addEffect(Effect effect){
        if ( lastEffect != null ){
            EffectUtil.tryChainEffect(effect, lastEffect);
        }
        this.effectChain.add(effect);
        this.lastEffect = effect;
        this.imageView.setEffect(effect);
        add(Type.EFFECT);
    }

    public void addText(Label label){
        this.labelChain.add(label);
        this.imageContainer.getChildren().add(label);
        add(Type.TEXT);
    }

    private void add(Type action) {
        System.out.println("Add: " + action.toString() + ", Current: " + current);
        if (this.current != null) {
            undoStack.push(current);
            System.out.println("Added to undo stack.");
        }
        this.current = action;
        redoStack.clear();
    }

    public void undo() {
        if ( !undoStack.isEmpty()) {
            redoStack.push(current);
            current = undoStack.pop();

            if ( current == Type.EFFECT ) {
                effectChain.undo();
                imageView.setEffect(effectChain.getCurrent());
            } else {
                Label a = labelChain.getCurrent();
                labelChain.undo();
                imageContainer.getChildren().remove(a);
            }

        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(current);
            current = redoStack.pop();
        }
    }

    public Type getCurrent() {
        return current;
    }

    public boolean canRedo(){
        System.out.println("Redo stack: " + redoStack.stream().count());
        return !redoStack.isEmpty();
    }

    public boolean canUndo(){
        System.out.println("Undo stack: " + redoStack.stream().count());
        return !undoStack.isEmpty();
    }

    public void exportToPng(File dest){
        // WritableImage snapshot = new WritableImage((int) imageContainer.getWidth(), (int) imageContainer.getHeight());//

        WritableImage snapshot = new WritableImage((int) imageW, (int) imageH);

        StackPane newStackPane = new StackPane();
        ImageView newImageView = new ImageView(new Image(imageView.getImage().getUrl()));

        newImageView.setFitHeight(imageH);
        newImageView.setFitWidth(imageW);
        newStackPane.getChildren().add(newImageView);

        newStackPane.snapshot(result -> {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(result.getImage(), null), "png", dest);
                System.out.println("Saved: " + dest.getAbsolutePath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }, null, snapshot);
    }


    private enum Type {
        EFFECT,
        TEXT
    }
}