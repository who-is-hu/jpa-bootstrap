package persistence.event.listener;

import persistence.bootstrap.MetaModel;
import persistence.entity.Status;
import persistence.entity.context.EntityEntry;
import persistence.entity.context.EntityKey;
import persistence.entity.context.PersistenceContext;
import persistence.event.LoadEvent;

public class DefaultLoadEventListener implements LoadEventListener {
    private final MetaModel metaModel;
    public DefaultLoadEventListener(MetaModel metaModel) {
        this.metaModel = metaModel;
    }

    @Override
    public <T> T onLoad(LoadEvent loadEvent) {
        Class<?> clazz = loadEvent.getEntityClass();
        Object id = loadEvent.getId();
        PersistenceContext persistenceContext = loadEvent.getPersistenceContext();

        EntityKey entityKey = new EntityKey(clazz, id);
        Object cachedEntity = persistenceContext.getEntity(entityKey);

        if(cachedEntity != null) {
            return (T) cachedEntity;
        }

        EntityEntry entityEntry = loadEvent.getEntityEntryFactory().createEntityEntry(Status.LOADING);
        loadEvent.getEntityEntryContext().addEntry(entityKey, entityEntry);

        Object foundEntity = metaModel.getEntityLoader(clazz).find(id);

        persistenceContext.addEntity(entityKey, foundEntity);
        entityEntry.setManaged();

        return (T) foundEntity;
    }
}
