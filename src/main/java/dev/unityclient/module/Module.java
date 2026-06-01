package dev.unityclient.module;

import dev.unityclient.UnityClient;
import dev.unityclient.setting.BoolSetting;
import dev.unityclient.setting.KeybindSetting;
import dev.unityclient.setting.Setting;
import dev.unityclient.setting.SettingGroup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Module {
    private final String name;
    private final String description;
    private final Category category;
    private final List<SettingGroup> groups = new ArrayList<>();
    private final BoolSetting visible = new BoolSetting("Visible", "Show in the array list.", true);
    private final KeybindSetting keybind = new KeybindSetting("Keybind", "Toggle key for this module.", -1);
    private boolean enabled;

    protected Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        SettingGroup general = new SettingGroup("General");
        general.add(visible);
        general.add(keybind);
        groups.add(general);
    }

    protected SettingGroup group(String name) {
        SettingGroup group = new SettingGroup(name);
        groups.add(group);
        return group;
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }
        this.enabled = enabled;
        try {
            if (enabled) {
                onEnable();
            } else {
                onDisable();
            }
            onToggle();
        } catch (RuntimeException ex) {
            this.enabled = false;
            UnityClient.LOGGER.error("Module {} failed while toggling", name, ex);
            UnityClient.INSTANCE.notifications().error(name + " disabled after an error");
        }
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public Category category() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public BoolSetting visibleSetting() {
        return visible;
    }

    public KeybindSetting keybind() {
        return keybind;
    }

    public List<SettingGroup> groups() {
        return Collections.unmodifiableList(groups);
    }

    public List<Setting<?>> getSettings() {
        List<Setting<?>> settings = new ArrayList<>();
        for (SettingGroup group : groups) {
            settings.addAll(group.settings());
        }
        return settings;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onToggle() {
    }

    public void onTick() {
    }

    public void onRender2D() {
    }

    public void onRender3D() {
    }

    public void onPacketSend(Object packet) {
    }

    public void onPacketReceive(Object packet) {
    }
}
