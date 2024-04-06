package persistence.event;

import persistence.event.listener.EventListener;
import persistence.event.listener.LoadEventListener;

public class EventType<T extends EventListener> {
    public static final EventType<LoadEventListener> LOAD = create("load", LoadEventListener.class);

    private final String eventName;
    private final Class<T> baseListenerInterface;

    private EventType(String eventName, Class<T> baseListenerInterface) {
        this.eventName = eventName;
        this.baseListenerInterface = baseListenerInterface;
    }

    private static <T extends EventListener> EventType<T> create(String name, Class<T> listenerRole) {
        return new EventType<>(name, listenerRole);
    }
}

