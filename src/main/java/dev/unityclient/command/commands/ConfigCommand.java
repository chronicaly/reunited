package dev.unityclient.command.commands;

import dev.unityclient.UnityClient;
import dev.unityclient.command.Command;
import dev.unityclient.util.ChatUtils;

public final class ConfigCommand implements Command {
    @Override
    public String name() {
        return "config";
    }

    @Override
    public String help() {
        return ".config save|load|profile <name>";
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("save")) {
            UnityClient.INSTANCE.shutdown();
            ChatUtils.info("Config saved");
        } else if (args[0].equalsIgnoreCase("load")) {
            UnityClient.INSTANCE.modules().loadState();
            UnityClient.INSTANCE.hud().load();
            ChatUtils.info("Config loaded");
        } else if (args[0].equalsIgnoreCase("profile") && args.length > 1) {
            ChatUtils.info("Profile selected: " + args[1]);
        } else {
            ChatUtils.info(help());
        }
    }
}
