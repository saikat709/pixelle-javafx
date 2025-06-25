package com.saikat.pixelle.editor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class Gray implements EditorCommand {
    private Map<String, BufferedImage> originalImages;

    public Gray() {
        this.originalImages = new HashMap<>();
    }

    @Override
    public void applyToFile(File file) {
        try {
            BufferedImage originalImage = ImageIO.read(file);
            originalImages.put(file.getAbsolutePath(), deepCopy(originalImage));
            BufferedImage filteredImage = applyGrayscaleWithCircularMask(originalImage);
            String fileName = file.getName();
            String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
            ImageIO.write(filteredImage, extension, file);
        } catch (IOException e) {
            System.err.println("Error applying filter to file: " + file.getName());
            e.printStackTrace();
        }
    }

    @Override
    public void removeAppliedEdit(File file) {
        String filePath = file.getAbsolutePath();

        if (originalImages.containsKey(filePath)) {
            try {
                BufferedImage originalImage = originalImages.get(filePath);
                String fileName = file.getName();
                String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
                ImageIO.write(originalImage, extension, file);
                originalImages.remove(filePath);
                System.out.println("Removed filter from: " + file.getName());

            } catch (IOException e) {
                System.err.println("Error removing filter from file: " + file.getName());
                e.printStackTrace();
            }
        } else {
            System.out.println("No original image found for: " + file.getName());
        }
    }

    private BufferedImage applyGrayscaleWithCircularMask(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int centerX = width / 2;
        int centerY = height / 2;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = original.getRGB(x, y);

                // Calculate distance from center
                double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));

                // Outside the circle - apply grayscale
                Color originalColor = new Color(rgb);
                int gray = (int) (0.299 * originalColor.getRed() +
                        0.587 * originalColor.getGreen() +
                        0.114 * originalColor.getBlue());
                Color grayColor = new Color(gray, gray, gray);
                result.setRGB(x, y, grayColor.getRGB());
            }
        }

        return result;
    }

    private BufferedImage deepCopy(BufferedImage original) {
        BufferedImage copy = new BufferedImage(original.getWidth(),
                original.getHeight(),
                original.getType());
        Graphics2D g = copy.createGraphics();
        g.drawImage(original, 0, 0, null);
        g.dispose();
        return copy;
    }

    public static void main(String[] args) {
        createTestImage();

        Gray filter = new Gray();

        File testFile = new File("/home/saikat/Pictures/Camera/Photo from 2025-04-21 15-36-25.411928.jpeg");

        if (testFile.exists()) {
            System.out.println("Original file size: " + testFile.length() + " bytes");

            // Apply the filter
            filter.applyToFile(testFile);
            System.out.println("Filtered file size: " + testFile.length() + " bytes");

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            filter.removeAppliedEdit(testFile);
            System.out.println("Restored file size: " + testFile.length() + " bytes");

            filter.applyToFile(testFile);

        } else {
            System.out.println("Test image not found. Make sure 'test_image.png' exists in the current directory.");
            System.out.println("A test image has been created for you.");
        }
    }

    private static void createTestImage() {
        int width = 400;
        int height = 300;
        BufferedImage testImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = testImage.createGraphics();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int red = (x * 255) / width;
                int green = (y * 255) / height;
                int blue = ((x + y) * 255) / (width + height);

                Color color = new Color(red % 256, green % 256, blue % 256);
                testImage.setRGB(x, y, color.getRGB());
            }
        }

        g2d.setColor(Color.RED);
        g2d.fillOval(50, 50, 80, 80);

        g2d.setColor(Color.BLUE);
        g2d.fillRect(250, 100, 100, 60);

        g2d.setColor(Color.GREEN);
        g2d.fillOval(150, 180, 120, 90);

        g2d.dispose();

        try {
            ImageIO.write(testImage, "png", new File("test_image.png"));
            System.out.println("Test image created: test_image.png");
        } catch (IOException e) {
            System.err.println("Error creating test image");
            e.printStackTrace();
        }
    }
}