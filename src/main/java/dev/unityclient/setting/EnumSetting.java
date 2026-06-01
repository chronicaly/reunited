package dev.unityclient.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public final class EnumSetting<T extends Enum<T>> extends Setting<T> {
    private final Class<T> type;

    public EnumSetting(String name, String description, T defaultValue, Class<T> type) {
        super(name, description, defaultValue);
        this.type = type;
    }

    public T[] values() {
        return type.getEnumConstants();
    }

    public void next() {
        T[] values = values();
        int next = (get().ordinal() + 1) % values.length;
        set(values[next]);
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(get().name());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element == null || !element.isJsonPrimitive()) {
            return;
        }
        try {
            set(Enum.valueOf(type, element.getAsString()));
        } catch (IllegalArgumentException ignored) {
            reset();
        }
    }
}
