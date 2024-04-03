package persistence.entity;

import persistence.bootstrap.MetaModel;
import persistence.entity.context.*;
import persistence.entity.exception.EntityAlreadyExistsException;
import persistence.entity.exception.EntityNotExistsException;
import persistence.entity.exception.EntityReadOnlyException;

public class EntityManagerImpl implements EntityManager {
    private final MetaModel metaModel;
    private final PersistenceContext persistenceContext;
    private final EntityEntryContext entityEntryContext;
    private final EntityEntryFactory entityEntryFactory;


    public EntityManagerImpl(
            PersistenceContext persistenceContext,
            EntityEntryContext entityEntryContext,
            EntityEntryFactory entityEntryFactory,
            MetaModel metaModel
    ) {
        this.persistenceContext = persistenceContext;
        this.entityEntryContext = entityEntryContext;
        this.entityEntryFactory = entityEntryFactory;
        this.metaModel = metaModel;
    }

    @Override
    public <T> T find(Class<T> clazz, Object id) {
        EntityKey entityKey = new EntityKey(clazz, id);
        Object cachedEntity = persistenceContext.getEntity(entityKey);

        if(cachedEntity != null) {
            return (T) cachedEntity;
        }

        EntityEntry entityEntry = entityEntryFactory.createEntityEntry(Status.LOADING);
        entityEntryContext.addEntry(entityKey, entityEntry);

        T foundEntity = metaModel.getEntityLoader(clazz).find(id);
        persistenceContext.addEntity(entityKey, foundEntity);

        entityEntry.setManaged();

        return foundEntity;
    }

    @Override
    public Object persist(Object entity) {
        EntityKey entityKey = EntityKey.fromEntity(entity);
        if(entityKey.hasId()) {
            throw new EntityAlreadyExistsException(entityKey);
        }

        // TODO: entityEntry 에 SAVING 상태로 등록. id 가 없는데 어떻게?

        metaModel.getEntityPersister(entity.getClass()).insert(entity);
        entityKey = EntityKey.fromEntity(entity);
        persistenceContext.addEntity(entityKey, entity);
        EntityEntry entityEntry = entityEntryFactory.createEntityEntry(Status.MANAGED);
        entityEntryContext.addEntry(entityKey, entityEntry);

        return entity;
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
