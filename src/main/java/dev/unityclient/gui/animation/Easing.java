package dev.unityclient.gui.animation;

public final class Easing {
    private Easing() {
    }

    public static double outCubic(double value) {
        double t = 1.0 - value;
        return 1.0 - t * t * t;
    }
}
