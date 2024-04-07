package persistence.action;

import persistence.entity.EntityPersister;

public class InsertAction implements EntityAction {
    private final Object entity;
    private final EntityPersister entityPersister;

    public InsertAction(Object entity, EntityPersister entityPersister) {
        this.entity = entity;
        this.entityPersister = entityPersister;
    }

    @Override
    public void execute() {
        entityPersister.insert(entity);
    }
}
