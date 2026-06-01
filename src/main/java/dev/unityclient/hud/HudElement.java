package dev.unityclient.hud;

import net.minecraft.client.gui.DrawContext;

public abstract class HudElement {
    private final String name;
    private int x;
    private int y;
    private boolean enabled = true;
    private double scale = 1.0;
    private double opacity = 1.0;

    protected HudElement(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public abstract void render(DrawContext context);

    public boolean hit(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width() && mouseY >= y && mouseY <= y + height();
    }

    public int width() {
        return Math.max(60, name.length() * 6 + 8);
    }

    public int height() {
        return 14;
    }

    public String name() {
        return name;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean enabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public double scale() {
        return scale;
    }

    public double opacity() {
        return opacity;
    }
}
