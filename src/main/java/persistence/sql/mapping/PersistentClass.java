package persistence.sql.mapping;
public class PersistentClass {
    private final TableData table;
    private final Columns columns;

    private PersistentClass(TableData table, Columns columns) {
        this.table = table;
        this.columns = columns;
    }

    public static PersistentClass from(Class<?> clazz) {
        TableData table = TableData.from(clazz);
        Columns columns = Columns.createColumns(clazz);
        return new PersistentClass(table, columns);
    }
}
