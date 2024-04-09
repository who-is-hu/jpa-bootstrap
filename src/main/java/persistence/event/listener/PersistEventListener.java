package persistence.event.listener;

import persistence.event.PersistEvent;

public interface PersistEventListener extends EventListener {
    Object onPersist(PersistEvent event);
}
