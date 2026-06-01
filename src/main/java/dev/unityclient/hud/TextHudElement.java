package dev.unityclient.hud;

import dev.unityclient.gui.GuiTheme;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class TextHudElement extends HudElement {
    private final String value;

    public TextHudElement(String name, String value, int x, int y) {
        super(name, x, y);
        this.value = value;
    }

    @Override
    public void render(DrawContext context) {
        context.fill(x(), y(), x() + width(), y() + height(), 0x55111111);
        context.drawText(MinecraftClient.getInstance().textRenderer, Text.literal(value), x() + 4, y() + 3, GuiTheme.TEXT, false);
    }
}
