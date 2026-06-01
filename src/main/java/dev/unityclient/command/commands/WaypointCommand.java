package dev.unityclient.command.commands;

import dev.unityclient.UnityClient;
import dev.unityclient.command.Command;
import dev.unityclient.gui.GuiTheme;
import dev.unityclient.util.ChatUtils;
import dev.unityclient.waypoint.Waypoint;

public final class WaypointCommand implements Command {
    @Override
    public String name() {
        return "waypoint";
    }

    @Override
    public String help() {
        return ".waypoint add|remove|list <name>";
    }

    @Override
    public void execute(String[] args) {
        if (args.length >= 2 && args[0].equalsIgnoreCase("add")) {
            UnityClient.INSTANCE.waypoints().add(new Waypoint(args[1], 0, 64, 0, "overworld", GuiTheme.DEFAULT_ACCENT, "unknown", System.currentTimeMillis()));
            ChatUtils.info("Added waypoint " + args[1]);
        } else if (args.length >= 2 && args[0].equalsIgnoreCase("remove")) {
            UnityClient.INSTANCE.waypoints().remove(args[1]);
            ChatUtils.info("Removed waypoint " + args[1]);
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("list")) {
            ChatUtils.info("Waypoints: " + UnityClient.INSTANCE.waypoints().all().size());
        } else {
            ChatUtils.info(help());
        }
    }
}
