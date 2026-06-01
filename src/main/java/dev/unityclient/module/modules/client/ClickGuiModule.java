package dev.unityclient.module.modules.client;

import dev.unityclient.gui.GuiTheme;
import dev.unityclient.module.Category;
import dev.unityclient.module.Module;
import dev.unityclient.setting.BoolSetting;
import dev.unityclient.setting.ColorSetting;
import dev.unityclient.setting.NumberSetting;
import dev.unityclient.setting.SettingGroup;

public final class ClickGuiModule extends Module {
    public final NumberSetting scale = new NumberSetting("Scale", "GUI scale independent from Minecraft GUI scale.", 1.0, 0.6, 2.0, 0.05, 2);
    public final NumberSetting animationSpeed = new NumberSetting("Animation Speed", "Base GUI animation speed.", 1.0, 0.2, 4.0, 0.1, 1);
    public final NumberSetting panelOpacity = new NumberSetting("Panel Opacity", "Panel background opacity.", 0.86, 0.2, 1.0, 0.01, 2);
    public final BoolSetting outline = new BoolSetting("Outline", "Draw panel and module outlines.", true);
    public final ColorSetting accent = new ColorSetting("Accent", "Default accent color.", GuiTheme.DEFAULT_ACCENT);
    public final BoolSetting blocky = new BoolSetting("Blocky", "Use blocky corners instead of rounded styling.", true);

    public ClickGuiModule() {
        super("ClickGUI", "Polished draggable panel interface.", Category.CLIENT);
        SettingGroup style = group("Style");
        style.add(scale);
        style.add(animationSpeed);
        style.add(panelOpacity);
        style.add(outline);
        style.add(accent);
        style.add(blocky);
    }
}
