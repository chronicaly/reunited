package dev.unityclient.friend;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.unityclient.UnityClient;
import dev.unityclient.config.JsonUtils;
import java.util.LinkedHashSet;
import java.util.Set;

public final class FriendManager {
    private final Set<String> friends = new LinkedHashSet<>();

    public void add(String name) {
        friends.add(name);
        save();
    }

    public void remove(String name) {
        friends.remove(name);
        save();
    }

    public Set<String> all() {
        return Set.copyOf(friends);
    }

    public void save() {
        JsonArray array = new JsonArray();
        friends.forEach(array::add);
        JsonUtils.write(UnityClient.INSTANCE.config().path("friends.json"), array);
    }

    public void load() {
        JsonElement root = JsonUtils.readObject(UnityClient.INSTANCE.config().path("friends.json")).get("friends");
        friends.clear();
        if (root != null && root.isJsonArray()) {
            root.getAsJsonArray().forEach(element -> friends.add(element.getAsString()));
        }
    }
}
