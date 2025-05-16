package com.saikat.pixelle.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;


public final class SingletonFactory {
    private static final ConcurrentMap<Class<?>, Object> instances = new ConcurrentHashMap<>();
    
    private SingletonFactory() {
        throw new AssertionError("Not instantiable");
    }

    public static <T> T getInstance(Class<T> clazz, Supplier<T> supplier) {
        Object instance = instances.get(clazz);
        if (instance == null) {
            synchronized (instances) {
                instance = instances.get(clazz);
                if (instance == null) {
                    instance = supplier.get();
                    instances.put(clazz, instance);
                }
            }
        }
        return clazz.cast(instance);
    }

    public static <T> T getInstance(Class<T> clazz) {
        return getInstance(clazz, () -> {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to create instance of " + clazz, e);
            }
        });
    }

    public static boolean hasInstance(Class<?> clazz) {
        return instances.containsKey(clazz);
    }

    public static void removeInstance(Class<?> clazz) {
        instances.remove(clazz);
    }
}