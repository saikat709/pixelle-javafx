package com.saikat.pixelle.editor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class PhotoEditor {
    Node tail;
    BufferedImage currentlyEditingImage;

    private static class Node {
        EditorCommand command;
        Node next, previous;

        public Node(EditorCommand command) {
            this.command = command;
            this.next = null;
            this.previous = null;
        }
    }


    public PhotoEditor (File file){
        this.tail = null;
        if ( !file.exists() ) {
            throw new RuntimeException("Image not found: " + file.getAbsolutePath());
        }
        try {
            currentlyEditingImage = ImageIO.read(file);
        } catch (IOException e ){
            System.out.println("Error in Photo editor cons. Reading as buffer.");
        }
    }


    public void addEditingCommand(EditorCommand command) {
        System.out.println("Started adding.");

        if ( currentlyEditingImage == null ){
            throw new RuntimeException("Currently editing file is null. Never initialized.");
        }

        Node node = new Node(command);
        if( tail == null) {
            node.previous = null;
            node.next = null;
            tail = node;
        }

        node.previous = tail;
        tail.next = node;
        tail = node;
        command.applyToFile(currentlyEditingImage);

        System.out.println("Applied command " + command);
    }


    public void redo(){
        if ( !hasNext() ){
            throw new RuntimeException("No next edit command found");
        }
        tail = tail.next;
        tail.command.applyToFile(currentlyEditingImage);
        System.out.println("Redo called.");
    }

    public void undo(){
        if ( !hasPrevious() ){
            throw new RuntimeException("No previous step.");
        }
        tail.command.removeAppliedEdit(currentlyEditingImage);
        tail = tail.previous;
        System.out.println("Undo called.");
    }

    public boolean hasNext(){
        return tail != null && tail.next != null;
    }

    public boolean hasPrevious(){
        return tail != null && tail.previous != null;
    }
}