package dev.unityclient.hud.elements;

import dev.unityclient.UnityClient;
import dev.unityclient.gui.GuiTheme;
import dev.unityclient.hud.HudElement;
import dev.unityclient.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public final class ArrayListHud extends HudElement {
    public ArrayListHud() {
        super("Array List", 8, 26);
    }

    @Override
    public void render(DrawContext context) {
        int y = y();
        for (Module module : UnityClient.INSTANCE.modules().all()) {
            if (module.isEnabled() && module.visibleSetting().get()) {
                context.drawText(MinecraftClient.getInstance().textRenderer, Text.literal(module.name()), x(), y, GuiTheme.DEFAULT_ACCENT, false);
                y += 10;
            }
        }
    }
}
