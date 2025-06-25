package com.saikat.pixelle.editor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;


public class Blur implements EditorCommand {
    private int radius;
    private Map<String, BufferedImage> originalImages;

    public Blur(int radius) {
        this.radius = Math.max(1, radius); // Ensure minimum radius of 1
        this.originalImages = new HashMap<>();
    }

    @Override
    public void applyToFile(File file) {
        try {
            BufferedImage originalImage = ImageIO.read(file);

            // Store original image for restoration
            originalImages.put(file.getAbsolutePath(), deepCopy(originalImage));

            BufferedImage blurredImage = applyBlur(originalImage);

            // Get file extension
            String fileName = file.getName();
            String extension = fileName.substring(fileName.lastIndexOf('.') + 1);

            // Save the blurred image
            ImageIO.write(blurredImage, extension, file);

            System.out.println("Applied blur filter (radius: " + radius + ") to: " + file.getName());

        } catch (IOException e) {
            System.err.println("Error applying blur to file: " + file.getName());
            e.printStackTrace();
        }
    }

    @Override
    public void removeAppliedEdit(File file) {
        String filePath = file.getAbsolutePath();

        if (originalImages.containsKey(filePath)) {
            try {
                BufferedImage originalImage = originalImages.get(filePath);

                // Get file extension
                String fileName = file.getName();
                String extension = fileName.substring(fileName.lastIndexOf('.') + 1);

                // Restore original image
                ImageIO.write(originalImage, extension, file);

                // Remove from cache
                originalImages.remove(filePath);

                System.out.println("Removed blur filter from: " + file.getName());

            } catch (IOException e) {
                System.err.println("Error removing blur from file: " + file.getName());
                e.printStackTrace();
            }
        } else {
            System.out.println("No original image found for: " + file.getName());
        }
    }

    private BufferedImage applyBlur(BufferedImage original) {
        // Always use manual blur to avoid ConvolveOp issues
        return applyManualBlur(original);
    }

    private BufferedImage applyBoxBlur(BufferedImage original) {
        // Limit kernel size to avoid ConvolveOp issues
        int maxKernelSize = 25; // Maximum safe kernel size
        int kernelSize = Math.min(radius * 2 + 1, maxKernelSize);

        // Ensure kernel size is odd
        if (kernelSize % 2 == 0) {
            kernelSize--;
        }

        try {
            float[] kernelData = new float[kernelSize * kernelSize];
            float kernelValue = 1.0f / (kernelSize * kernelSize);

            // Create uniform kernel for box blur
            for (int i = 0; i < kernelData.length; i++) {
                kernelData[i] = kernelValue;
            }

            Kernel kernel = new Kernel(kernelSize, kernelSize, kernelData);
            ConvolveOp convolveOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

            // Ensure compatible image format
            BufferedImage compatibleOriginal = ensureCompatibleImage(original);
            BufferedImage result = new BufferedImage(compatibleOriginal.getWidth(),
                    compatibleOriginal.getHeight(),
                    BufferedImage.TYPE_INT_RGB);

            return convolveOp.filter(compatibleOriginal, result);
        } catch (Exception e) {
            System.out.println("ConvolveOp failed, falling back to manual blur");
            return applyManualBlur(original);
        }
    }

    private BufferedImage ensureCompatibleImage(BufferedImage original) {
        if (original.getType() == BufferedImage.TYPE_INT_RGB) {
            return original;
        }

        BufferedImage compatible = new BufferedImage(original.getWidth(),
                original.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = compatible.createGraphics();
        g.drawImage(original, 0, 0, null);
        g.dispose();
        return compatible;
    }

    private BufferedImage applyMultiPassBlur(BufferedImage original) {
        BufferedImage result = deepCopy(original);

        // Apply multiple smaller blurs to achieve larger blur effect
        int passes = (radius / 5) + 1;
        int passRadius = Math.min(5, radius);

        for (int pass = 0; pass < passes; pass++) {
            result = applySinglePassBlur(result, passRadius);
        }

        return result;
    }

    private BufferedImage applySinglePassBlur(BufferedImage image, int blurRadius) {
        int kernelSize = blurRadius * 2 + 1;
        float[] kernelData = new float[kernelSize * kernelSize];

        // Create Gaussian-like kernel
        float sigma = blurRadius / 3.0f;
        float sum = 0;
        int center = blurRadius;

        for (int y = 0; y < kernelSize; y++) {
            for (int x = 0; x < kernelSize; x++) {
                int dx = x - center;
                int dy = y - center;
                float distance = (float) Math.sqrt(dx * dx + dy * dy);
                float value = (float) Math.exp(-(distance * distance) / (2 * sigma * sigma));
                kernelData[y * kernelSize + x] = value;
                sum += value;
            }
        }

        // Normalize kernel
        for (int i = 0; i < kernelData.length; i++) {
            kernelData[i] /= sum;
        }

        Kernel kernel = new Kernel(kernelSize, kernelSize, kernelData);
        ConvolveOp convolveOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

        BufferedImage result = new BufferedImage(image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        return convolveOp.filter(image, result);
    }

    // Manual blur implementation for better control and reliability
    private BufferedImage applyManualBlur(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Use optimized blur for better performance
        if (radius <= 5) {
            return applyFastBlur(original);
        } else {
            return applyGaussianBlur(original);
        }
    }

    private BufferedImage applyFastBlur(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int[] avgColor = calculateAverageColor(original, x, y, radius);
                Color blurredColor = new Color(
                        Math.min(255, Math.max(0, avgColor[0])),
                        Math.min(255, Math.max(0, avgColor[1])),
                        Math.min(255, Math.max(0, avgColor[2]))
                );
                result.setRGB(x, y, blurredColor.getRGB());
            }
        }

        return result;
    }

