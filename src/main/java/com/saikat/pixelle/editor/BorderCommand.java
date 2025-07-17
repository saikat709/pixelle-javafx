package com.saikat.pixelle.editor;

import com.saikat.pixelle.components.BorderRectangle;

public class BorderCommand extends Command {
    private BorderRectangle currentBorder;
    private BorderRectangle previousBorder;

    public BorderCommand(BorderRectangle currentBorder, BorderRectangle previousBorder) {
        this.currentBorder = currentBorder;
        this.previousBorder = previousBorder;
    }

    public BorderRectangle getCurrentBorder() {
        return currentBorder;
    }

    public BorderRectangle getPreviousBorder() {
        return previousBorder;
    }
}
