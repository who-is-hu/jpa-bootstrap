package persistence.event;

import persistence.action.ActionQueue;
import persistence.entity.context.EntityEntryContext;
import persistence.entity.context.PersistenceContext;

public class DeleteEvent {
    private final Object entity;
    private final PersistenceContext persistenceContext;
    private final EntityEntryContext entityEntryContext;
    private final ActionQueue actionQueue;

    public DeleteEvent(
            Object entity,
            PersistenceContext persistenceContext,
            EntityEntryContext entityEntryContext,
            ActionQueue actionQueue) {
        this.entity = entity;
        this.persistenceContext = persistenceContext;
        this.entityEntryContext = entityEntryContext;
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

    public ActionQueue getActionQueue() {
        return actionQueue;
    }
}
