package persistence.bootstrap;

import jdbc.JdbcTemplate;
import persistence.entity.EntityLoader;
import persistence.entity.EntityPersister;
import persistence.entity.GeneratedIdObtainStrategy;

public interface MetaModel {
    EntityPersister getEntityPersister(Class<?> clazz);

    EntityLoader getEntityLoader(Class<?> clazz);
}
