package persistence.sql.dml;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.Order;
import persistence.model.Person;
import persistence.sql.mapping.PersistentClass;

import static org.assertj.core.api.Assertions.assertThat;
import static persistence.sql.dml.BooleanExpression.eq;

class SelectQueryBuilderTest {
    private final SelectQueryBuilder selectQueryBuilder = new SelectQueryBuilder(PersistentClass.from(Person.class));

    @Test
    @DisplayName("요구사항2: findAll 쿼리 생성")
    void testFindAll() {
        String expected = "select users.id, users.nick_name, users.old, users.email from users";
        WhereBuilder booleanBuilder = new WhereBuilder();
        String selectQuery = selectQueryBuilder.build(booleanBuilder);

        assertThat(selectQuery).isEqualTo(expected);
    }

    @Test
    @DisplayName("요구사항3: findById 쿼리 생성")
    void testFindById() {
        int id = 1;
        String expected = String.format("select users.id, users.nick_name, users.old, users.email from users where id = %s", id);
        WhereBuilder booleanBuilder = new WhereBuilder();
        booleanBuilder.and(eq("id", id));
        String selectQuery = selectQueryBuilder.build(booleanBuilder);

        assertThat(selectQuery).isEqualTo(expected);
    }

    @Test
    @DisplayName("join 쿼리 생성 테스트")
    void testFindWithJoin() {
        SelectQueryBuilder sut = new SelectQueryBuilder(PersistentClass.from(Order.class));
        int id = 1;
        String expected = String.format(
                "select orders.id, orders.order_number, order_items.id, order_items.product, order_items.quantity from orders join order_items on orders.id = order_items.order_id where id = %s",
                id
        );
        WhereBuilder booleanBuilder = new WhereBuilder();
        booleanBuilder.and(eq("id", id));

        String selectQuery = sut.build(booleanBuilder);

        assertThat(selectQuery).isEqualTo(expected);
    }
}
