package persistence.event.listener;

import persistence.event.MergeEvent;

public interface MergeEventListener extends EventListener {
    Object onMerge(MergeEvent event);
}
