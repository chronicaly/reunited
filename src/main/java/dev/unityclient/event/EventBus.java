package dev.unityclient.event;

import dev.unityclient.UnityClient;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class EventBus {
    private final List<Entry<?>> listeners = new CopyOnWriteArrayList<>();

    public <T> void subscribe(Class<T> type, EventPriority priority, Consumer<T> consumer) {
        listeners.add(new Entry<>(type, priority, consumer));
        listeners.sort(Comparator.comparing((Entry<?> e) -> e.priority()).reversed());
    }

    public <T> void unsubscribe(Consumer<T> consumer) {
        listeners.removeIf(entry -> entry.consumer() == consumer);
    }

    public void post(Object event) {
        List<Entry<?>> snapshot = new ArrayList<>(listeners);
        for (Entry<?> entry : snapshot) {
            if (entry.type().isInstance(event)) {
                dispatch(entry, event);
            }
        }
    }

    private <T> void dispatch(Entry<T> entry, Object event) {
        try {
            entry.consumer().accept(entry.type().cast(event));
        } catch (RuntimeException ex) {
            UnityClient.LOGGER.error("Event listener failed for {}", event.getClass().getSimpleName(), ex);
        }
    }

    private record Entry<T>(Class<T> type, EventPriority priority, Consumer<T> consumer) {
    }
}
