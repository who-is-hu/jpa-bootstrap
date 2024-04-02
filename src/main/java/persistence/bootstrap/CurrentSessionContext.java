package persistence.bootstrap;

import persistence.entity.EntityManager;

public interface CurrentSessionContext {
    EntityManager currentSession() throws EntityManagerNotFoundException;
    void putSession(EntityManager entityManager);

    boolean hasBind();
}

