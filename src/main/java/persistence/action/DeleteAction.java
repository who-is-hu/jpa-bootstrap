package persistence.action;

import persistence.entity.EntityPersister;

public class DeleteAction implements EntityAction {
    private final Object entity;
    private final EntityPersister entityPersister;

    public DeleteAction(Object entity, EntityPersister entityPersister) {
        this.entity = entity;
        this.entityPersister = entityPersister;
    }

    @Override
    public void execute() {
        entityPersister.delete(entity);
    }
}
