package dev.unityclient.module;

import dev.unityclient.setting.BoolSetting;
import dev.unityclient.setting.EnumSetting;
import dev.unityclient.setting.NumberSetting;
import dev.unityclient.setting.SettingGroup;

public class SimpleModule extends Module {
    public enum SafetyMode {
        PASSIVE,
        VISUAL_ONLY,
        ASSISTIVE
    }

    public SimpleModule(String name, String description, Category category, SafetyMode mode) {
        super(name, description, category);
        SettingGroup behavior = group("Behavior");
        behavior.add(new EnumSetting<>("Mode", "Conservative behavior mode.", mode, SafetyMode.class));
        behavior.add(new BoolSetting("Notify", "Show a notification when toggled.", true));
        behavior.add(new NumberSetting("Delay", "Tick delay for safe repeated actions.", 4.0, 0.0, 40.0, 1.0, 0));
    }
}
