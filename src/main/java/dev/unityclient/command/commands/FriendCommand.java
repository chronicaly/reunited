package dev.unityclient.command.commands;

import dev.unityclient.UnityClient;
import dev.unityclient.command.Command;
import dev.unityclient.util.ChatUtils;

public final class FriendCommand implements Command {
    @Override
    public String name() {
        return "friend";
    }

    @Override
    public String help() {
        return ".friend add|remove|list <name>";
    }

    @Override
    public void execute(String[] args) {
        if (args.length >= 2 && args[0].equalsIgnoreCase("add")) {
            UnityClient.INSTANCE.friends().add(args[1]);
            ChatUtils.info("Added friend " + args[1]);
        } else if (args.length >= 2 && args[0].equalsIgnoreCase("remove")) {
            UnityClient.INSTANCE.friends().remove(args[1]);
            ChatUtils.info("Removed friend " + args[1]);
        } else if (args.length >= 1 && args[0].equalsIgnoreCase("list")) {
            ChatUtils.info("Friends: " + String.join(", ", UnityClient.INSTANCE.friends().all()));
        } else {
            ChatUtils.info(help());
        }
    }
}
