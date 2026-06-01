package dev.unityclient.gui;

import dev.unityclient.UnityClient;
import dev.unityclient.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class HudEditorScreen extends Screen {
    private HudElement dragging;
    private int dragX;
    private int dragY;

    public HudEditorScreen() {
        super(Text.literal("Unity HUD Editor"));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0x66000000);
        UnityClient.INSTANCE.hud().renderEditor(context, mouseX, mouseY);
        context.drawText(textRenderer, Text.literal("HUD Editor"), 8, 8, GuiTheme.TEXT, false);
        context.drawText(textRenderer, Text.literal("Drag elements, press Esc to save"), 8, 20, GuiTheme.MUTED, false);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        double mouseX = click.x();
        double mouseY = click.y();
        dragging = UnityClient.INSTANCE.hud().hit((int) mouseX, (int) mouseY);
        if (dragging != null) {
            dragX = (int) mouseX - dragging.x();
            dragY = (int) mouseY - dragging.y();
            return true;
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseDragged(Click click, double deltaX, double deltaY) {
        if (dragging != null) {
            dragging.setPosition(Math.max(0, (int) click.x() - dragX), Math.max(0, (int) click.y() - dragY));
            return true;
        }
        return super.mouseDragged(click, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(Click click) {
        dragging = null;
        return super.mouseReleased(click);
    }

    @Override
    public void close() {
        UnityClient.INSTANCE.hud().save();
        MinecraftClient.getInstance().setScreen(null);
    }
}
