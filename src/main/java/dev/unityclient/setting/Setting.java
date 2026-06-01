package dev.unityclient.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class Setting<T> {
    private final String name;
    private final String description;
    private final T defaultValue;
    private Supplier<Boolean> visible = () -> true;
    private Consumer<T> changed = ignored -> {
    };
    private T value;

    protected Setting(String name, String description, T defaultValue) {
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        T validated = validate(value);
        if (!Objects.equals(this.value, validated)) {
            this.value = validated;
            changed.accept(validated);
        }
    }

    public T defaultValue() {
        return defaultValue;
    }

    public void reset() {
        set(defaultValue);
    }

    public boolean visible() {
        return visible.get();
    }

    public Setting<T> visibleWhen(Supplier<Boolean> visible) {
        this.visible = visible;
        return this;
    }

    public Setting<T> onChanged(Consumer<T> changed) {
        this.changed = changed;
        return this;
    }

    protected T validate(T value) {
        return value;
    }

    public JsonElement toJson() {
        T current = get();
        if (current instanceof Number number) {
            return new JsonPrimitive(number);
        }
        if (current instanceof Boolean bool) {
            return new JsonPrimitive(bool);
        }
        return new JsonPrimitive(String.valueOf(current));
    }

    public abstract void fromJson(JsonElement element);
}
