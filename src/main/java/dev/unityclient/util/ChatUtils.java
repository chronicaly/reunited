package dev.unityclient.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public final class ChatUtils {
    private static final String PREFIX = "[Unity] ";

    private ChatUtils() {
    }

    public static void info(String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.inGameHud != null) {
            client.inGameHud.getChatHud().addMessage(Text.literal(PREFIX + message));
        }
    }
}