    private BufferedImage applyGaussianBlur(BufferedImage original) {
        // Apply horizontal blur first
        BufferedImage horizontalBlur = applyHorizontalBlur(original);
        // Then apply vertical blur
        return applyVerticalBlur(horizontalBlur);
    }

    private BufferedImage applyHorizontalBlur(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                long totalRed = 0, totalGreen = 0, totalBlue = 0;
                int count = 0;

                for (int i = Math.max(0, x - radius); i <= Math.min(width - 1, x + radius); i++) {
                    Color pixel = new Color(original.getRGB(i, y));
                    totalRed += pixel.getRed();
                    totalGreen += pixel.getGreen();
                    totalBlue += pixel.getBlue();
                    count++;
                }

                if (count > 0) {
                    Color blurredColor = new Color(
                            (int) (totalRed / count),
                            (int) (totalGreen / count),
                            (int) (totalBlue / count)
                    );
                    result.setRGB(x, y, blurredColor.getRGB());
                }
            }
        }

        return result;
    }

    private BufferedImage applyVerticalBlur(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                long totalRed = 0, totalGreen = 0, totalBlue = 0;
                int count = 0;

                for (int i = Math.max(0, y - radius); i <= Math.min(height - 1, y + radius); i++) {
                    Color pixel = new Color(original.getRGB(x, i));
                    totalRed += pixel.getRed();
                    totalGreen += pixel.getGreen();
                    totalBlue += pixel.getBlue();
                    count++;
                }

                if (count > 0) {
                    Color blurredColor = new Color(
                            (int) (totalRed / count),
                            (int) (totalGreen / count),
                            (int) (totalBlue / count)
                    );
                    result.setRGB(x, y, blurredColor.getRGB());
                }
            }
        }

        return result;
    }

    private int[] calculateAverageColor(BufferedImage image, int centerX, int centerY, int radius) {
        int width = image.getWidth();
        int height = image.getHeight();

        long totalRed = 0, totalGreen = 0, totalBlue = 0;
        int pixelCount = 0;

        // Use square sampling for better performance
        for (int x = Math.max(0, centerX - radius); x <= Math.min(width - 1, centerX + radius); x++) {
            for (int y = Math.max(0, centerY - radius); y <= Math.min(height - 1, centerY + radius); y++) {
                Color pixel = new Color(image.getRGB(x, y));
                totalRed += pixel.getRed();
                totalGreen += pixel.getGreen();
                totalBlue += pixel.getBlue();
                pixelCount++;
            }
        }

        if (pixelCount == 0) pixelCount = 1; // Avoid division by zero

        return new int[] {
                (int) (totalRed / pixelCount),
                (int) (totalGreen / pixelCount),
                (int) (totalBlue / pixelCount)
        };
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

    // Getter for radius
    public int getRadius() {
        return radius;
    }

    // Setter for radius
    public void setRadius(int radius) {
        this.radius = Math.max(1, radius);
    }

    // Method to get blur intensity description
    public String getBlurIntensity() {
        if (radius <= 2) return "Light";
        else if (radius <= 5) return "Medium";
        else if (radius <= 10) return "Strong";
        else return "Very Strong";
    }

    public static void main(String[] args) {
        // Test with your image file
        String imagePath = "/home/saikat/Pictures/Camera/Photo from 2025-04-21 15-36-25.411928.jpeg";  // Change this to your image path
        int blurRadius = 5;           // Change this to desired blur radius

        File inputFile = new File(imagePath);

        if (!inputFile.exists()) {
            System.out.println("Error: File not found at " + imagePath);
            return;
        }

        // Create output filename
        String outputPath = imagePath.substring(0, imagePath.lastIndexOf('.')) +
                "_blur" + imagePath.substring(imagePath.lastIndexOf('.'));
        File outputFile = new File(outputPath);

        try {
            // Copy original to output
            BufferedImage image = ImageIO.read(inputFile);
            String extension = imagePath.substring(imagePath.lastIndexOf('.') + 1);
            ImageIO.write(image, extension, outputFile);

            // Apply blur
            Blur blur = new Blur(blurRadius);
            blur.applyToFile(outputFile);

            System.out.println("Blur applied successfully!");
            System.out.println("Output: " + outputPath);

        } catch (IOException e) {
            System.out.println("Error processing image: " + e.getMessage());
        }
    }
}