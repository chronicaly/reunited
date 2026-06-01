package dev.unityclient.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public final class KeybindSetting extends Setting<Integer> {
    public KeybindSetting(String name, String description, int defaultValue) {
        super(name, description, defaultValue);
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(get());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element != null && element.isJsonPrimitive()) {
            set(element.getAsInt());
        }
    }
}
