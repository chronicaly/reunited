package dev.unityclient.config;

import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager {
    private Path root;

    public void init() {
        root = FabricLoader.getInstance().getConfigDir().resolve("unity-client");
        try {
            Files.createDirectories(root.resolve("profiles"));
            createDefaults();
        } catch (Exception ignored) {
        }
    }

    public Path path(String file) {
        return root.resolve(file);
    }

    private void createDefaults() {
        for (String file : new String[]{"modules.json", "hud.json", "theme.json", "friends.json", "waypoints.json", "server-profiles.json"}) {
            Path path = path(file);
            if (!Files.exists(path)) {
                JsonUtils.write(path, new JsonObject());
            }
        }
        for (String profile : new String[]{"default.json", "anarchy.json", "singleplayer.json"}) {
            Path path = root.resolve("profiles").resolve(profile);
            if (!Files.exists(path)) {
                JsonUtils.write(path, new JsonObject());
            }
        }
    }
}
