package persistence.bootstrap;

import persistence.entity.EntityManager;

public class CurrentSessionContextImpl implements CurrentSessionContext {
    EntityManager entityManager;
    @Override
    public EntityManager currentSession() {
        return entityManager;
    }

    @Override
    public void putSession(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
