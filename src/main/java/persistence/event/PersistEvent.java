package persistence.event;

import persistence.entity.context.EntityEntryContext;
import persistence.entity.context.EntityEntryFactory;
import persistence.entity.context.PersistenceContext;

public class PersistEvent {
    private final Object entity;
    private final PersistenceContext persistenceContext;
    private final EntityEntryContext entityEntryContext;
    private final EntityEntryFactory entityEntryFactory;

    public PersistEvent(
            Object entity,
            PersistenceContext persistenceContext,
            EntityEntryContext entityEntryContext,
            EntityEntryFactory entityEntryFactory
    ) {
        this.entity = entity;
        this.persistenceContext = persistenceContext;
        this.entityEntryContext = entityEntryContext;
        this.entityEntryFactory = entityEntryFactory;
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
}
