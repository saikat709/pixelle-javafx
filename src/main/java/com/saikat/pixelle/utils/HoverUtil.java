package com.saikat.pixelle.utils;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

public class HoverUtil {

    public static void applySimpleHover(Node node) {
        node.setOnMouseEntered(e -> node.setStyle("-fx-background-color: #e0e0e0;"));
        node.setOnMouseExited(e -> node.setStyle(""));
    }

    public static void applyGlowHover(Node node) {
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.LIGHTBLUE);
        shadow.setRadius(10);

        node.setOnMouseEntered(e -> node.setEffect(shadow));
        node.setOnMouseExited(e -> node.setEffect(null));
    }

    public static void applyScaleHover(Node node, double scale) {
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(150), node);
        scaleIn.setToX(scale);
        scaleIn.setToY(scale);

        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(150), node);
        scaleOut.setToX(1);
        scaleOut.setToY(1);

        node.setOnMouseEntered(e -> scaleIn.playFromStart());
        node.setOnMouseExited(e -> scaleOut.playFromStart());
    }

    public static void applyComboHover(Node node) {
        DropShadow shadow = new DropShadow(10, Color.GRAY);

        node.setOnMouseEntered((MouseEvent e) -> {
            node.setStyle("-fx-background-color: #f0f0f0; -fx-cursor: hand;");
            node.setEffect(shadow);
        });

        node.setOnMouseExited((MouseEvent e) -> {
            node.setStyle("");
            node.setEffect(null);
        });
    }

    public static void applyPulseHover(Node node) {
        ScaleTransition pulse = new ScaleTransition(Duration.millis(600), node);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.05);
        pulse.setToY(1.05);
        pulse.setCycleCount(ScaleTransition.INDEFINITE);
        pulse.setAutoReverse(true);

        node.setOnMouseEntered(e -> pulse.playFromStart());
        node.setOnMouseExited(e -> {
            pulse.stop();
            node.setScaleX(1.0);
            node.setScaleY(1.0);
        });
    }

    public static void applyRotateHover(Node node) {
        node.setOnMouseEntered(e -> node.setRotate(5));
        node.setOnMouseExited(e -> node.setRotate(0));
    }

    public static void applyFadeHover(Node node) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), node);
        fadeIn.setToValue(0.7);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), node);
        fadeOut.setToValue(1.0);

        node.setOnMouseEntered(e -> fadeIn.playFromStart());
        node.setOnMouseExited(e -> fadeOut.playFromStart());
    }

    public static void applyBorderGlowHover(Node node) {
        node.setOnMouseEntered(e -> node.setStyle("-fx-border-color: #00ccff; -fx-border-width: 2;"));
        node.setOnMouseExited(e -> node.setStyle(""));
    }

    public static void applyLiftHover(Node node) {
        TranslateTransition up = new TranslateTransition(Duration.millis(150), node);
        up.setToY(-3);

        TranslateTransition down = new TranslateTransition(Duration.millis(150), node);
        down.setToY(0);

        node.setOnMouseEntered(e -> up.playFromStart());
        node.setOnMouseExited(e -> down.playFromStart());
    }

}
