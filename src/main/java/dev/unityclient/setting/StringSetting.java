package dev.unityclient.setting;

import com.google.gson.JsonElement;

public final class StringSetting extends Setting<String> {
    public StringSetting(String name, String description, String defaultValue) {
        super(name, description, defaultValue);
    }

    @Override
    protected String validate(String value) {
        return value == null ? "" : value;
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element != null && element.isJsonPrimitive()) {
            set(element.getAsString());
        }
    }
}
