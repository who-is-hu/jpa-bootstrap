package persistence.sql.mapping;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;



@Entity
@jakarta.persistence.Table(name = "with_table_name")
class WithTableName {
    @Id
    private int id;
}

@Entity
class WithoutTableName {
    @Id
    private int id;
}

class TableTest {

    @Test
    @DisplayName("getName: @Table(name)이 있다면 name을 반환한다")
    void testGetNameWithAnnotation() {
        Table table = Table.from(WithTableName.class);
        assertThat(table.getName()).isEqualTo("with_table_name");
    }

    @Test
    @DisplayName("getName: @Table(name)이 없다면 소문자 클래스 이름 반환한다")
    void testGetName() {
        Table table = Table.from(WithoutTableName.class);
        assertThat(table.getName()).isEqualTo("withouttablename");
    }

}
