package persistence.sql.dml;

import database.DatabaseServer;
import database.H2;
import jdbc.JdbcTemplate;
import org.junit.jupiter.api.*;
import persistence.model.Person;
import persistence.PersonRowMapper;
import persistence.sql.ddl.CreateQueryBuilder;
import persistence.sql.ddl.DropQueryBuilder;
import persistence.sql.dialect.H2Dialect;
import persistence.sql.mapping.PersistentClass;

import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class DMLQueryGeneratorH2DbTest {
    private static JdbcTemplate jdbcTemplate;
    private static DatabaseServer server;
    private final PersistentClass personPersistentClass = PersistentClass.from(Person.class);
    private final DropQueryBuilder dropQueryBuilder = new DropQueryBuilder(Person.class);
    private final CreateQueryBuilder createQueryBuilder = new CreateQueryBuilder(new H2Dialect(), Person.class);;
    private final SelectQueryBuilder selectQueryBuilder = new SelectQueryBuilder(personPersistentClass);
    private final InsertQueryBuilder insertQueryBuilder = new InsertQueryBuilder(personPersistentClass);

    @BeforeAll
    public static void tearUp() throws Exception {
        server = new H2();
        server.start();
        jdbcTemplate = new JdbcTemplate(server.getConnection());
    }

    @AfterAll
    public static void tearDown() {
        server.stop();
    }

    @BeforeEach
    public void setUp() {
        String sql = createQueryBuilder.build();
        jdbcTemplate.execute(sql);
    }

    @AfterEach
    public void cleanUp() {
        jdbcTemplate.execute(dropQueryBuilder.build());
    }

    @Test
    @DisplayName("h2db에 insert 하고 select 로 확인")
    void testInsert() {
        String nickName1 = "person1";
        String nickName2 = "person2";
        int age = 20;
        String email = "email";
        Person person1 = new Person(null, nickName1, age, email, null);
        Person person2 = new Person(null, nickName2, age, email, null);

        jdbcTemplate.execute(insertQueryBuilder.build(person1));
        jdbcTemplate.execute(insertQueryBuilder.build(person2));

        List<Person> persons = jdbcTemplate.query(
                selectQueryBuilder.build(new WhereBuilder()),
                new PersonRowMapper()
        );

        assertSoftly(softly -> {
            softly.assertThat(persons).hasSize(2);
            softly.assertThat(persons.get(0).getName()).isEqualTo(nickName1);
            softly.assertThat(persons.get(1).getName()).isEqualTo(nickName2);
        });
    }
}
