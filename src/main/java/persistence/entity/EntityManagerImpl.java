package persistence.entity;

import persistence.action.ActionQueue;
import persistence.entity.context.EntityEntryContext;
import persistence.entity.context.EntityEntryFactory;
import persistence.entity.context.PersistenceContext;
import persistence.event.*;
import persistence.event.listener.DeleteEventListener;
import persistence.event.listener.LoadEventListener;
import persistence.event.listener.MergeEventListener;
import persistence.event.listener.PersistEventListener;

public class EntityManagerImpl implements EntityManager {
    private final PersistenceContext persistenceContext;
    private final EntityEntryContext entityEntryContext;
    private final EntityEntryFactory entityEntryFactory;
    private final EventListenerRegistry eventListenerRegistry;
    private final ActionQueue actionQueue = new ActionQueue();

    public EntityManagerImpl(
            PersistenceContext persistenceContext,
            EntityEntryContext entityEntryContext,
            EntityEntryFactory entityEntryFactory,
            EventListenerRegistry eventListenerRegistry
    ) {
        this.persistenceContext = persistenceContext;
        this.entityEntryContext = entityEntryContext;
        this.entityEntryFactory = entityEntryFactory;
        this.eventListenerRegistry = eventListenerRegistry;
    }

    @Override
    public <T> T find(Class<T> clazz, Object id) {
        LoadEvent loadEvent = new LoadEvent(clazz, id, persistenceContext, entityEntryContext, entityEntryFactory);
        LoadEventListener eventListener = (LoadEventListener) eventListenerRegistry.getEventListener(EventType.LOAD);
        return eventListener.onLoad(loadEvent);
    }

    @Override
    public Object persist(Object entity) {
        PersistEvent event = new PersistEvent(
                entity,
                persistenceContext,
                entityEntryContext,
                entityEntryFactory,
                actionQueue
        );
        PersistEventListener eventListener =
                (PersistEventListener) eventListenerRegistry.getEventListener(EventType.PERSIST);

        return eventListener.onPersist(event);
    }

    @Override
    public Object merge(Object entity) {
        MergeEvent mergeEvent = new MergeEvent(
                entity,
                persistenceContext,
                entityEntryContext,
                actionQueue
        );
        MergeEventListener eventListener =
                (MergeEventListener) eventListenerRegistry.getEventListener(EventType.MERGE);

        return eventListener.onMerge(mergeEvent);
    }

    @Override
    public void remove(Object entity) {
        DeleteEvent deleteEvent = new DeleteEvent(
                entity,
                persistenceContext,
                entityEntryContext,
                actionQueue
        );
        DeleteEventListener eventListener =
                (DeleteEventListener) eventListenerRegistry.getEventListener(EventType.DELETE);

        eventListener.onDelete(deleteEvent);
    }

    @Override
    public void flush() {
        actionQueue.executeActions();
    }
}
