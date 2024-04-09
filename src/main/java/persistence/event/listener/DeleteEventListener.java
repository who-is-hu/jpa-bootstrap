package persistence.event.listener;

import persistence.event.DeleteEvent;

public interface DeleteEventListener extends EventListener {
    void onDelete(DeleteEvent event);
}
