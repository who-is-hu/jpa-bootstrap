package persistence.bootstrap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.H2DBTestSupport;
import persistence.entity.H2GeneratedIdObtainStrategy;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.*;

class MetaModelImplTest extends H2DBTestSupport {
    @Test
    @DisplayName("metamodel 생성하면서 entity persister와 loader를 저장해둔다")
    void testCreate() throws Exception {
        InFlightMetadataCollector inFlightMetadataCollector = InFlightMetadataCollector.create(
                new ComponentScanner(),
                "persistence.bootstrap"
        );
        MetaModel metaModel = MetaModelImpl.create(
                jdbcTemplate,
                inFlightMetadataCollector,
                new H2GeneratedIdObtainStrategy()
        );

        assertSoftly(softly -> {
            softly.assertThat(metaModel.getEntityPersister(TestEntityClass.class)).isNotNull();
        });
    }

}
