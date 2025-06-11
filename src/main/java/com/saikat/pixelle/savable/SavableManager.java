package com.saikat.pixelle.savable;

import java.util.HashMap;

public class SavableManager {
    private final HashMap<String, Savable> store;

    public SavableManager() {
        store = new HashMap<String, Savable>();
    }

    public Savable getSavableClass(String name) {
        if ( !store.containsKey(name) ) {
            store.put(name, new Savable());
        }
        return store.get(name);
    }
}
