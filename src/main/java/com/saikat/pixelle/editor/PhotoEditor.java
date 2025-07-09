package com.saikat.pixelle.editor;

import javafx.scene.image.ImageView;

public class PhotoEditor {
    ImageView currentImage;
    Node tail;

    private static class Node {
        EditorCommand command;
        Node next, previous;

        public Node(EditorCommand command) {
            this.command = command;
            this.next = null;
            this.previous = null;
        }
    }


    public PhotoEditor (ImageView imageView){
        this.tail = null;
        this.currentImage = imageView;
    }


    public void addEditingCommand(EditorCommand command) {
        System.out.println("Started adding.");

        if ( currentImage == null ){
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

        // command.applyToFile(currentlyEditingImage);
        System.out.println("Applied command " + command);
    }


    public void redo(){
        if ( !hasNext() ){
            throw new RuntimeException("No next edit command found");
        }
        tail = tail.next;
        // tail.command.applyToFile(currentlyEditingImage);
        System.out.println("Redo called.");
    }

    public void undo(){
        if ( !hasPrevious() ){
            throw new RuntimeException("No previous step.");
        }
        // tail.command.removeAppliedEdit(currentlyEditingImage);
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