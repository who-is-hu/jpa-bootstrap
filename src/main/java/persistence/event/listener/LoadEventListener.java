package persistence.event.listener;

import persistence.event.LoadEvent;

public interface LoadEventListener extends EventListener {
    <T> T onLoad(LoadEvent event);
}

