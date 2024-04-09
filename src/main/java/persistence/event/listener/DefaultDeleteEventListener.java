package persistence.event.listener;

import persistence.action.ActionQueue;
import persistence.action.DeleteAction;
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
        ActionQueue actionQueue = event.getActionQueue();

        EntityKey entityKey = EntityKey.fromEntity(entity);
        EntityEntry entityEntry = entityEntryContext.getEntry(entityKey);

        entityEntry.setDeleted();
        DeleteAction deleteAction = new DeleteAction(entity, metaModel.getEntityPersister(entity.getClass()));
        actionQueue.addDeleteAction(deleteAction);
        persistenceContext.removeEntity(entity);
        entityEntry.setGone();
    }
}
