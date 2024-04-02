package persistence.bootstrap;


import database.DatabaseServer;
import jdbc.JdbcTemplate;
import persistence.entity.EntityManager;
import persistence.entity.EntityManagerImpl;
import persistence.entity.GeneratedIdObtainStrategy;
import persistence.entity.context.DefaultEntityEntryFactory;
import persistence.entity.context.EntityEntryContext;
import persistence.entity.context.PersistenceContextImpl;

import java.sql.Connection;

public class EntityManagerFactoryImpl implements EntityManagerFactory {
    private final CurrentSessionContext currentSessionContext;
    private final InFlightMetadataCollector metaData;
    private final GeneratedIdObtainStrategy generatedIdObtainStrategy;

    public EntityManagerFactoryImpl(
            CurrentSessionContext currentSessionContext,
            InFlightMetadataCollector metaData,
            GeneratedIdObtainStrategy generatedIdObtainStrategy
    ) {
        this.currentSessionContext = currentSessionContext;
        this.metaData = metaData;
        this.generatedIdObtainStrategy = generatedIdObtainStrategy;
    }

    @Override
    public EntityManager openSession(Connection connection) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(connection);

        return new EntityManagerImpl(
                new PersistenceContextImpl(),
                new EntityEntryContext(),
                new DefaultEntityEntryFactory(),
                MetaModelImpl.create(jdbcTemplate, metaData, generatedIdObtainStrategy)
        );
    }
}
