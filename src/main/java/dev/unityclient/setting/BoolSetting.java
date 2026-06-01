package dev.unityclient.setting;

import com.google.gson.JsonElement;

public final class BoolSetting extends Setting<Boolean> {
    public BoolSetting(String name, String description, boolean defaultValue) {
        super(name, description, defaultValue);
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element != null && element.isJsonPrimitive()) {
            set(element.getAsBoolean());
        }
    }
}
