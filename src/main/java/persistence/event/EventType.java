package persistence.event;

import persistence.event.listener.*;

public class EventType<T extends EventListener> {
    public static final EventType<LoadEventListener> LOAD = create("load", LoadEventListener.class);
    public static final EventType<PersistEventListener> PERSIST = create("persist", PersistEventListener.class);
    public static final EventType<MergeEventListener> MERGE = create("merge", MergeEventListener.class);
    public static final EventType<DeleteEventListener> DELETE = create("delete", DeleteEventListener.class);

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

