package com.saikat.pixelle.utils;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SepiaToneApp extends Application {

    private ImageView imageView;
    private Image originalImage;

    @Override
    public void start(Stage primaryStage) {
        // 1. Load the original image
        originalImage = new Image("file:/home/saikat/Pictures/Screenshots/_.png"); // !!! IMPORTANT: Replace with your image path

        // 2. Create an ImageView to display the image
        imageView = new ImageView(originalImage);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(800);

        // 3. Create a ComboBox to select effects
        ComboBox<String> effectSelector = new ComboBox<>();
        effectSelector.getItems().addAll(
                "None", "Sepia Tone", "Box Blur", "Motion Blur", "Gaussian Blur",
                "Color Input", "Drop Shadow", "Glow", "Inner Shadow", "Lighting",
                "Perspective Transform", "Reflection", "Blend", "Displacement Map",
                "Bloom", "Color Adjust", "Desaturate" // Added Desaturate for grayscale
        );
        effectSelector.setValue("None"); // Default selection

        // 4. Listen for ComboBox selection changes to apply effects
        effectSelector.setOnAction(event -> applyEffect(effectSelector.getValue()));

        // 5. Layout
        VBox root = new VBox(10, effectSelector, imageView);
        root.setPrefSize(800, 800); // Adjust as needed

        Scene scene = new Scene(root);
        primaryStage.setTitle("JavaFX Image Effects");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void applyEffect(String effectName) {
        Effect effect = null; // Initialize to null for "None"

        switch (effectName) {
            case "Sepia Tone":
                SepiaTone sepiaTone = new SepiaTone();
                sepiaTone.setLevel(0.8);
                effect = sepiaTone;
                break;
            case "Box Blur":
                BoxBlur boxBlur = new BoxBlur();
                boxBlur.setWidth(5);
                boxBlur.setHeight(5);
                boxBlur.setIterations(2);
                effect = boxBlur;
                break;
            case "Motion Blur":
                MotionBlur motionBlur = new MotionBlur();
                motionBlur.setRadius(10);
                motionBlur.setAngle(45);
                effect = motionBlur;
                break;
            case "Gaussian Blur":
                GaussianBlur gaussianBlur = new GaussianBlur();
                gaussianBlur.setRadius(10);
                effect = gaussianBlur;
                break;
            case "Color Input":
                // This replaces the image with a solid color within a shape
                // Less of a "filter" and more of a "fill" effect.
                // Not typically used directly on an ImageView for a filter.
                // For demonstration, let's just make it a simple red block.
                // In a real scenario, it's used as an input for other effects (e.g., Blend)
                effect = new ColorInput(0, 0, imageView.getFitWidth(), imageView.getFitHeight(), javafx.scene.paint.Color.RED);
                break;
            case "Drop Shadow":
                DropShadow dropShadow = new DropShadow();
                dropShadow.setRadius(5.0);
                dropShadow.setOffsetX(3.0);
                dropShadow.setOffsetY(3.0);
                dropShadow.setColor(javafx.scene.paint.Color.BLACK);
                effect = dropShadow;
                break;
            case "Glow":
                Glow glow = new Glow();
                glow.setLevel(0.8); // 0.0-1.0
                effect = glow;
                break;
            case "Inner Shadow":
                InnerShadow innerShadow = new InnerShadow();
                innerShadow.setRadius(5.0);
                innerShadow.setOffsetX(3.0);
                innerShadow.setOffsetY(3.0);
                innerShadow.setColor(javafx.scene.paint.Color.BLACK);
                effect = innerShadow;
                break;
            case "Lighting":
                // Lighting can be complex, often involves a Light source
                Light.Distant light = new Light.Distant();
                light.setAzimuth(-135.0);
                Lighting lighting = new Lighting();
                lighting.setLight(light);
                effect = lighting;
                break;
            case "Perspective Transform":
                PerspectiveTransform pt = new PerspectiveTransform();
                pt.setUlx(0.0);
                pt.setUly(0.0);
                pt.setUrx(imageView.getFitWidth());
                pt.setUry(0.0);
                pt.setLrx(imageView.getFitWidth() * 0.9);
                pt.setLry(imageView.getFitHeight() * 1.1);
                pt.setLlx(imageView.getFitWidth() * 0.1);
                pt.setLly(imageView.getFitHeight() * 1.1);
                effect = pt;
                break;
            case "Reflection":
                Reflection reflection = new Reflection();
                reflection.setFraction(0.7); // How much of the reflection is visible (0.0-1.0)
                reflection.setTopOffset(10.0);
                effect = reflection;
                break;
            case "Blend":
                // Blend combines two inputs. Here, we blend the original image with a blur.
                // For a simpler demo, we'll blend it with a solid color.
                // In practice, you'd use Blend.Mode and two inputs.
                Blend blend = new Blend();
                blend.setMode(BlendMode.MULTIPLY);
                // A more complex example would set inputs:
                // blend.setTopInput(new BoxBlur());
                // blend.setBottomInput(new DropShadow());
                effect = blend;
                break;
            case "Displacement Map":
                // Requires a FloatMap. This is for advanced displacement effects.
                // Not suitable for a simple quick demo without a custom map.
                // For example, to create a ripple effect.
                // Effect effect = new DisplacementMap(floatMap, ...);
                // We'll skip complex setup here.
                System.out.println("Displacement Map requires a custom FloatMap setup.");
                break;
            case "Bloom":
                Bloom bloom = new Bloom();
                bloom.setThreshold(0.1); // Adjust for intensity (0.0-1.0)
                effect = bloom;
                break;
            case "Color Adjust":
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setBrightness(0.2); // Adjust brightness
                colorAdjust.setContrast(0.1);   // Adjust contrast
                colorAdjust.setHue(0.0);        // Adjust hue (e.g., 0.5 for a strong color shift)
                colorAdjust.setSaturation(0.0); // Adjust saturation (0.0 for grayscale, 1.0 for full color)
                effect = colorAdjust;
                break;
            case "Desaturate": // Custom case for grayscale
                ColorAdjust grayscaleAdjust = new ColorAdjust();
                grayscaleAdjust.setSaturation(-1.0); // -1.0 removes all color (grayscale)
                effect = grayscaleAdjust;
                break;
            case "None":
            default:
                // No effect
                break;
        }
        imageView.setEffect(effect);
    }

    public static void main(String[] args) {
        launch(args);
    }
}