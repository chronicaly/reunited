package dev.unityclient.gui.animation;

public final class SmoothFloat {
    private float value;

    public float update(float target, float speed) {
        value += (target - value) * Math.min(1.0f, speed);
        return value;
    }
}
