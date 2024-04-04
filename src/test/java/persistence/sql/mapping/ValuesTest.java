package persistence.sql.mapping;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.Person;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class ValuesTest {
    @Test
    @DisplayName("값 넣으면서 생성 테스트")
    void testCreateWithValueColumns() {
        Person person = new Person(1L, "test", 10, "test", null);
        Columns columns = Columns.createColumns(Person.class);

        Values values = Values.fromEntity(person, columns);

        assertSoftly(softly -> {
            softly.assertThat(values.getEntries()).isNotEmpty();
            softly.assertThat(values.getEntries().stream().noneMatch(Objects::isNull)).isTrue();
        });
    }

    @Test
    public void testGetValuesMap() {
        Person person = new Person(1L, "test", 10, "test", null);
        Columns columns = Columns.createColumns(Person.class);
        Values values = Values.fromEntity(person, columns);

        List<Map.Entry<String, Object>> entries = values.getEntries();

        assertSoftly(softly -> {
            softly.assertThat(entries).isNotEmpty();
            softly.assertThat(entries.stream().noneMatch(entry -> entry.getValue() == null)).isTrue();
        });
    }
}
