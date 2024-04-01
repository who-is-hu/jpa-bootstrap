package persistence.sql.mapping;

import java.lang.reflect.InvocationTargetException;

public class PersistentClass {
    private final Class<?> entityClass;
    private final Table table;
    private final Columns columns;
    private final Associations associations;

    private PersistentClass(Class<?> entityClass, Table table, Columns columns, Associations associations) {
        this.entityClass = entityClass;
        this.table = table;
        this.columns = columns;
        this.associations = associations;
    }

    public static PersistentClass from(Class<?> clazz) {
        return new PersistentClass(
                clazz,
                Table.from(clazz),
                Columns.createColumns(clazz),
                Associations.fromEntityClass(clazz)
        );
    }

    public Class<?> getEntityClass() {
        return this.entityClass;
    }

    public Table getTable() {
        return table;
    }

    public Columns getColumns() {
        return columns;
    }

    public Associations getAssociations() {
        return associations;
    }

    public Column getPkColumn() {
        return columns.getPkColumn();
    }

    public String getTableName() {
        return table.getName();
    }

    public Object createEntity() throws RuntimeException {
        try {
            return this.entityClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
