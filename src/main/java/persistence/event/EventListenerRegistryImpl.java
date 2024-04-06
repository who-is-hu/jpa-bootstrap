package persistence.event;

import persistence.bootstrap.MetaModel;
import persistence.event.listener.DefaultLoadEventListener;
import persistence.event.listener.EventListener;

import java.util.HashMap;
import java.util.Map;

public class EventListenerRegistryImpl implements EventListenerRegistry {
    public final MetaModel metaModel;
    private final Map<EventType<?>, EventListener> listenerMap = new HashMap<>();

    public EventListenerRegistryImpl(MetaModel metaModel) {
        this.metaModel = metaModel;
        registerEventListener(EventType.LOAD, new DefaultLoadEventListener(metaModel));
    }

    @Override
    public <T extends EventListener> EventListener getEventListener(EventType<T> eventType) {
        return listenerMap.get(eventType);
    }

    @Override
    public <T extends EventListener> void registerEventListener(EventType<T> eventType, T eventListener) {
        listenerMap.put(eventType, eventListener);
    }
}
