package com.saikat.pixelle.constants;

public enum ColorFilterType {
    GRAYSCALE,       // Converts to black and white tones
    SEPIA,           // Old photograph look
    INVERT,          // Inverts colors
    RED_TINT,        // Keeps red, tones down others
    GREEN_TINT,      // Keeps green, tones down others
    BLUE_TINT,       // Keeps blue, tones down others
    BINARIZE,        // Black or white only (threshold)
    BRIGHTNESS_UP,   // Makes image brighter
    BRIGHTNESS_DOWN, // Makes image darker
    CONTRAST_UP,     // High contrast effect
    CONTRAST_DOWN    // Low contrast effect
}
