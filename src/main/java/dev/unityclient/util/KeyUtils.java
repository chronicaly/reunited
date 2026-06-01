package dev.unityclient.util;

import org.lwjgl.glfw.GLFW;

public final class KeyUtils {
    private KeyUtils() {
    }

    public static String name(int key) {
        if (key < 0) {
            return "None";
        }
        String name = GLFW.glfwGetKeyName(key, 0);
        return name == null ? String.valueOf(key) : name.toUpperCase();
    }
}
