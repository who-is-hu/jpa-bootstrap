package persistence.bootstrap;

import persistence.entity.EntityManager;

public interface CurrentSessionContext {
    EntityManager currentSession();
    void putSession(EntityManager entityManager);
}

