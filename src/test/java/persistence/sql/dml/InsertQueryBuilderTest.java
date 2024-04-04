package persistence.sql.dml;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.Person;
import persistence.sql.mapping.PersistentClass;

import static org.assertj.core.api.Assertions.assertThat;

class InsertQueryBuilderTest {
    InsertQueryBuilder insertQueryBuilder = new InsertQueryBuilder(PersistentClass.from(Person.class));

    @Test
    @DisplayName("요구사항1: insert 쿼리 생성")
    void testGenerateInsertQuery() {
        String nickName = "nickName";
        int age = 20;
        String email = "email";
        Person person = new Person(null, nickName, age, email, null);
        String expected = String.format(
                "insert into users (old, nick_name, email) values (%s, '%s', '%s')",
                age,
                nickName,
                email
        );

        String insertQuery = insertQueryBuilder.build(person);

        assertThat(insertQuery).isEqualTo(expected);
    }
}
