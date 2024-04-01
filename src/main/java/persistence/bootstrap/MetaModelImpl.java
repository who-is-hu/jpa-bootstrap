package persistence.bootstrap;

import jdbc.JdbcTemplate;
import persistence.entity.EntityLoader;
import persistence.entity.EntityPersister;
import persistence.entity.EntityPersisterImpl;
import persistence.entity.GeneratedIdObtainStrategy;

import java.util.HashMap;
import java.util.Map;

public class MetaModelImpl implements MetaModel {
    private final Map<Class<?>, EntityPersister> persisterMap = new HashMap<>();
    private final Map<Class<?>, EntityLoader> loaderMap = new HashMap<>();

    public void init(
            JdbcTemplate jdbcTemplate,
            InFlightMetadataCollector inFlightMetadataCollector,
            GeneratedIdObtainStrategy generatedIdObtainStrategy
    ) {
        inFlightMetadataCollector.getPersistentClassMap().forEach((key, value) -> {
            persisterMap.put(key, new EntityPersisterImpl(generatedIdObtainStrategy, jdbcTemplate, value));
            loaderMap.put(key, new EntityLoader(jdbcTemplate, value));
        });
    }

    public EntityPersister getEntityPersister(Class<?> clazz) {
        return persisterMap.get(clazz);
    }

    public EntityLoader getEntityLoader(Class<?> clazz) {
        return loaderMap.get(clazz);
    }
}
