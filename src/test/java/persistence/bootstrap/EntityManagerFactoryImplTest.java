package persistence.bootstrap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.H2DBTestSupport;
import persistence.entity.EntityManager;
import persistence.entity.H2GeneratedIdObtainStrategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EntityManagerFactoryImplTest extends H2DBTestSupport {
    @DisplayName("EntityManager 를 생성한다.")
    @Test
    void testCreateEntityManager() throws Exception {
        EntityManagerFactory entityManagerFactory = new EntityManagerFactoryImpl(
                new CurrentSessionContextImpl(),
                InFlightMetadataCollector.create(new ComponentScanner(), "persistence.model"),
                new H2GeneratedIdObtainStrategy()
        );

        EntityManager entityManager = entityManagerFactory.openSession(server.getConnection());

        assertThat(entityManager).isNotNull();
    }
}
