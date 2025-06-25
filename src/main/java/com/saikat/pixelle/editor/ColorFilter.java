package com.saikat.pixelle.editor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

public class ColorFilter implements EditorCommand {

    public static void main(String[] args) {
        File inputImage = new File("/home/saikat/Pictures/Camera/Photo from 2025-04-21 15-36-25.411928.jpeg");

        // Apply grayscale
        ColorFilter grayFilter = new ColorFilter(ColorFilter.FilterType.GRAYSCALE);
        grayFilter.applyToFile(inputImage);

        // Apply sepia
        ColorFilter sepiaFilter = new ColorFilter(ColorFilter.FilterType.SEPIA);
        sepiaFilter.applyToFile(inputImage);
    }

    public enum FilterType {
        GRAYSCALE,
        SEPIA
    }

    private final FilterType filterType;

    public ColorFilter(FilterType filterType) {
        this.filterType = filterType;
    }

    @Override
    public void applyToFile(File file) {
        try {
            BufferedImage img = ImageIO.read(file);
            BufferedImage filteredImage = applyFilter(img);

            String outputPath = file.getParent() + File.separator + "filtered_" + file.getName();
            ImageIO.write(filteredImage, "jpeg", new File(outputPath));
            System.out.println("Filter applied. Saved to: " + outputPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeAppliedEdit(File file) {
        System.out.println("This demo doesn't support removing filters. Keep the original file for backup.");
    }

    private BufferedImage applyFilter(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage output = new BufferedImage(width, height, img.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color original = new Color(img.getRGB(x, y));

                int r = original.getRed();
                int g = original.getGreen();
                int b = original.getBlue();

                Color newColor;

                if (filterType == FilterType.GRAYSCALE) {
                    int gray = (r + g + b) / 3;
                    newColor = new Color(gray, gray, gray);
                } else if (filterType == FilterType.SEPIA) {
                    int tr = (int)(0.393 * r + 0.769 * g + 0.189 * b);
                    int tg = (int)(0.349 * r + 0.686 * g + 0.168 * b);
                    int tb = (int)(0.272 * r + 0.534 * g + 0.131 * b);

                    tr = Math.min(255, tr);
                    tg = Math.min(255, tg);
                    tb = Math.min(255, tb);

                    newColor = new Color(tr, tg, tb);
                } else {
                    newColor = original;
                }

                output.setRGB(x, y, newColor.getRGB());
            }
        }

        return output;
    }
}
