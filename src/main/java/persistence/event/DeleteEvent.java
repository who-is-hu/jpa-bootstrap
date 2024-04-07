package persistence.event;

import persistence.entity.context.EntityEntryContext;
import persistence.entity.context.PersistenceContext;

public class DeleteEvent {
    private final Object entity;
    private final PersistenceContext persistenceContext;
    private final EntityEntryContext entityEntryContext;

    public DeleteEvent(Object entity, PersistenceContext persistenceContext, EntityEntryContext entityEntryContext) {
        this.entity = entity;
        this.persistenceContext = persistenceContext;
        this.entityEntryContext = entityEntryContext;
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
}
