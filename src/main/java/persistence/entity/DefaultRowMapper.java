package persistence.entity;

import jdbc.RowMapper;
import persistence.sql.mapping.Associations;
import persistence.sql.mapping.Column;
import persistence.sql.mapping.OneToManyData;
import persistence.sql.mapping.PersistentClass;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DefaultRowMapper<T> implements RowMapper<T> {
    private final PersistentClass persistentClass;

    public DefaultRowMapper(PersistentClass persistentClass) {
        this.persistentClass = persistentClass;
    }

    @Override
    public T mapRow(ResultSet resultSet) throws SQLException {
        Object entity = this.persistentClass.createEntity();
        Associations associations = persistentClass.getAssociations();

        for (Column column : persistentClass.getColumns()) {
            column.setValue(entity, resultSet.getObject(column.getNameWithTable()));
        }

        if (associations.hasEagerLoad()) {
            mapCollection(resultSet, entity, associations);
        }

        return (T) entity;
    }

    private void mapCollection(ResultSet resultSet, Object entity, Associations associations) throws SQLException {
        for (OneToManyData association : associations.getEagerAssociations()) {
            Field field = association.getField();
            field.setAccessible(true);
            innerSet(entity, field, getChildren(association, resultSet));
        }
    }

    private List<Object> getChildren(OneToManyData association, ResultSet resultSet) throws SQLException {
        PersistentClass persistentClass = association.getReferencePersistentClass();

        final List<Object> children = new ArrayList<>();

        do {
            Object childEntity = persistentClass.createEntity();
            for(Column column : persistentClass.getColumns()) {
                column.setValue(childEntity, resultSet.getObject(column.getNameWithTable()));
            }
            children.add(childEntity);
        } while (resultSet.next());
        return children;
    }

    private void innerSet(Object entity, Field field, Object value) throws SQLException {
        try {
            field.set(entity, value);
        } catch (IllegalAccessException e) {
            throw new SQLException(e);
        }
    }
}
