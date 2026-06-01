package dev.unityclient.render;

import net.minecraft.client.gui.DrawContext;

public final class Renderer2D {
    public void rect(DrawContext context, int x, int y, int width, int height, int color) {
        context.fill(x, y, x + width, y + height, color);
    }
}
