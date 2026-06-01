package dev.unityclient.notification;

import dev.unityclient.gui.GuiTheme;
import java.util.ArrayDeque;
import java.util.Deque;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public final class NotificationManager {
    private final Deque<Notification> notifications = new ArrayDeque<>();
    private int maxNotifications = 5;

    public void info(String message) {
        add(NotificationType.INFO, message);
    }

    public void success(String message) {
        add(NotificationType.SUCCESS, message);
    }

    public void warning(String message) {
        add(NotificationType.WARNING, message);
    }

    public void error(String message) {
        add(NotificationType.ERROR, message);
    }

    public void add(NotificationType type, String message) {
        notifications.addFirst(new Notification(type, message, System.currentTimeMillis(), 3000));
        while (notifications.size() > maxNotifications) {
            notifications.removeLast();
        }
    }

    public void render(DrawContext context) {
        long now = System.currentTimeMillis();
        notifications.removeIf(notification -> notification.expired(now));
        int y = 24;
        for (Notification notification : notifications) {
            int color = switch (notification.type()) {
                case ERROR -> GuiTheme.ERROR;
                case WARNING -> GuiTheme.WARNING;
                case SUCCESS -> GuiTheme.DEFAULT_ACCENT;
                case INFO -> GuiTheme.TEXT;
            };
            int x = Math.max(4, context.getScaledWindowWidth() - 180);
            context.fill(x - 4, y - 3, x + 172, y + 12, 0xAA111111);
            context.drawText(MinecraftClient.getInstance().textRenderer, Text.literal(notification.message()), x, y, color, false);
            y += 18;
        }
    }
}
