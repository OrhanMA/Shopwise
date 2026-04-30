package com.shopwise.backend.testutil;

import java.lang.reflect.Field;

public final class TestEntityUtils {
    private TestEntityUtils() {
    }

    public static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Impossible de préparer l'entité de test.", exception);
        }
    }
}
