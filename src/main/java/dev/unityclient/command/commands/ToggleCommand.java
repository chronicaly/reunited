package dev.unityclient.command.commands;

import dev.unityclient.UnityClient;
import dev.unityclient.command.Command;
import dev.unityclient.util.ChatUtils;

public final class ToggleCommand implements Command {
    @Override
    public String name() {
        return "toggle";
    }

    @Override
    public String help() {
        return ".toggle <module>";
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            ChatUtils.info(help());
            return;
        }
        UnityClient.INSTANCE.modules().getModule(String.join(" ", args)).ifPresentOrElse(module -> {
            module.toggle();
            ChatUtils.info(module.name() + " " + (module.isEnabled() ? "enabled" : "disabled"));
        }, () -> ChatUtils.info("Module not found"));
    }
}
