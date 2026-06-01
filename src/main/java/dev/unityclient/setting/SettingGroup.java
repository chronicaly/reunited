package dev.unityclient.setting;

import java.util.ArrayList;
import java.util.List;

public final class SettingGroup {
    private final String name;
    private final List<Setting<?>> settings = new ArrayList<>();

    public SettingGroup(String name) {
        this.name = name;
    }

    public void add(Setting<?> setting) {
        settings.add(setting);
    }

    public String name() {
        return name;
    }

    public List<Setting<?>> settings() {
        return List.copyOf(settings);
    }
}
