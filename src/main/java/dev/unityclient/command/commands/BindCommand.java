package dev.unityclient.command.commands;

import dev.unityclient.UnityClient;
import dev.unityclient.command.Command;
import dev.unityclient.util.ChatUtils;

public final class BindCommand implements Command {
    @Override
    public String name() {
        return "bind";
    }

    @Override
    public String help() {
        return ".bind <module> <keyCode>";
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            ChatUtils.info(help());
            return;
        }
        String key = args[args.length - 1];
        String moduleName = String.join(" ", java.util.Arrays.copyOf(args, args.length - 1));
        try {
            int keyCode = Integer.parseInt(key);
            UnityClient.INSTANCE.modules().getModule(moduleName).ifPresentOrElse(module -> {
                module.keybind().set(keyCode);
                ChatUtils.info("Bound " + module.name() + " to " + keyCode);
            }, () -> ChatUtils.info("Module not found"));
        } catch (NumberFormatException ex) {
            ChatUtils.info("Use a GLFW key code for now.");
        }
    }
}
