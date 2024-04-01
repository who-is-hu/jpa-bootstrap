package persistence.entity;

import jakarta.persistence.Id;
import jdbc.JdbcTemplate;
import persistence.sql.ddl.exception.IdAnnotationMissingException;
import persistence.sql.dml.DeleteQueryBuilder;
import persistence.sql.dml.InsertQueryBuilder;
import persistence.sql.dml.UpdateQueryBuilder;
import persistence.sql.dml.WhereBuilder;
import persistence.sql.mapping.Column;
import persistence.sql.mapping.Columns;
import persistence.sql.mapping.PersistentClass;
import persistence.sql.mapping.Table;

import java.lang.reflect.Field;
import java.util.Arrays;

import static persistence.sql.dml.BooleanExpression.eq;

public class EntityPersisterImpl implements EntityPersister {
    private final GeneratedIdObtainStrategy generatedIdObtainStrategy;
    private final JdbcTemplate jdbcTemplate;
    private final PersistentClass persistentClass;

    public EntityPersisterImpl(
            GeneratedIdObtainStrategy generatedIdObtainStrategy,
            JdbcTemplate jdbcTemplate,
            PersistentClass persistentClass
    ) {
        this.generatedIdObtainStrategy = generatedIdObtainStrategy;
        this.jdbcTemplate = jdbcTemplate;
        this.persistentClass = persistentClass;
    }

    @Override
    public boolean update(Object entity) {
        Column keyColumn = persistentClass.getPkColumn();

        if(keyColumn.hasNotValue(entity)) {
            return false;
        }

        UpdateQueryBuilder updateQueryBuilder = new UpdateQueryBuilder(persistentClass);
        WhereBuilder whereBuilder = new WhereBuilder();
        whereBuilder.and(eq(keyColumn.getNameWithTable(), keyColumn.getValue(entity)));

        jdbcTemplate.execute(updateQueryBuilder.build(entity, whereBuilder));

        return true;
    }

    @Override
    public void insert(Object entity) {
        Class<?> clazz = entity.getClass();
        InsertQueryBuilder insertQueryBuilder = new InsertQueryBuilder(PersistentClass.from(clazz));

        jdbcTemplate.execute(insertQueryBuilder.build(entity));

        setIdToEntity(entity, clazz);
    }

    private void setIdToEntity(Object entity, Class<?> clazz) {
        Object generatedId = jdbcTemplate.queryForObject(
                generatedIdObtainStrategy.getQueryString(),
                generatedIdObtainStrategy.getRowMapper()
        );

        Field idField = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow();
        idField.setAccessible(true);

        try {
            idField.set(entity, generatedId);
        } catch (IllegalAccessException e) {
            throw new IdAnnotationMissingException();
        }
    }


    @Override
    public void delete(Object entity) {
        Class<?> clazz = entity.getClass();
        Table table = Table.from(clazz);
        Column idColumn = Columns.createColumns(entity.getClass()).getPkColumn();

        DeleteQueryBuilder deleteQueryBuilder = new DeleteQueryBuilder(clazz);
        WhereBuilder builder = new WhereBuilder();
        builder.and(eq(idColumn.getNameWithTable(), idColumn.getValue(entity)));

        jdbcTemplate.execute(deleteQueryBuilder.build(builder));
    }
}
