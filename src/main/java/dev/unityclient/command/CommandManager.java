package dev.unityclient.command;

import dev.unityclient.command.commands.BindCommand;
import dev.unityclient.command.commands.ConfigCommand;
import dev.unityclient.command.commands.FriendCommand;
import dev.unityclient.command.commands.ToggleCommand;
import dev.unityclient.command.commands.WaypointCommand;
import dev.unityclient.util.ChatUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class CommandManager {
    private final List<Command> commands = new ArrayList<>();
    private String prefix = ".";

    public void init() {
        if (!commands.isEmpty()) {
            return;
        }
        commands.add(new ToggleCommand());
        commands.add(new BindCommand());
        commands.add(new ConfigCommand());
        commands.add(new FriendCommand());
        commands.add(new WaypointCommand());
        commands.add(new Command() {
            @Override
            public String name() {
                return "help";
            }

            @Override
            public String help() {
                return ".help";
            }

            @Override
            public void execute(String[] args) {
                commands.forEach(command -> ChatUtils.info(command.help()));
            }
        });
    }

    public String prefix() {
        return prefix;
    }

    public void execute(String message) {
        String body = message.startsWith(prefix) ? message.substring(prefix.length()) : message;
        String[] split = body.trim().split("\\s+");
        if (split.length == 0 || split[0].isBlank()) {
            return;
        }
        String name = split[0].toLowerCase(Locale.ROOT);
        String[] args = Arrays.copyOfRange(split, 1, split.length);
        for (Command command : commands) {
            if (command.name().equalsIgnoreCase(name)) {
                command.execute(args);
                return;
            }
        }
        ChatUtils.info("Unknown command. Try .help");
    }
}
