package persistence.bootstrap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class InFlightMetadataCollectorTest {
    private final ComponentScanner componentScanner = new ComponentScanner();
    @DisplayName("@Entity 클래스만 가져와서 메타데이터를 생성한다.")
    @Test
    void createMetadataFromEntityClass() throws Exception {
        InFlightMetadataCollector inFlightMetadataCollector = InFlightMetadataCollector.create(
                componentScanner, "persistence.bootstrap"
        );

        assertSoftly(softly -> {
            softly.assertThat(inFlightMetadataCollector.getPersistentClass(TestNotEntityClass.class)).isNull();
            softly.assertThat(inFlightMetadataCollector.getPersistentClass(TestEntityClass.class)).isNotNull();
        });
    }

}
