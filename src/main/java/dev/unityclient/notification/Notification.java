package dev.unityclient.notification;

public record Notification(NotificationType type, String message, long created, long durationMillis) {
    public boolean expired(long now) {
        return now - created > durationMillis;
    }
}
