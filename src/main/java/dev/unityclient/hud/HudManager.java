package dev.unityclient.hud;

import com.google.gson.JsonObject;
import dev.unityclient.UnityClient;
import dev.unityclient.config.JsonUtils;
import dev.unityclient.hud.elements.ArrayListHud;
import dev.unityclient.hud.elements.ArmorHud;
import dev.unityclient.hud.elements.CoordinatesHud;
import dev.unityclient.hud.elements.DirectionHud;
import dev.unityclient.hud.elements.FpsHud;
import dev.unityclient.hud.elements.NotificationHud;
import dev.unityclient.hud.elements.PingHud;
import dev.unityclient.hud.elements.PotionHud;
import dev.unityclient.hud.elements.SpeedHud;
import dev.unityclient.hud.elements.TotemCountHud;
import dev.unityclient.hud.elements.WatermarkHud;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.DrawContext;

public final class HudManager {
    private final List<HudElement> elements = new ArrayList<>();

    public void init() {
        if (!elements.isEmpty()) {
            return;
        }
        elements.add(new WatermarkHud());
        elements.add(new ArrayListHud());
        elements.add(new FpsHud());
        elements.add(new PingHud());
        elements.add(new CoordinatesHud());
        elements.add(new DirectionHud());
        elements.add(new SpeedHud());
        elements.add(new ArmorHud());
        elements.add(new PotionHud());
        elements.add(new TotemCountHud());
        elements.add(new NotificationHud());
    }

    public void render(DrawContext context) {
        for (HudElement element : elements) {
            if (element.enabled()) {
                element.render(context);
            }
        }
        UnityClient.INSTANCE.notifications().render(context);
    }

    public void renderEditor(DrawContext context, int mouseX, int mouseY) {
        for (HudElement element : elements) {
            element.render(context);
            if (element.hit(mouseX, mouseY)) {
                context.fill(element.x(), element.y(), element.x() + element.width(), element.y() + element.height(), 0x3355FF55);
            }
        }
    }

    public HudElement hit(int x, int y) {
        for (int i = elements.size() - 1; i >= 0; i--) {
            HudElement element = elements.get(i);
            if (element.hit(x, y)) {
                return element;
            }
        }
        return null;
    }

    public void save() {
        JsonObject root = new JsonObject();
        for (HudElement element : elements) {
            JsonObject data = new JsonObject();
            data.addProperty("x", element.x());
            data.addProperty("y", element.y());
            data.addProperty("enabled", element.enabled());
            root.add(element.name(), data);
        }
        JsonUtils.write(UnityClient.INSTANCE.config().path("hud.json"), root);
    }

    public void load() {
        JsonObject root = JsonUtils.readObject(UnityClient.INSTANCE.config().path("hud.json"));
        for (HudElement element : elements) {
            if (!root.has(element.name()) || !root.get(element.name()).isJsonObject()) {
                continue;
            }
            JsonObject data = root.getAsJsonObject(element.name());
            if (data.has("x") && data.has("y")) {
                element.setPosition(data.get("x").getAsInt(), data.get("y").getAsInt());
            }
            if (data.has("enabled")) {
                element.setEnabled(data.get("enabled").getAsBoolean());
            }
        }
    }
}
