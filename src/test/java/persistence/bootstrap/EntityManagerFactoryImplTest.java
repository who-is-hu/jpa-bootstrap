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

    @DisplayName("현재 스레드에 이미 EntityManager 가 있으면 생성하지 않고 가져온다")
    @Test
    void doNotCreateWhenEmAlreadyExists() throws Exception {
        EntityManagerFactory entityManagerFactory = new EntityManagerFactoryImpl(
                new CurrentSessionContextImpl(),
                InFlightMetadataCollector.create(new ComponentScanner(), "persistence.model"),
                new H2GeneratedIdObtainStrategy()
        );

        EntityManager entityManager1 = entityManagerFactory.openSession(server.getConnection());
        EntityManager entityManager2 = entityManagerFactory.openSession(server.getConnection());

        assertThat(entityManager1).isEqualTo(entityManager2);
    }
}
