package persistence.event;

import persistence.event.listener.EventListener;

public interface EventListenerRegistry {
    <T extends EventListener> EventListener getEventListener(EventType<T> eventType);
    <T extends EventListener> void registerEventListener(EventType<T> eventType, T eventListener);
}
