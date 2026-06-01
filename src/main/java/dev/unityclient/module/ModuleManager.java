package dev.unityclient.module;

import com.google.gson.JsonObject;
import dev.unityclient.UnityClient;
import dev.unityclient.config.JsonUtils;
import dev.unityclient.module.modules.client.ClickGuiModule;
import dev.unityclient.setting.Setting;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class ModuleManager {
    private final List<Module> modules = new ArrayList<>();
    private final Map<Category, List<Module>> byCategory = new EnumMap<>(Category.class);

    public void init() {
        if (!modules.isEmpty()) {
            return;
        }
        for (Category category : Category.values()) {
            byCategory.put(category, new ArrayList<>());
        }
        register(new ClickGuiModule());
        register(new SimpleModule("HUD", "Enables HUD rendering and the HUD editor.", Category.CLIENT, SimpleModule.SafetyMode.VISUAL_ONLY));
        register(new SimpleModule("Theme", "Central theme settings for Unity Client.", Category.CLIENT, SimpleModule.SafetyMode.VISUAL_ONLY));
        register(new SimpleModule("Notifications", "Controls notification display and limits.", Category.CLIENT, SimpleModule.SafetyMode.VISUAL_ONLY));
        register(new SimpleModule("Profiles", "Displays and switches current config profile.", Category.CLIENT, SimpleModule.SafetyMode.PASSIVE));

        register(new SimpleModule("FullBright", "Client-side brightness helper with safe restoration planned.", Category.RENDER, SimpleModule.SafetyMode.VISUAL_ONLY));
        register(new SimpleModule("Freecam", "Client-side detached camera framework placeholder.", Category.RENDER, SimpleModule.SafetyMode.VISUAL_ONLY));
        register(new SimpleModule("Nametags", "Enhanced player/entity nametag rendering framework.", Category.RENDER, SimpleModule.SafetyMode.VISUAL_ONLY));
        register(new SimpleModule("Tracers", "Visual line rendering framework for entities.", Category.RENDER, SimpleModule.SafetyMode.VISUAL_ONLY));
        register(new SimpleModule("StorageESP", "Cached storage highlighting framework.", Category.RENDER, SimpleModule.SafetyMode.VISUAL_ONLY));
        register(new SimpleModule("BlockHighlight", "Custom targeted block outline framework.", Category.RENDER, SimpleModule.SafetyMode.VISUAL_ONLY));
        register(new SimpleModule("NoRender", "Client-side visual effect hiding framework.", Category.RENDER, SimpleModule.SafetyMode.VISUAL_ONLY));
        register(new SimpleModule("Waypoints", "World and HUD waypoint display.", Category.RENDER, SimpleModule.SafetyMode.VISUAL_ONLY));
        register(new SimpleModule("DeathCoords", "Prints and stores death coordinates.", Category.RENDER, SimpleModule.SafetyMode.PASSIVE));

        register(new SimpleModule("AutoEat", "Safe eating assistant framework.", Category.PLAYER, SimpleModule.SafetyMode.ASSISTIVE));
        register(new SimpleModule("AutoTool", "Best-tool selection framework.", Category.PLAYER, SimpleModule.SafetyMode.ASSISTIVE));
        register(new SimpleModule("AutoReplenish", "Hotbar stack refill framework.", Category.PLAYER, SimpleModule.SafetyMode.ASSISTIVE));
        register(new SimpleModule("InventoryTweaks", "Inventory organization framework.", Category.PLAYER, SimpleModule.SafetyMode.ASSISTIVE));
        register(new SimpleModule("AutoRespawn", "Automatic respawn helper.", Category.PLAYER, SimpleModule.SafetyMode.ASSISTIVE));

        register(new SimpleModule("Sprint", "Keeps sprint enabled when movement conditions are met.", Category.MOVEMENT, SimpleModule.SafetyMode.ASSISTIVE));
        register(new SimpleModule("SafeWalk", "Prevents accidental edge walking framework.", Category.MOVEMENT, SimpleModule.SafetyMode.ASSISTIVE));
        register(new SimpleModule("Step", "Conservative step-height helper framework.", Category.MOVEMENT, SimpleModule.SafetyMode.ASSISTIVE));
        register(new SimpleModule("Parkour", "Auto-jump-at-edge helper framework.", Category.MOVEMENT, SimpleModule.SafetyMode.ASSISTIVE));
        register(new SimpleModule("ElytraHelper", "Displays elytra speed, durability, and firework information.", Category.MOVEMENT, SimpleModule.SafetyMode.VISUAL_ONLY));

        register(new SimpleModule("PortalLogger", "Logs portal coordinates locally.", Category.WORLD, SimpleModule.SafetyMode.PASSIVE));
        register(new SimpleModule("NewChunks", "Local chunk observation and display framework.", Category.WORLD, SimpleModule.SafetyMode.VISUAL_ONLY));
        register(new SimpleModule("BaritoneBridge", "Optional Baritone integration bridge.", Category.WORLD, SimpleModule.SafetyMode.PASSIVE));

        register(new SimpleModule("BetterChat", "Client-side chat formatting framework.", Category.MISC, SimpleModule.SafetyMode.PASSIVE));
        register(new SimpleModule("AutoReconnect", "Reconnect timer framework for disconnect screens.", Category.MISC, SimpleModule.SafetyMode.PASSIVE));
        register(new SimpleModule("ServerInfo", "Displays server information when available.", Category.MISC, SimpleModule.SafetyMode.VISUAL_ONLY));

        register(new SimpleModule("AutoTotem", "Conservative offhand totem helper framework.", Category.COMBAT, SimpleModule.SafetyMode.ASSISTIVE));
        register(new SimpleModule("Offhand", "Conservative offhand item selection framework.", Category.COMBAT, SimpleModule.SafetyMode.ASSISTIVE));
        register(new SimpleModule("TargetHUD", "Visual selected-target information HUD.", Category.COMBAT, SimpleModule.SafetyMode.VISUAL_ONLY));
        register(new SimpleModule("CrystalHelper", "Visual-only crystal placement helper framework.", Category.COMBAT, SimpleModule.SafetyMode.VISUAL_ONLY));
    }

    public void register(Module module) {
        modules.add(module);
        byCategory.computeIfAbsent(module.category(), ignored -> new ArrayList<>()).add(module);
        modules.sort(Comparator.comparing(Module::name));
        byCategory.get(module.category()).sort(Comparator.comparing(Module::name));
    }

    public List<Module> all() {
        return List.copyOf(modules);
    }

    public List<Module> byCategory(Category category) {
        return List.copyOf(byCategory.getOrDefault(category, List.of()));
    }

    public Optional<Module> getModule(String name) {
        String normalized = name.toLowerCase(Locale.ROOT).replace(" ", "");
        return modules.stream()
            .filter(module -> module.name().toLowerCase(Locale.ROOT).replace(" ", "").equals(normalized))
            .findFirst();
    }

    public <T extends Module> Optional<T> getModule(Class<T> type) {
        return modules.stream().filter(type::isInstance).map(type::cast).findFirst();
    }

    public void onTick() {
        for (Module module : modules) {
            if (!module.isEnabled()) {
                continue;
            }
            try {
                module.onTick();
            } catch (RuntimeException ex) {
                UnityClient.LOGGER.error("Module {} failed during tick", module.name(), ex);
                module.setEnabled(false);
            }
        }
    }

    public void handleKey(int key) {
        for (Module module : modules) {
            if (module.keybind().get() == key) {
                module.toggle();
            }
        }
    }

    public void saveState() {
        JsonObject root = new JsonObject();
        for (Module module : modules) {
            JsonObject data = new JsonObject();
            data.addProperty("enabled", module.isEnabled());
            for (Setting<?> setting : module.getSettings()) {
                data.add(setting.name(), setting.toJson());
            }
            root.add(module.name(), data);
        }
        JsonUtils.write(UnityClient.INSTANCE.config().path("modules.json"), root);
    }

    public void loadState() {
        JsonObject root = JsonUtils.readObject(UnityClient.INSTANCE.config().path("modules.json"));
        for (Module module : modules) {
            if (!root.has(module.name()) || !root.get(module.name()).isJsonObject()) {
                continue;
            }
            JsonObject data = root.getAsJsonObject(module.name());
            for (Setting<?> setting : module.getSettings()) {
                if (data.has(setting.name())) {
                    setting.fromJson(data.get(setting.name()));
                }
            }
            if (data.has("enabled") && data.get("enabled").getAsBoolean()) {
                module.setEnabled(true);
            }
        }
    }
}
