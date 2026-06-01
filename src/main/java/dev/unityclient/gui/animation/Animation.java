package dev.unityclient.gui.animation;

public final class Animation {
    private double value;

    public double update(double target, double speed) {
        value += (target - value) * Math.min(1.0, speed);
        return value;
    }

    public double value() {
        return value;
    }
}
