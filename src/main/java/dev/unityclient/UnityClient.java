package dev.unityclient;

import dev.unityclient.command.CommandManager;
import dev.unityclient.config.ConfigManager;
import dev.unityclient.event.EventBus;
import dev.unityclient.friend.FriendManager;
import dev.unityclient.hud.HudManager;
import dev.unityclient.module.ModuleManager;
import dev.unityclient.notification.NotificationManager;
import dev.unityclient.waypoint.WaypointManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UnityClient {
    public static final String MOD_ID = "unity-client";
    public static final String NAME = "Unity Client";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    public static final UnityClient INSTANCE = new UnityClient();

    private final EventBus eventBus = new EventBus();
    private final ModuleManager moduleManager = new ModuleManager();
    private final HudManager hudManager = new HudManager();
    private final NotificationManager notificationManager = new NotificationManager();
    private final FriendManager friendManager = new FriendManager();
    private final WaypointManager waypointManager = new WaypointManager();
    private final ConfigManager configManager = new ConfigManager();
    private final CommandManager commandManager = new CommandManager();

    private UnityClient() {
    }

    public void init() {
        LOGGER.info("Starting {}", NAME);
        moduleManager.init();
        hudManager.init();
        commandManager.init();
        configManager.init();
        friendManager.load();
        waypointManager.load();
        moduleManager.loadState();
        hudManager.load();
    }

    public void shutdown() {
        moduleManager.saveState();
        hudManager.save();
        friendManager.save();
        waypointManager.save();
    }

    public EventBus events() {
        return eventBus;
    }

    public ModuleManager modules() {
        return moduleManager;
    }

    public HudManager hud() {
        return hudManager;
    }

    public NotificationManager notifications() {
        return notificationManager;
    }

    public FriendManager friends() {
        return friendManager;
    }

    public WaypointManager waypoints() {
        return waypointManager;
    }

    public ConfigManager config() {
        return configManager;
    }

    public CommandManager commands() {
        return commandManager;
    }
}
