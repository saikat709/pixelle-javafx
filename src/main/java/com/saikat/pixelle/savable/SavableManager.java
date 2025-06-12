package com.saikat.pixelle.savable;

import java.util.HashMap;

public class SavableManager {
    private final HashMap<Class<?>, Savable> store;

    public SavableManager() {
        store = new HashMap<Class<?>, Savable>();
    }

    public Savable getSavableClass(Class<?> cls) {
        Savable instance = store.get(cls);
        if (instance == null) {
            try {
                instance = (Savable) cls.getDeclaredConstructor().newInstance();
                Savable loadedInstance = instance.loadFromDevice();
                if ( loadedInstance != null ) instance = loadedInstance;
                store.put(cls, instance);
            } catch (Exception exc){
                throw new IllegalArgumentException(exc.getMessage());
            }
        }
        return instance;
    }
}
