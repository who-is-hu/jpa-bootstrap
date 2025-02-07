package persistence.entity.collection;

import jdbc.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import persistence.entity.DefaultRowMapper;
import persistence.sql.dml.BooleanExpression;
import persistence.sql.dml.SelectQueryBuilder;
import persistence.sql.dml.WhereBuilder;
import persistence.sql.mapping.*;

import java.util.Collection;


public class CollectionLoader {
    private final JdbcTemplate jdbcTemplate;
    private final OneToManyData association;
    private static final Logger logger = LoggerFactory.getLogger(CollectionLoader.class);


    public CollectionLoader(
            JdbcTemplate jdbcTemplate,
            OneToManyData association
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.association = association;
    }

    public <T> Collection<T> loadCollection(Object parentId) {
        PersistentClass persistentClass = association.getReferencePersistentClass();

        SelectQueryBuilder selectQueryBuilder = new SelectQueryBuilder(persistentClass);
        WhereBuilder whereBuilder = new WhereBuilder();
        whereBuilder.and(BooleanExpression.eq(association.getJoinColumnName(), parentId));
        String query = selectQueryBuilder.build(whereBuilder);

        logger.debug("query: {}", query);
        return jdbcTemplate.query(query, new DefaultRowMapper<T>(persistentClass));
    }
}
