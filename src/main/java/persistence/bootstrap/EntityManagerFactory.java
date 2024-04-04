package persistence.bootstrap;

import persistence.entity.EntityManager;

import java.sql.Connection;

public interface EntityManagerFactory {
    EntityManager openSession(Connection connection);
}

