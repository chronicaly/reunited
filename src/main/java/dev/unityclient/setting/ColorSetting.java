package dev.unityclient.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.unityclient.render.ColorUtils;

public final class ColorSetting extends Setting<Integer> {
    public ColorSetting(String name, String description, int defaultValue) {
        super(name, description, defaultValue);
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(ColorUtils.toHex(get()));
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element != null && element.isJsonPrimitive()) {
            set(ColorUtils.fromHex(element.getAsString(), defaultValue()));
        }
    }
}
