package persistence.bootstrap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.H2DBTestSupport;
import persistence.entity.H2GeneratedIdObtainStrategy;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.*;

class MetaModelImplTest extends H2DBTestSupport {
    @Test
    @DisplayName("metamodel 클래스에 entity persister와 loader를 저장해둔다")
    void testInit() throws Exception {
        InFlightMetadataCollector inFlightMetadataCollector = new InFlightMetadataCollector(new ComponentScanner());
        inFlightMetadataCollector.collectMetadata("persistence.bootstrap");
        MetaModelImpl metaModel = new MetaModelImpl();

        metaModel.init(jdbcTemplate, inFlightMetadataCollector, new H2GeneratedIdObtainStrategy());

        assertSoftly(softly -> {
            softly.assertThat(metaModel.getEntityPersister(TestEntityClass.class)).isNotNull();
        });
    }

}
