package persistence.sql.mapping;

import jakarta.persistence.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import persistence.sql.dialect.Dialect;
import persistence.sql.dialect.H2Dialect;
import persistence.sql.mapping.exception.GenerationTypeMissingException;

import static jakarta.persistence.GenerationType.IDENTITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Entity
class TestClass {
    @Id
    private Integer id;

    @Transient
    private String ignore;

    @GeneratedValue(strategy = IDENTITY)
    private Long generated;

    private Long notGenerated;

    private Long nullable;

    @jakarta.persistence.Column(nullable = false)
    private Long notNullable;

    @jakarta.persistence.Column(name = "has_column")
    private Long hasColumn;

    private Long hasNotColumn;

    public void setId(int id) {
        this.id = id;
    }
}

class ColumnDataTest {
    Dialect dialect = new H2Dialect();

    private static final String GENERATED_VALUE_FIELD_NAME = "generated";
    private static final String NOT_GENERATED_VALUE_FIELD_NAME = "notGenerated";
    private final Table table = Table.from(TestClass.class);

    @ParameterizedTest()
    @CsvSource({
            GENERATED_VALUE_FIELD_NAME + ", true",
            NOT_GENERATED_VALUE_FIELD_NAME + ", false"
    })
    @DisplayName("hasGenerationType 테스트")
    void testHasGenerationType(String fieldName, boolean expected) throws Exception {
        Column column = Column.createColumn(table.getName(), TestClass.class.getDeclaredField(fieldName));

        assertThat(column.hasGenerationType()).isEqualTo(expected);
    }

    @Test
    @DisplayName("GeneratedValue 아닌데 getGenerationType 호출시 에러")
    void errorWhenGetGenerationTypeInvokedButIsNotGeneratedValue() throws Exception {
        Column column =
                Column.createColumn(table.getName(), TestClass.class.getDeclaredField(NOT_GENERATED_VALUE_FIELD_NAME));

        assertThrows(GenerationTypeMissingException.class, column::getGenerationType);
    }

    @Test
    @DisplayName("getGenerationType 테스트")
    void testGetGenerationType() throws Exception {
        Column column =
                Column.createColumn(table.getName(), TestClass.class.getDeclaredField(GENERATED_VALUE_FIELD_NAME));

        assertThat(column.getGenerationType()).isNotNull();
    }

    @ParameterizedTest
    @CsvSource({
            "nullable, false",
            "notNullable, true"
    })
    @DisplayName("isNotNullable 테스트")
    void testIsNullable(String fieldName, boolean expected) throws Exception {
        Column column = Column.createColumn(table.getName(), TestClass.class.getDeclaredField(fieldName));

        assertThat(column.isNotNullable()).isEqualTo(expected);
    }

    @Test
    @DisplayName("getName: 재정의 된 컬럼이름 있을시 필드명 대신 반환.")
    void testGetColumnNameWithAnnotation() throws Exception {
        Column column = Column.createColumn(table.getName(), TestClass.class.getDeclaredField("hasColumn"));

        assertThat(column.getName()).isEqualTo("has_column");
    }

    @Test
    @DisplayName("getName: 재정의 된 컬럼이름 없으면 필드명 반환.")
    void testGetColumnName() throws Exception {
        String fieldName = "hasNotColumn";
        Column column = Column.createColumn(table.getName(), TestClass.class.getDeclaredField(fieldName));

        assertThat(column.getName()).isEqualTo("has_not_column");
    }

    @Test
    @DisplayName("getValue 테스트")
    void testGetValue() throws Exception {
        int id = 1;
        TestClass testClass = new TestClass();
        testClass.setId(id);
        String fieldName = "id";

        Column column = Column.createColumn(
                table.getName(),
                TestClass.class.getDeclaredField(fieldName)
        );

        assertThat(column.getValue(testClass)).isEqualTo(id);
    }
}
