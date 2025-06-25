package com.saikat.pixelle.editor;

import java.io.File;

public class PhotoEditor {
    Node tail;
    File currentlyEditingImage;


    private static class Node {
        EditorCommand command;
        Node next, previous;

        public Node(EditorCommand command) {
            this.command = command;
            this.next = null;
            this.previous = null;
        }
    }

    public PhotoEditor (String pathToImage){
        this.tail = null;
        this.currentlyEditingImage = new File(pathToImage);

        if ( !currentlyEditingImage.exists() ) {
            throw new RuntimeException("Image not found: " + currentlyEditingImage.getAbsolutePath());
        }
    }


    void addEditingCommand(EditorCommand command) {
        Node node = new Node(command);
        if( tail == null) {
            node.previous = null;
            node.next = null;
            tail = node;
        }
        tail.next = node;
        tail = node;
        command.applyToFile(currentlyEditingImage);
    }


    void redo(){
        if ( !hasNext() ){
            throw new RuntimeException("No next edit command found");
        }
        tail = tail.next;
        tail.command.applyToFile(currentlyEditingImage);
    }

    void undo(){
        if ( !hasPrevious() ){
            throw new RuntimeException("No previous step.");
        }
        tail.command.removeAppliedEdit(currentlyEditingImage);
        tail = tail.previous;
    }

    boolean hasNext(){
        return tail.next != null;
    }

    boolean hasPrevious(){
        return tail.previous != null;
    }
}