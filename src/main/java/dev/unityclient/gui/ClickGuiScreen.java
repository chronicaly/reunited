package dev.unityclient.gui;

import dev.unityclient.UnityClient;
import dev.unityclient.module.Category;
import dev.unityclient.module.Module;
import dev.unityclient.module.modules.client.ClickGuiModule;
import dev.unityclient.setting.BoolSetting;
import dev.unityclient.setting.ColorSetting;
import dev.unityclient.setting.EnumSetting;
import dev.unityclient.setting.KeybindSetting;
import dev.unityclient.setting.NumberSetting;
import dev.unityclient.setting.Setting;
import dev.unityclient.setting.StringSetting;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public final class ClickGuiScreen extends Screen {
    private static final int PANEL_WIDTH = 138;
    private static final int HEADER_HEIGHT = 18;
    private static final int ROW_HEIGHT = 16;

    private final Map<Category, PanelState> panels = new EnumMap<>(Category.class);
    private final Map<Module, Boolean> expanded = new HashMap<>();
    private String search = "";
    private Module binding;
    private PanelState dragging;
    private double dragOffsetX;
    private double dragOffsetY;

    public ClickGuiScreen() {
        super(Text.literal("Unity Client"));
        int x = 16;
        int y = 24;
        for (Category category : Category.values()) {
            panels.put(category, new PanelState(x, y));
            x += PANEL_WIDTH + 8;
            if (x > 760) {
                x = 16;
                y += 190;
            }
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        double scale = scale();
        int sx = (int) (mouseX / scale);
        int sy = (int) (mouseY / scale);
        context.fill(0, 0, width, height, 0x77000000);
        drawSearch(context);
        for (Category category : Category.values()) {
            drawPanel(context, category, panels.get(category), sx, sy, scale);
        }
        if (binding != null) {
            drawText(context, "Press a key for " + binding.name() + " (Esc clears)", 10, height - 18, GuiTheme.TEXT);
        }
        super.render(context, mouseX, mouseY, delta);
    }

    private void drawSearch(DrawContext context) {
        context.fill(8, 6, 224, 22, GuiTheme.PANEL_HEADER);
        context.fill(8, 21, 224, 22, GuiTheme.DEFAULT_ACCENT);
        drawText(context, search.isBlank() ? "Search modules and settings..." : search, 13, 10, search.isBlank() ? GuiTheme.MUTED : GuiTheme.TEXT);
    }

    private void drawPanel(DrawContext context, Category category, PanelState panel, int mouseX, int mouseY, double scale) {
        int x = scaled(panel.x, scale);
        int y = scaled(panel.y, scale);
        int w = scaled(PANEL_WIDTH, scale);
        int contentHeight = contentHeight(category);
        int h = scaled(Math.min(HEADER_HEIGHT + contentHeight, 270), scale);
        context.fill(x, y, x + w, y + h, GuiTheme.PANEL);
        context.fill(x, y, x + w, y + scaled(HEADER_HEIGHT, scale), GuiTheme.PANEL_HEADER);
        context.fill(x, y + scaled(HEADER_HEIGHT, scale) - 1, x + w, y + scaled(HEADER_HEIGHT, scale), GuiTheme.DEFAULT_ACCENT);
        drawText(context, category.name(), x + 6, y + 5, GuiTheme.TEXT);

        int rowY = panel.y + HEADER_HEIGHT + 3 - panel.scroll;
        for (Module module : UnityClient.INSTANCE.modules().byCategory(category)) {
            if (!matches(module)) {
                continue;
            }
            int logicalRowY = rowY;
            drawModule(context, module, panel.x + 4, logicalRowY, PANEL_WIDTH - 8, mouseX, mouseY, scale);
            rowY += ROW_HEIGHT;
            if (Boolean.TRUE.equals(expanded.get(module))) {
                for (Setting<?> setting : module.getSettings()) {
                    if (!setting.visible() || !matches(setting)) {
                        continue;
                    }
                    drawSetting(context, setting, panel.x + 12, rowY, PANEL_WIDTH - 20, scale);
                    rowY += ROW_HEIGHT;
                }
                context.fill(scaled(panel.x + 3, scale), scaled(logicalRowY - 1, scale), scaled(panel.x + PANEL_WIDTH - 3, scale), scaled(rowY, scale), 0x3355FF55);
            }
        }
    }

    private void drawModule(DrawContext context, Module module, int x, int y, int width, int mouseX, int mouseY, double scale) {
        boolean hover = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + ROW_HEIGHT;
        int color = module.isEnabled() ? 0x5533AA33 : hover ? 0x332E3430 : 0x221A1D22;
        context.fill(scaled(x, scale), scaled(y, scale), scaled(x + width, scale), scaled(y + ROW_HEIGHT - 1, scale), color);
        if (module.isEnabled()) {
            context.fill(scaled(x, scale), scaled(y, scale), scaled(x + 2, scale), scaled(y + ROW_HEIGHT - 1, scale), GuiTheme.DEFAULT_ACCENT);
        }
        drawText(context, module.name(), scaled(x + 5, scale), scaled(y + 4, scale), GuiTheme.TEXT);
        drawText(context, Boolean.TRUE.equals(expanded.get(module)) ? "-" : "+", scaled(x + width - 11, scale), scaled(y + 4, scale), GuiTheme.MUTED);
    }

    private void drawSetting(DrawContext context, Setting<?> setting, int x, int y, int width, double scale) {
        context.fill(scaled(x, scale), scaled(y, scale), scaled(x + width, scale), scaled(y + ROW_HEIGHT - 2, scale), 0x22111111);
        String value = valueText(setting);
        drawText(context, setting.name(), scaled(x + 4, scale), scaled(y + 4, scale), GuiTheme.MUTED);
        drawText(context, value, scaled(x + width - Math.min(width - 42, value.length() * 6), scale), scaled(y + 4, scale), GuiTheme.TEXT);
    }

    private String valueText(Setting<?> setting) {
        if (setting instanceof ColorSetting) {
            return "#" + Integer.toHexString((Integer) setting.get()).substring(2).toUpperCase(Locale.ROOT);
        }
        if (setting instanceof KeybindSetting keybind) {
            return keybind.get() < 0 ? "None" : GLFW.glfwGetKeyName(keybind.get(), 0) == null ? String.valueOf(keybind.get()) : GLFW.glfwGetKeyName(keybind.get(), 0);
        }
        return String.valueOf(setting.get());
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        double scale = scale();
        int sx = (int) (mouseX / scale);
        int sy = (int) (mouseY / scale);
        for (Category category : Category.values()) {
            PanelState panel = panels.get(category);
            if (inside(sx, sy, panel.x, panel.y, PANEL_WIDTH, HEADER_HEIGHT)) {
                dragging = panel;
                dragOffsetX = sx - panel.x;
                dragOffsetY = sy - panel.y;
                return true;
            }
            Module hit = hitModule(category, panel, sx, sy);
            if (hit != null) {
                if (button == 0) {
                    hit.toggle();
                } else if (button == 1) {
                    expanded.put(hit, !Boolean.TRUE.equals(expanded.get(hit)));
                } else if (button == 2) {
                    binding = hit;
                }
                return true;
            }
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseReleased(Click click) {
        dragging = null;
        return super.mouseReleased(click);
    }

    @Override
    public boolean mouseDragged(Click click, double deltaX, double deltaY) {
        if (dragging != null) {
            double scale = scale();
            dragging.x = (int) (click.x() / scale - dragOffsetX);
            dragging.y = (int) (click.y() / scale - dragOffsetY);
            dragging.x = Math.max(0, Math.min(dragging.x, (int) (width / scale) - PANEL_WIDTH));
            dragging.y = Math.max(24, Math.min(dragging.y, (int) (height / scale) - HEADER_HEIGHT));
            return true;
        }
        return super.mouseDragged(click, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        double scale = scale();
        int sx = (int) (mouseX / scale);
        int sy = (int) (mouseY / scale);
        for (Category category : Category.values()) {
            PanelState panel = panels.get(category);
            if (inside(sx, sy, panel.x, panel.y, PANEL_WIDTH, 270)) {
                panel.scroll = Math.max(0, panel.scroll - (int) (verticalAmount * 12));
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        int keyCode = input.key();
        if (binding != null) {
            binding.keybind().set(keyCode == GLFW.GLFW_KEY_ESCAPE ? -1 : keyCode);
            binding = null;
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_BACKSPACE && !search.isEmpty()) {
            search = search.substring(0, search.length() - 1);
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_ESCAPE && search.isEmpty()) {
            close();
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            search = "";
            return true;
        }
        UnityClient.INSTANCE.modules().handleKey(keyCode);
        return super.keyPressed(input);
    }

    @Override
    public boolean charTyped(CharInput input) {
        String typed = input.asString();
        if (typed != null && !typed.isBlank() && input.isValidChar()) {
            search += typed;
            return true;
        }
        return super.charTyped(input);
    }

    @Override
    public void close() {
        UnityClient.INSTANCE.modules().saveState();
        MinecraftClient.getInstance().setScreen(null);
    }

    private Module hitModule(Category category, PanelState panel, int mouseX, int mouseY) {
        int rowY = panel.y + HEADER_HEIGHT + 3 - panel.scroll;
        for (Module module : UnityClient.INSTANCE.modules().byCategory(category)) {
            if (!matches(module)) {
                continue;
            }
            if (inside(mouseX, mouseY, panel.x + 4, rowY, PANEL_WIDTH - 8, ROW_HEIGHT)) {
                return module;
            }
            rowY += ROW_HEIGHT;
            if (Boolean.TRUE.equals(expanded.get(module))) {
                for (Setting<?> setting : module.getSettings()) {
                    if (setting.visible() && matches(setting)) {
                        rowY += ROW_HEIGHT;
                    }
                }
            }
        }
        return null;
    }

    private boolean matches(Module module) {
        if (search.isBlank()) {
            return true;
        }
        String needle = search.toLowerCase(Locale.ROOT);
        if (module.name().toLowerCase(Locale.ROOT).contains(needle) || module.description().toLowerCase(Locale.ROOT).contains(needle)) {
            return true;
        }
        return module.getSettings().stream().anyMatch(setting -> matches(setting));
    }

    private boolean matches(Setting<?> setting) {
        String needle = search.toLowerCase(Locale.ROOT);
        return setting.name().toLowerCase(Locale.ROOT).contains(needle) || setting.description().toLowerCase(Locale.ROOT).contains(needle);
    }

    private int contentHeight(Category category) {
        int height = 8;
        for (Module module : UnityClient.INSTANCE.modules().byCategory(category)) {
            if (!matches(module)) {
                continue;
            }
            height += ROW_HEIGHT;
            if (Boolean.TRUE.equals(expanded.get(module))) {
                height += ROW_HEIGHT * module.getSettings().stream().filter(setting -> setting.visible() && matches(setting)).count();
            }
        }
        return height;
    }

    private double scale() {
        return UnityClient.INSTANCE.modules().getModule(ClickGuiModule.class).map(module -> module.scale.get()).orElse(1.0);
    }

    private int scaled(int value, double scale) {
        return (int) Math.round(value * scale);
    }

    private boolean inside(int mx, int my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }

    private void drawText(DrawContext context, String text, int x, int y, int color) {
        context.drawText(textRenderer, Text.literal(text), x, y, color, false);
    }

    private static final class PanelState {
        private int x;
        private int y;
        private int scroll;

        private PanelState(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
