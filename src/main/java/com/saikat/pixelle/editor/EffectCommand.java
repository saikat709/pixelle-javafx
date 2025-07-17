package com.saikat.pixelle.editor;

import javafx.scene.effect.Effect;

public class EffectCommand extends Command {
    private Effect currentEffect;
    private Effect previousEffect;

    public EffectCommand(Effect currentEffect, Effect previousEffect) {
        this.currentEffect = currentEffect;
        this.previousEffect = previousEffect;
    }

    public Effect getCurrentEffect() {
        return currentEffect;
    }

    public void setCurrentEffect(Effect currentEffect) {
        this.currentEffect = currentEffect;
    }

    public Effect getPreviousEffect() {
        return previousEffect;
    }

    public void setPreviousEffect(Effect previousEffect) {
        this.previousEffect = previousEffect;
    }
}
