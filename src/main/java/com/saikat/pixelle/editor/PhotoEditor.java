package com.saikat.pixelle.editor;

import com.saikat.pixelle.components.BorderRectangle;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Objects;
import java.util.Stack;

public class PhotoEditor {
    private final Integer UNDO = 900;
    private final Integer REDO = 901;

    private ImageView imageView;
    private StackPane imageContainer;
    private Pane      drawAndTextPane;

    private double imageW, imageH;

    private ColorAdjust     lastAppliedAdjust;
    private Effect          lastAppliedColorEffect;
    private GaussianBlur    lastAppliedGaussianBlur;
    private BorderRectangle lastborderRectangle;
    private Effect          lastEffect;

    private EffectChainBuilder effectChainBuilder;

    private Stack<Command> undoStack;
    private Stack<Command> redoStack;

    public PhotoEditor (ImageView imageView, StackPane stackPane, Pane drawAndTextPane){
        this.imageView = imageView;
        this.imageContainer = stackPane;
        this.drawAndTextPane = drawAndTextPane;

        this.imageW = 0.0;
        this.imageH = 0.0;

        this.lastAppliedAdjust = null;
        this.lastAppliedGaussianBlur = null;
        this.lastEffect = null;
        this.lastAppliedColorEffect = null;
        this.lastborderRectangle = new BorderRectangle();

        this.effectChainBuilder = new EffectChainBuilder();

        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    public void initializeImageSize(Image image){
        this.imageW = image.getWidth();
        this.imageH = image.getHeight();
    }

    public void addBorder(BorderRectangle borderRectangle){
        BorderRectangle currRect = borderRectangle.clone();

        BorderCommand borderCommand = new BorderCommand(currRect, this.lastborderRectangle);
        this.undoStack.push(borderCommand);

        imageContainer.getChildren().remove(this.lastborderRectangle);
        imageContainer.getChildren().add(borderCommand.getCurrentBorder());

        this.lastborderRectangle = borderCommand.getCurrentBorder();

        if ( !redoStack.isEmpty() ) redoStack.clear();
    }

    public void addBlurEffect(GaussianBlur effect){
        Effect newEff = effectChainBuilder.addEffect(effect);
        Command cmd = new EffectCommand(lastEffect, newEff);

        undoStack.push(cmd);
        imageView.setEffect(newEff);

        this.lastAppliedGaussianBlur = effect;
        this.lastEffect = newEff;

        if ( !redoStack.isEmpty() ) redoStack.clear();
    }

    public void addColorEffect(Effect colorEffect){
        Effect newEff = effectChainBuilder.addEffect(colorEffect);
        Command command = new EffectCommand(lastEffect, newEff);

        undoStack.push(command);
        imageView.setEffect(colorEffect);

        this.lastAppliedColorEffect = colorEffect;
        this.lastEffect =  newEff;

        if ( !redoStack.isEmpty() ) redoStack.clear();
    }

    public Effect getLastEffect() {
        return this.lastEffect;
    }

    public Effect getappliedEffect(Effect effect){
        return effectChainBuilder.getAppliedEffect(effect);
    }

    public BorderRectangle getLastborderRectangle() {
        return lastborderRectangle;
    }

    public void addDrawCommand(Shape shape){
        drawAndTextPane.getChildren().add(shape);
        DrawCommand drawCommand = new DrawCommand(shape);
        undoStack.push(drawCommand);
        if ( !redoStack.isEmpty() ) redoStack.clear();
    }

    public void redo(){
        if ( redoStack.isEmpty() ) {
            throw new EmptyStackException();
        }
        Command command = redoStack.pop();
        processCommand(command, REDO);
        undoStack.push(command);
    }

    public void undo(){
        System.out.println("Stack: undo -> " + undoStack.size() + ",  redoStack -> " + redoStack.size() );

        if ( undoStack.isEmpty() ){
            throw new EmptyStackException();
        }

        Command command = undoStack.pop();
        processCommand(command, UNDO);
        redoStack.push(command);

    }

    private void processCommand(Command command, Integer type){
        if ( command instanceof EffectCommand ){
            if ( type == REDO ) {
                System.out.println("Applying.");
                imageView.setEffect(((EffectCommand) command).getPreviousEffect());
            } else {
                imageView.setEffect(((EffectCommand) command).getCurrentEffect());
                System.out.println("Removing.");
            }
        } else if ( command instanceof BorderCommand ){
            System.out.println(this.imageContainer.getChildren().toString());
            this.imageContainer.getChildren().remove(lastborderRectangle);
            System.out.println(this.imageContainer.getChildren().toString());
            if ( type == REDO ) {
                this.imageContainer.getChildren().add(((BorderCommand) command).getCurrentBorder());
                this.lastborderRectangle = ((BorderCommand) command).getCurrentBorder();
            } else {
                this.imageContainer.getChildren().add(((BorderCommand) command).getPreviousBorder());
                this.lastborderRectangle = ((BorderCommand) command).getPreviousBorder();
            }
            System.out.println(this.imageContainer.getChildren().toString());
        } else if ( command instanceof DrawCommand ){
            if ( type == REDO ) drawAndTextPane.getChildren().add(((DrawCommand) command).getShape());
            else drawAndTextPane.getChildren().remove(((DrawCommand) command).getShape());
        }
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