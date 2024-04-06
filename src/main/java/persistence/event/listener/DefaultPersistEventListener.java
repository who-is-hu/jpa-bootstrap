package persistence.event.listener;

import persistence.bootstrap.MetaModel;
import persistence.entity.Status;
import persistence.entity.context.*;
import persistence.entity.exception.EntityAlreadyExistsException;
import persistence.event.PersistEvent;

public class DefaultPersistEventListener implements PersistEventListener {
    private final MetaModel metaModel;

    public DefaultPersistEventListener(MetaModel metaModel) {
        this.metaModel = metaModel;
    }

    @Override
    public Object onPersist(PersistEvent event) {
        Object entity = event.getEntity();
        PersistenceContext persistenceContext = event.getPersistenceContext();
        EntityEntryContext entityEntryContext = event.getEntityEntryContext();
        EntityEntryFactory entityEntryFactory = event.getEntityEntryFactory();

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
}
