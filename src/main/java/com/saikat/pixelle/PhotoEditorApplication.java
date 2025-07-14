package com.saikat.pixelle;

import com.saikat.pixelle.constants.Screens;
import com.saikat.pixelle.managers.ScreenManager;
import com.saikat.pixelle.savable.AppSettings;
import com.saikat.pixelle.savable.SavableManager;
import com.saikat.pixelle.utils.OpenUtil;
import com.saikat.pixelle.utils.SingletonFactoryUtil;
import javafx.application.Application;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.stage.Stage;

import java.lang.reflect.Method;

public class PhotoEditorApplication extends Application {

    @Override
    public void start(Stage stage) {

        stage.setTitle("Pixelle");
        stage.setMinWidth(760);
        stage.setMinHeight(520);


        Effect a = new GaussianBlur();
        tryChainEffect(a, new BoxBlur());
        System.out.println(tryGetInputEffect(a).toString());

        System.setProperty("glass.win.uiScale", "100%"); // Force Glass to use 100% scale
        System.setProperty("prism.forcehiDPI", "false"); // Disable HiDPI

        OpenUtil open = SingletonFactoryUtil.getInstance(OpenUtil.class);
        open.initializeWithHostServices(getHostServices());

        ScreenManager screenManager = SingletonFactoryUtil.getInstance(ScreenManager.class);
        screenManager.initialize(stage);

        screenManager.startApplication();
    }


    public static Effect tryGetInputEffect(Effect effect) {
        try {
            Method getInput = effect.getClass().getMethod("getInput");
            return (Effect) getInput.invoke(effect);
        } catch (Exception e) {
            System.err.println("Cannot get input from " + effect.getClass().getSimpleName());
            return null;
        }
    }

    public static void tryChainEffect(Effect target, Effect input) {
        try {
            Method setInput = target.getClass().getMethod("setInput", Effect.class);
            setInput.invoke(target, input);
        } catch (Exception e) {
            // Effect doesn't support chaining or failed to chain
            System.err.println("Cannot chain " + target.getClass().getSimpleName());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}