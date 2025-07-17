package com.saikat.pixelle.constants;

import java.io.File;

public class ConstValues {
    public static final String STARTING_HELP_URL  = "https://github.com/saikat709/pixelle-javafx";

    public static final String GEMINI_IMAGE_MODEL = "gemini-2.0-flash-preview-image-generation";
    public static final String APP_DIRECTORY_NAME_HIDDEN = ".pixelle";
    public static final String APP_DIRECTORY_NAME = "Pixelle";
    public static final File   BASE_DIR           = new File(System.getProperty("user.dir"), ".pixelle");
    public static final File   DOWNLOAD_DIR       = new File(System.getProperty("user.home"), "Downloads/" + APP_DIRECTORY_NAME);

    public static final String GENERATED_FILENAME = "ai generated image";
    public static final String CURRENTLY_EDITING_IMAGE ="currently_editing_image";

    public static final double UI_SCALE_FACTOR = 1.006;
    public static final double ZOOM_FACTOR     = 1.05;
}
