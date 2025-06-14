package com.saikat.pixelle.constants;

import java.io.File;

public class ConstValues {
    public static final String STARTING_HELP_URL = "https://github.com/saikat/Pixelle";
    public static final String GEMINI_IMAGE_MODEL = "gemini-2.0-flash-preview-image-generation";
    public static final String APP_DIRECTORY_NAME_HIDDEN = ".pixelle";
    public static final String APP_DIRECTORY_NAME = "Pixelle";
    public static final File BASE_DIR = new File(System.getProperty("user.dir"), ".pixelle");
    public static final File DOWNLOAD_DIR = new File(System.getProperty("user.home"), "Downloads/" + APP_DIRECTORY_NAME);
}
