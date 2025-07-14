package com.saikat.pixelle.editor;

import java.util.Stack;

public class Chain<T> {
    private final Stack<T> undoStack = new Stack<>();
    private final Stack<T> redoStack = new Stack<>();
    private T current;

    public void add(T newState) {
        if (current != null) {
            undoStack.push(current);
        }
        current = newState;
        redoStack.clear();
    }

    public T undo() {
        if ( !undoStack.isEmpty()) {
            redoStack.push(current);
            current = undoStack.pop();
        }
        return current;
    }

    public T redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(current);
            current = redoStack.pop();
        }
        return current;
    }

    public T getCurrent() {
        return current;
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
}
