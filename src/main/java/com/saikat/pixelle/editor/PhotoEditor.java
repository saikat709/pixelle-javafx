package com.saikat.pixelle.editor;

import com.saikat.pixelle.components.BorderRectangle;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class PhotoEditor {
    private ImageView imageView;
    private StackPane imageContainer;

    private Rectangle   borderRect;
    private Rectangle   cropRect;

    private double imageW, imageH;

    private ColorAdjust     lastAppliedAdjust;
    private Effect          lastAppliedColorEffect;
    private GaussianBlur    lastAppliedGaussianBlur;
    private BorderRectangle lastborderRectangle;
    private Effect          lastEffect;

    private EffectChainBuilder effectChainBuilder;

    private Stack<Command> undoStack;
    private Stack<Command> redoStack;


    public PhotoEditor (ImageView imageView, StackPane stackPane){
        this.imageView = imageView;
        this.imageContainer = stackPane;

        this.imageW = 0.0;
        this.imageH = 0.0;

        this.lastAppliedAdjust = null;
        this.lastAppliedGaussianBlur = null;
        this.lastEffect = null;
        this.lastAppliedColorEffect = null;

        this.effectChainBuilder = new EffectChainBuilder();

        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    public void initializeImageSize(Image image){
        this.imageW = image.getWidth();
        this.imageH = image.getHeight();
    }

    public void addBorderRect(BorderRectangle rect){

    }

    public void addBlurEffect(GaussianBlur effect){
        Effect newEff = (GaussianBlur) effectChainBuilder.addEffect(effect);
        Command cmd = new EffectCommand(lastEffect, newEff);
        undoStack.push(cmd);
        imageView.setEffect(newEff);
        this.lastAppliedGaussianBlur = effect;
        this.lastEffect = newEff;
    }

    public void addColorEffect(Effect colorEffect){
        Effect newEff = effectChainBuilder.addEffect(colorEffect);
        Command command = new EffectCommand(lastEffect, newEff);
        undoStack.push(command);
        imageView.setEffect(colorEffect);
        this.lastAppliedColorEffect = newEff;
        this.lastEffect =  newEff;
    }

    public Effect getLastEffect() {
        return this.lastEffect;
    }

    public Effect getappliedEffect(Effect effect){
        return effectChainBuilder.getAppliedEffect(effect);
    }

    public boolean canUndo(){
        return !undoStack.isEmpty();
    }

    public boolean canRedo(){
        return !redoStack.isEmpty();
    }

    public void exportToPng(File dest){
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