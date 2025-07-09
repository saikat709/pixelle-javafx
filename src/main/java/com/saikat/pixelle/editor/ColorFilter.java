//package com.saikat.pixelle.editor;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.awt.image.ConvolveOp;
//import java.awt.image.Kernel;
//import java.awt.Color;
//import java.io.File;
//import java.io.IOException;
//
//public class ColorFilter implements EditorCommand {
//
//    public enum FilterType {
//        GRAYSCALE,
//        SEPIA,
//        INVERT,
//        RED_TINT,
//        GREEN_TINT,
//        BLUE_TINT,
//        BINARIZE,
//        BRIGHTNESS_UP,
//        BRIGHTNESS_DOWN,
//        CONTRAST_UP,
//        CONTRAST_DOWN,
//        SHARPEN,
//        EDGE_DETECTION
//    }
//
//    private final FilterType filterType;
//
//    public ColorFilter(FilterType filterType) {
//        this.filterType = filterType;
//    }
//
//    @Override
//    public void applyToFile(BufferedImage file) {
//        try {
//            BufferedImage img = ImageIO.read(file);
//            BufferedImage filteredImage = applyFilter(img);
//
//            String outputPath = file.getParent() + File.separator + "filtered_" + filterType.name().toLowerCase() + "_" + file.getName();
//            ImageIO.write(filteredImage, "jpeg", new File(outputPath));
//            System.out.println("Filter applied. Saved to: " + outputPath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void removeAppliedEdit(File file) {
//        System.out.println("This demo doesn't support removing filters. Keep the original file for backup.");
//    }
//
//    private BufferedImage applyFilter(BufferedImage img) {
//        int width = img.getWidth();
//        int height = img.getHeight();
//        BufferedImage output = new BufferedImage(width, height, img.getType());
//
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                Color original = new Color(img.getRGB(x, y));
//                int r = original.getRed();
//                int g = original.getGreen();
//                int b = original.getBlue();
//
//                Color newColor = switch (filterType) {
//                    case GRAYSCALE -> {
//                        int gray = (r + g + b) / 3;
//                        yield new Color(gray, gray, gray);
//                    }
//                    case SEPIA -> {
//                        int tr = (int)(0.393 * r + 0.769 * g + 0.189 * b);
//                        int tg = (int)(0.349 * r + 0.686 * g + 0.168 * b);
//                        int tb = (int)(0.272 * r + 0.534 * g + 0.131 * b);
//                        yield new Color(Math.min(255, tr), Math.min(255, tg), Math.min(255, tb));
//                    }
//                    case INVERT -> {
//                        yield new Color(255 - r, 255 - g, 255 - b);
//                    }
//                    case RED_TINT -> {
//                        yield new Color(r, g / 3, b / 3);
//                    }
//                    case GREEN_TINT -> {
//                        yield new Color(r / 3, g, b / 3);
//                    }
//                    case BLUE_TINT -> {
//                        yield new Color(r / 3, g / 3, b);
//                    }
//                    case BINARIZE -> {
//                        int avg = (r + g + b) / 3;
//                        yield avg > 128 ? Color.WHITE : Color.BLACK;
//                    }
//                    case BRIGHTNESS_UP -> new Color(clamp(r + 20), clamp(g + 20), clamp(b + 20));
//                    case BRIGHTNESS_DOWN -> new Color(clamp(r - 20), clamp(g - 20), clamp(b - 20));
//                    case CONTRAST_UP -> adjustContrast(r, g, b, 1.2 + 20 * 0.2);
//                    case CONTRAST_DOWN -> adjustContrast(r, g, b, 0.8 - 20 * 0.2);
//                    default -> original;
//                };
//
//                output.setRGB(x, y, newColor.getRGB());
//            }
//        }
//
//        if  (filterType == FilterType.SHARPEN) {
//            return applyConvolution(output, getSharpenKernel());
//        } else if (filterType == FilterType.EDGE_DETECTION) {
//            return applyConvolution(output, getEdgeDetectionKernel());
//        }
//
//        return output;
//    }
//
//    private Color adjustContrast(int r, int g, int b, double factor) {
//        int nr = clamp((int)((r - 128) * factor + 128));
//        int ng = clamp((int)((g - 128) * factor + 128));
//        int nb = clamp((int)((b - 128) * factor + 128));
//        return new Color(nr, ng, nb);
//    }
//
//    private int clamp(int val) {
//        return Math.max(0, Math.min(255, val));
//    }
//
//    private BufferedImage applyConvolution(BufferedImage img, Kernel kernel) {
//        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
//        return op.filter(img, null);
//    }
//
//    private Kernel getBlurKernel() {
//        float[] matrix = new float[]{
//                1f / 9, 1f / 9, 1f / 9,
//                1f / 9, 1f / 9, 1f / 9,
//                1f / 9, 1f / 9, 1f / 9
//        };
//        return new Kernel(3, 3, matrix);
//    }
//
//    private Kernel getSharpenKernel() {
//        float[] matrix = new float[]{
//                0, -1,  0,
//                -1,  5, -1,
//                0, -1,  0
//        };
//        return new Kernel(3, 3, matrix);
//    }
//
//    private Kernel getEdgeDetectionKernel() {
//        float[] matrix = new float[]{
//                -1, -1, -1,
//                -1,  8, -1,
//                -1, -1, -1
//        };
//        return new Kernel(3, 3, matrix);
//    }
//
//    public static void main(String[] args) {
//        File input = new File("/home/saikat/Pictures/Camera/Photo from 2025-04-21 15-36-25.411928.jpeg");
//
//        new ColorFilter(FilterType.GRAYSCALE).applyToFile(input);
//        new ColorFilter(FilterType.SEPIA).applyToFile(input);
//        new ColorFilter(FilterType.INVERT).applyToFile(input);
//        new ColorFilter(FilterType.RED_TINT).applyToFile(input);
//        new ColorFilter(FilterType.BRIGHTNESS_UP).applyToFile(input);
//        new ColorFilter(FilterType.SHARPEN).applyToFile(input);
//    }
//}
