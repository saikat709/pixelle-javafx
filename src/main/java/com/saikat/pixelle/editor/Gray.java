package com.saikat.pixelle.editor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Gray implements EditorCommand {

    int[] previousPixels;

    @Override
    public void applyToFile(BufferedImage img) {
        long start = System.nanoTime();

        int width = img.getWidth();
        int height = img.getHeight();
        int[] pixels = img.getRGB(0, 0, width, height, null, 0, width);
        previousPixels = pixels;

        for (int i = 0; i < pixels.length; i++) {
            int p = pixels[i];

            int a = (p >> 24) & 0xff;
            int r = (p >> 16) & 0xff;
            int g = (p >> 8) & 0xff;
            int b = p & 0xff;

            int avg = (r + g + b) / 3;

            p = (a << 24) | (avg << 16) | (avg << 8) | avg;

            pixels[i] = p;
        }
        img.setRGB(0, 0, width, height, pixels, 0, width);

        long end = System.nanoTime();
        System.out.println("Time taken: " + (end - start) / 1_000_000 + " ms");
    }

    private static int getNewPixel(int pixel) {
        int alpha = ( pixel >> 24) & 0xff;
        int red   = ( pixel >> 16) & 0xff;
        int green = ( pixel >> 8 )  & 0xff;
        int blue  = pixel & 0xff;

        red   = red / 2;
        green = green / 2;
        blue  = blue / 2;

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    @Override
    public void removeAppliedEdit(BufferedImage originalImage) {
//        for (Pixel p : previousValues) {
//            originalImage.setRGB(p.getPositionX(), p.getPositionY(), p.getValue());
//        }
        originalImage.setRGB(0, 0, originalImage.getWidth(), originalImage.getHeight(),
                previousPixels, 0, originalImage.getWidth());
    }

    @Override
    public String toString() {
        return "Gray Edit.";
    }

    public static void main(String[] args) {

//        Gray filter = new Gray();
//
//        File testFile = new File("/home/saikat/Pictures/Screenshots/_.png");
//
//        if (testFile.exists()) {
//            System.out.println("Original file size: " + testFile.length() + " bytes");
//
//            // Apply the filter
//            filter.applyToFile(testFile);
//            System.out.println("Filtered file size: " + testFile.length() + " bytes");
//
//            ( new Scanner(System.in ) ).nextLine();
//
//            try {
//                Thread.sleep(200);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//
//            filter.removeAppliedEdit(testFile);
//            System.out.println("Restored file size: " + testFile.length() + " bytes");
//
//        } else {
//            System.out.println("Test image not found. Make sure 'test_image.png' exists in the current directory.");
//            System.out.println("A test image has been created for you.");
//        }
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