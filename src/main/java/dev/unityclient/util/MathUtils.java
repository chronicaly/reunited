package dev.unityclient.util;

public final class MathUtils {
    private MathUtils() {
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
