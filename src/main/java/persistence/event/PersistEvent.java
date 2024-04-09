package persistence.event;

import persistence.action.ActionQueue;
import persistence.entity.context.EntityEntryContext;
import persistence.entity.context.EntityEntryFactory;
import persistence.entity.context.PersistenceContext;

public class PersistEvent {
    private final Object entity;
    private final PersistenceContext persistenceContext;
    private final EntityEntryContext entityEntryContext;
    private final EntityEntryFactory entityEntryFactory;
    private final ActionQueue actionQueue;

    public PersistEvent(
            Object entity,
            PersistenceContext persistenceContext,
            EntityEntryContext entityEntryContext,
            EntityEntryFactory entityEntryFactory,
            ActionQueue actionQueue
    ) {
        this.entity = entity;
        this.persistenceContext = persistenceContext;
        this.entityEntryContext = entityEntryContext;
        this.entityEntryFactory = entityEntryFactory;
        this.actionQueue = actionQueue;
    }

    public Object getEntity() {
        return entity;
    }

    public PersistenceContext getPersistenceContext() {
        return persistenceContext;
    }

    public EntityEntryContext getEntityEntryContext() {
        return entityEntryContext;
    }

    public EntityEntryFactory getEntityEntryFactory() {
        return entityEntryFactory;
    }

    public ActionQueue getActionQueue() {
        return actionQueue;
    }
}
