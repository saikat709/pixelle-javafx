package com.saikat.pixelle.editor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;

public class Frame implements EditorCommand {

    private final Color frameColor;
    private final int frameWidth;
    private final int cornerRadius;

    public Frame(Color color, int width, int radius) {
        this.frameColor = color;
        this.frameWidth = width;
        this.cornerRadius = radius;
    }

    @Override
    public void applyToFile(File file) {
        try {
            BufferedImage input = ImageIO.read(file);

            int imgW = input.getWidth();
            int imgH = input.getHeight();

            // Create output image larger by frame width on all sides
            int outW = imgW + 2 * frameWidth;
            int outH = imgH + 2 * frameWidth;
            BufferedImage output = new BufferedImage(outW, outH, input.getType());

            Graphics2D g = output.createGraphics();

            // Enable smooth edges
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Fill entire image with frame color (including corners)
            g.setColor(frameColor);
            g.fillRect(0, 0, outW, outH);

            if (cornerRadius > 0) {
                // Cut out inner rounded rectangle from frame
                g.setComposite(java.awt.AlphaComposite.Clear);
                g.fill(new RoundRectangle2D.Double(frameWidth, frameWidth, imgW, imgH, cornerRadius*2, cornerRadius*2));
                g.setComposite(java.awt.AlphaComposite.SrcOver);
            } else {
                // No rounded corners, just clear inner rectangle
                g.clearRect(frameWidth, frameWidth, imgW, imgH);
            }

            // Draw original image on top inside the frame
            g.drawImage(input, frameWidth, frameWidth, null);

            g.dispose();

            String outputPath = file.getParent() + File.separator + "framed_" + file.getName();
            ImageIO.write(output, "jpeg", new File(outputPath));
            System.out.println("✅ Frame applied. Saved to: " + outputPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeAppliedEdit(File file) {
        System.out.println("Frame removal is not supported. Keep original.");
    }

    public static void main(String[] args) {
        File input = new File("/home/saikat/Pictures/Camera/Photo from 2025-04-21 15-36-25.411928.jpeg");

        Frame frame = new Frame(new Color(255, 0, 0), 40, 30);
        frame.applyToFile(input);
    }
}
