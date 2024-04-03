package persistence.entity;

import jdbc.JdbcTemplate;
import persistence.entity.collection.CollectionLoader;
import persistence.entity.collection.PersistentList;
import persistence.entity.collection.PersistentSet;
import persistence.sql.dml.BooleanExpression;
import persistence.sql.dml.SelectQueryBuilder;
import persistence.sql.dml.WhereBuilder;
import persistence.sql.mapping.Associations;
import persistence.sql.mapping.Columns;
import persistence.sql.mapping.PersistentClass;

import java.util.List;
import java.util.Set;

public class EntityLoader {
    private final JdbcTemplate jdbcTemplate;
    private final PersistentClass persistentClass;
    public EntityLoader(JdbcTemplate jdbcTemplate, PersistentClass persistentClass) {
        this.jdbcTemplate = jdbcTemplate;
        this.persistentClass = persistentClass;
    }

    public <T> T find(Object id) {
        Columns columns = persistentClass.getColumns();
        Associations associations = persistentClass.getAssociations();

        SelectQueryBuilder selectQueryBuilder = new SelectQueryBuilder(persistentClass);
        WhereBuilder whereBuilder = new WhereBuilder();
        whereBuilder.and(BooleanExpression.eq(columns.getPkColumnName(), id));
        String query = selectQueryBuilder.build(whereBuilder);
        T entity = jdbcTemplate.queryForObject(query, new DefaultRowMapper<T>(persistentClass));

        if(associations.hasLazyLoad()) {
            setPersistentCollection(entity, id, associations);
        }

        return entity;
    }

    private <T> void setPersistentCollection(
            T entity,
            Object id,
            Associations associations
    ) {
        associations.getLazyAssociations().forEach(association -> {
            Class<?> collectionType = association.getField().getType();
            CollectionLoader collectionLoader = new CollectionLoader(jdbcTemplate, association);

            if (collectionType.equals(List.class)) {
                association.setCollectionToField(entity, new PersistentList<>(collectionLoader, id));
            } else if (collectionType.equals(Set.class)) {
                association.setCollectionToField(entity, new PersistentSet<>(collectionLoader, id));
            } else {
                throw new UnsupportedOperationException("Unsupported collection type: " + collectionType.getSimpleName());
            }
        });
    }
}
