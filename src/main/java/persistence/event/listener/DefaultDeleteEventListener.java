package persistence.event.listener;

import persistence.bootstrap.MetaModel;
import persistence.entity.context.EntityEntry;
import persistence.entity.context.EntityEntryContext;
import persistence.entity.context.EntityKey;
import persistence.entity.context.PersistenceContext;
import persistence.event.DeleteEvent;

public class DefaultDeleteEventListener implements DeleteEventListener {
    private final MetaModel metaModel;

    public DefaultDeleteEventListener(MetaModel metaModel) {
        this.metaModel = metaModel;
    }

    @Override
    public void onDelete(DeleteEvent event) {
        Object entity = event.getEntity();
        PersistenceContext persistenceContext = event.getPersistenceContext();
        EntityEntryContext entityEntryContext = event.getEntityEntryContext();

        EntityKey entityKey = EntityKey.fromEntity(entity);
        EntityEntry entityEntry = entityEntryContext.getEntry(entityKey);

        entityEntry.setDeleted();
        metaModel.getEntityPersister(entity.getClass()).delete(entity);
        persistenceContext.removeEntity(entity);
        entityEntry.setGone();
    }
}
