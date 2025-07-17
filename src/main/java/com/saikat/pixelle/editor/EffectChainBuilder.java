package com.saikat.pixelle.editor;

import com.saikat.pixelle.utils.EffectUtil;
import javafx.scene.effect.Effect;

import java.util.ArrayList;
import java.util.List;

public class EffectChainBuilder {
    private List<Effect> effects;

    public EffectChainBuilder() {
        effects = new ArrayList<>();
    }

    public Effect addEffect(Effect effect) {
        Effect toRemove = null;
        for (Effect e : effects) {
            if ( e.getClass().getName().equals(effect.getClass().getName()) ) {
                toRemove = e;
                break;
            }
        }
        effects.add(effect);
        if (toRemove != null) effects.remove(toRemove);
        return getCurrentEffect();
    }

    public Effect getAppliedEffect(Effect effect) {

        System.out.println(effect.getClass().getName());
        if ( effects.isEmpty() ) return effect;

        Effect eff = null;
        for( int i = 0; i < effects.size(); i++ ){
            if ( !effects.get(i).getClass().getName().equals(effect.getClass().getName()) ) {
                Effect curr = effects.get(i);
                eff = EffectUtil.tryChainEffect(curr, eff);
            }
        }

        System.out.println("Effect chain: " + effects.size());
        return EffectUtil.tryChainEffect(effect, eff);
    }

    public Effect getCurrentEffect(){
        Effect eff = null;
        for( int i = 0; i < effects.size(); i++ ){
            Effect curr =  effects.get(i);
            eff = EffectUtil.tryChainEffect(curr, eff);
        }
        return eff;
    }
}
