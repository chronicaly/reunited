package dev.unityclient.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public final class FontRenderer {
    public void text(DrawContext context, String text, int x, int y, int color) {
        context.drawText(MinecraftClient.getInstance().textRenderer, Text.literal(text), x, y, color, false);
    }
}
