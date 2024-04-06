package persistence.entity;

import persistence.bootstrap.MetaModel;
import persistence.entity.context.*;
import persistence.entity.exception.EntityAlreadyExistsException;
import persistence.entity.exception.EntityNotExistsException;
import persistence.entity.exception.EntityReadOnlyException;
import persistence.event.EventListenerRegistry;
import persistence.event.EventType;
import persistence.event.LoadEvent;
import persistence.event.PersistEvent;
import persistence.event.listener.EventListener;
import persistence.event.listener.LoadEventListener;
import persistence.event.listener.PersistEventListener;

public class EntityManagerImpl implements EntityManager {
    private final MetaModel metaModel;
    private final PersistenceContext persistenceContext;
    private final EntityEntryContext entityEntryContext;
    private final EntityEntryFactory entityEntryFactory;
    private final EventListenerRegistry eventListenerRegistry;


    public EntityManagerImpl(
            PersistenceContext persistenceContext,
            EntityEntryContext entityEntryContext,
            EntityEntryFactory entityEntryFactory,
            MetaModel metaModel,
            EventListenerRegistry eventListenerRegistry
    ) {
        this.persistenceContext = persistenceContext;
        this.entityEntryContext = entityEntryContext;
        this.entityEntryFactory = entityEntryFactory;
        this.metaModel = metaModel;
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
                entityEntryFactory
        );
        PersistEventListener eventListener =
                (PersistEventListener) eventListenerRegistry.getEventListener(EventType.PERSIST);

        return eventListener.onPersist(event);
    }

    @Override
    public Object merge(Object entity) {
        EntityKey entityKey = EntityKey.fromEntity(entity);
        EntityEntry entityEntry = entityEntryContext.getEntry(entityKey);

        if(entityEntry == null || persistenceContext.getEntity(entityKey) == null) {
            throw new EntityNotExistsException(entityKey);
        }

        if(entityEntry.isReadOnly()){
            throw new EntityReadOnlyException(entityKey);
        }

        if(!persistenceContext.isDirty(entityKey, entity)) {
            return entity;
        }

        entityEntry.setSaving();
        metaModel.getEntityPersister(entity.getClass()).update(entity);
        persistenceContext.addEntity(entityKey, entity);
        entityEntry.setManaged();

        return entity;
    }

    @Override
    public void remove(Object entity) {
        EntityKey entityKey = EntityKey.fromEntity(entity);
        EntityEntry entityEntry = entityEntryContext.getEntry(entityKey);

        entityEntry.setDeleted();
        metaModel.getEntityPersister(entity.getClass()).delete(entity);
        persistenceContext.removeEntity(entity);
        entityEntry.setGone();
    }
}
