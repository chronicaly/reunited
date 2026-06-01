package dev.unityclient.setting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.unityclient.util.MathUtils;

public final class NumberSetting extends Setting<Double> {
    private final double min;
    private final double max;
    private final double increment;
    private final int precision;

    public NumberSetting(String name, String description, double defaultValue, double min, double max, double increment, int precision) {
        super(name, description, defaultValue);
        this.min = min;
        this.max = max;
        this.increment = increment;
        this.precision = precision;
    }

    @Override
    protected Double validate(Double value) {
        double clamped = MathUtils.clamp(value, min, max);
        if (increment > 0.0) {
            clamped = Math.round(clamped / increment) * increment;
        }
        double factor = Math.pow(10.0, precision);
        return Math.round(clamped * factor) / factor;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(get());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element != null && element.isJsonPrimitive()) {
            set(element.getAsDouble());
        }
    }

    public double min() {
        return min;
    }

    public double max() {
        return max;
    }

    public int precision() {
        return precision;
    }
}
