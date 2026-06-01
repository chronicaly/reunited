package dev.unityclient.waypoint;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.unityclient.UnityClient;
import dev.unityclient.config.JsonUtils;
import java.util.ArrayList;
import java.util.List;

public final class WaypointManager {
    private final List<Waypoint> waypoints = new ArrayList<>();

    public void add(Waypoint waypoint) {
        waypoints.add(waypoint);
        save();
    }

    public void remove(String name) {
        waypoints.removeIf(waypoint -> waypoint.name().equalsIgnoreCase(name));
        save();
    }

    public List<Waypoint> all() {
        return List.copyOf(waypoints);
    }

    public void save() {
        JsonArray array = new JsonArray();
        for (Waypoint waypoint : waypoints) {
            JsonObject data = new JsonObject();
            data.addProperty("name", waypoint.name());
            data.addProperty("x", waypoint.x());
            data.addProperty("y", waypoint.y());
            data.addProperty("z", waypoint.z());
            data.addProperty("dimension", waypoint.dimension());
            data.addProperty("color", waypoint.color());
            data.addProperty("server", waypoint.server());
            data.addProperty("created", waypoint.created());
            array.add(data);
        }
        JsonObject root = new JsonObject();
        root.add("waypoints", array);
        JsonUtils.write(UnityClient.INSTANCE.config().path("waypoints.json"), root);
    }

    public void load() {
        waypoints.clear();
        JsonObject root = JsonUtils.readObject(UnityClient.INSTANCE.config().path("waypoints.json"));
        if (!root.has("waypoints") || !root.get("waypoints").isJsonArray()) {
            return;
        }
        root.getAsJsonArray("waypoints").forEach(element -> {
            JsonObject data = element.getAsJsonObject();
            waypoints.add(new Waypoint(
                data.get("name").getAsString(),
                data.get("x").getAsDouble(),
                data.get("y").getAsDouble(),
                data.get("z").getAsDouble(),
                data.get("dimension").getAsString(),
                data.get("color").getAsInt(),
                data.get("server").getAsString(),
                data.get("created").getAsLong()
            ));
        });
    }
}
