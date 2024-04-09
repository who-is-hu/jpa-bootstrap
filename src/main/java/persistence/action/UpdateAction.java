package persistence.action;

import persistence.entity.EntityPersister;

public class UpdateAction implements EntityAction {
    private final Object entity;
    private final EntityPersister entityPersister;

    public UpdateAction(Object entity, EntityPersister entityPersister) {
        this.entity = entity;
        this.entityPersister = entityPersister;
    }

    @Override
    public void execute() {
        entityPersister.update(entity);
    }
}
