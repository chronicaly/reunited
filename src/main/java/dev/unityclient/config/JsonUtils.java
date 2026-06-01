package dev.unityclient.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.unityclient.UnityClient;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

public final class JsonUtils {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private JsonUtils() {
    }

    public static JsonObject readObject(Path path) {
        if (!Files.exists(path)) {
            return new JsonObject();
        }
        try (Reader reader = Files.newBufferedReader(path)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (RuntimeException | IOException ex) {
            UnityClient.LOGGER.warn("Could not read JSON {}", path, ex);
            backup(path);
            return new JsonObject();
        }
    }

    public static void write(Path path, Object value) {
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(value, writer);
            }
        } catch (IOException ex) {
            UnityClient.LOGGER.error("Could not write JSON {}", path, ex);
        }
    }

    private static void backup(Path path) {
        try {
            if (Files.exists(path)) {
                Files.move(path, path.resolveSibling(path.getFileName() + "." + Instant.now().toEpochMilli() + ".bak"));
            }
        } catch (IOException ex) {
            UnityClient.LOGGER.warn("Could not backup corrupt JSON {}", path, ex);
        }
    }
}
