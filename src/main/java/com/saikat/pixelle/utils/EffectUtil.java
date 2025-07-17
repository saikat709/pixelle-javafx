package com.saikat.pixelle.utils;

import javafx.scene.effect.Effect;
import java.lang.reflect.Method;

public class EffectUtil {

    public static Effect tryGetInputEffect(Effect effect) {
        try {
            Method getInput = effect.getClass().getMethod("getInput");
            return (Effect) getInput.invoke(effect);
        } catch (Exception e) {
            System.err.println("Cannot get input from " + effect.getClass().getSimpleName());
            return null;
        }
    }

    public static Effect tryChainEffect(Effect target, Effect input) {
        try {
            Method setInput = target.getClass().getMethod("setInput", Effect.class);
            setInput.invoke(target, input);
        } catch (Exception e) {
            System.err.println("Cannot chain " + target.getClass().getSimpleName());
        }
        return target;
    }
}
