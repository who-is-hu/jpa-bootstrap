package persistence.event;

import persistence.entity.context.*;

public class LoadEvent {
    private final Class<?> entityClass;
    private final Object id;
    private final PersistenceContext persistenceContext;
    private final EntityEntryContext entityEntryContext;
    private final EntityEntryFactory entityEntryFactory;

    public LoadEvent(
            Class<?> entityClass,
            Object id,
            PersistenceContext persistenceContext,
            EntityEntryContext entityEntryContext,
            EntityEntryFactory entityEntryFactory
    ) {
        this.id = id;
        this.entityClass = entityClass;
        this.persistenceContext = persistenceContext;
        this.entityEntryContext = entityEntryContext;
        this.entityEntryFactory = entityEntryFactory;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public Object getId() {
        return id;
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
