package persistence.event.listener;

import persistence.bootstrap.MetaModel;
import persistence.entity.context.EntityEntry;
import persistence.entity.context.EntityEntryContext;
import persistence.entity.context.EntityKey;
import persistence.entity.context.PersistenceContext;
import persistence.entity.exception.EntityNotExistsException;
import persistence.entity.exception.EntityReadOnlyException;
import persistence.event.MergeEvent;

public class DefaultMergeEventListener implements MergeEventListener {
    private final MetaModel metaModel;

    public DefaultMergeEventListener(MetaModel metaModel) {
        this.metaModel = metaModel;
    }

    @Override
    public Object onMerge(MergeEvent event) {
        Object entity = event.getEntity();
        PersistenceContext persistenceContext = event.getPersistenceContext();
        EntityEntryContext entityEntryContext = event.getEntityEntryContext();

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
}
