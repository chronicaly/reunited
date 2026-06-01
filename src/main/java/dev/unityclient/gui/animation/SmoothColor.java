package dev.unityclient.gui.animation;

public final class SmoothColor {
    private int value;

    public int update(int target) {
        value = target;
        return value;
    }
}
