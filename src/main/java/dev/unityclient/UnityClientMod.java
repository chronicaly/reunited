package dev.unityclient;

import dev.unityclient.gui.ClickGuiScreen;
import dev.unityclient.gui.HudEditorScreen;
import dev.unityclient.util.ChatUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public final class UnityClientMod implements ClientModInitializer {
    private KeyBinding clickGuiKey;
    private KeyBinding hudEditorKey;
    private final KeyBinding.Category category = KeyBinding.Category.create(Identifier.of(UnityClient.MOD_ID, "keys"));

    @Override
    public void onInitializeClient() {
        UnityClient.INSTANCE.init();
        registerKeys();
        registerEvents();
        Runtime.getRuntime().addShutdownHook(new Thread(UnityClient.INSTANCE::shutdown, "Unity Client Config Save"));
    }

    private void registerKeys() {
        clickGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.unity-client.clickgui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            category
        ));
        hudEditorKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.unity-client.hudeditor",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            category
        ));
    }

    private void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (clickGuiKey.wasPressed()) {
                client.setScreen(new ClickGuiScreen());
            }
            while (hudEditorKey.wasPressed()) {
                client.setScreen(new HudEditorScreen());
            }
            UnityClient.INSTANCE.modules().onTick();
        });

        HudRenderCallback.EVENT.register((context, tickCounter) -> UnityClient.INSTANCE.hud().render(context));

        ClientSendMessageEvents.ALLOW_CHAT.register(message -> {
            if (message.startsWith(UnityClient.INSTANCE.commands().prefix())) {
                UnityClient.INSTANCE.commands().execute(message);
                return false;
            }
            return true;
        });

        ClientSendMessageEvents.ALLOW_COMMAND.register(command -> {
            String text = "/" + command;
            if (text.startsWith(UnityClient.INSTANCE.commands().prefix())) {
                UnityClient.INSTANCE.commands().execute(text);
                return false;
            }
            return true;
        });

        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            ChatUtils.info("Unity Client loaded");
        }
    }
}
