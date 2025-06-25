package com.saikat.pixelle.editor;

import java.io.File;

public interface EditorCommand {
    public void applyToFile(File file);
    public void removeAppliedEdit(File file);
}
