package persistence.entity;

import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jdbc.RowMapper;
import persistence.sql.mapping.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultRowMapper<T> implements RowMapper<T> {
    private final PersistentClass persistentClass;
    private final List<Field> fields;

    public DefaultRowMapper(PersistentClass persistentClass) {
        this.persistentClass = persistentClass;
        this.fields = getFields(persistentClass.getEntityClass());
    }

    private List<Field> getFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Transient.class))
                .filter(field -> !field.isAnnotationPresent(OneToMany.class))
                .collect(Collectors.toList());
    }

    @Override
    public T mapRow(ResultSet resultSet) throws SQLException {
        Object entity = this.persistentClass.createEntity();
        TableData table = persistentClass.getTable();
        Associations associations = persistentClass.getAssociations();

        for (Field field : fields) {
            ColumnData columnData = ColumnData.createColumn(table.getName(), field);
            setValue(entity, field, columnData, resultSet);
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

    private void setValue(Object entity, Field field, ColumnData columnData, ResultSet resultSet) throws SQLException {
        field.setAccessible(true);
        innerSet(entity, field, resultSet.getObject(columnData.getNameWithTable()));
    }

    private List<Object> getChildren(OneToManyData association, ResultSet resultSet) throws SQLException {
        PersistentClass persistentClass = association.getReferencePersistentClass();

        final List<Object> children = new ArrayList<>();

        do {
            Object childEntity = persistentClass.createEntity();
            for (Field field : getFields(persistentClass.getEntityClass())) {
                field.setAccessible(true);
                ColumnData column = ColumnData.createColumn(persistentClass.getTableName(), field);
                innerSet(childEntity, field, resultSet.getObject(column.getNameWithTable()));
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
