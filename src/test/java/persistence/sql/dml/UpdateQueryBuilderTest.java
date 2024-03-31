package persistence.sql.dml;

import org.junit.jupiter.api.Test;
import persistence.Person;
import persistence.sql.mapping.Columns;
import persistence.sql.mapping.PersistentClass;
import persistence.sql.mapping.TableData;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateQueryBuilderTest {
    @Test
    void testUpdateQueryBuilder() {
        Person person = new Person(1L, "test", 10, "test", null);
        String expected = String.format("update users set old = %d, nick_name = '%s', email = '%s' where id = %d",
                person.getAge(),
                person.getName(),
                person.getEmail(),
                person.getId()
        );
        Class<?> clazz = Person.class;
        UpdateQueryBuilder updateQueryBuilder = new UpdateQueryBuilder(PersistentClass.from(Person.class));
        WhereBuilder whereBuilder = new WhereBuilder();
        whereBuilder.and(BooleanExpression.eq("id", 1L));

        String query = updateQueryBuilder.build(person, whereBuilder);

        assertThat(query).isEqualTo(expected);
    }

}
