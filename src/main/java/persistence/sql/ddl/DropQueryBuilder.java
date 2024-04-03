package persistence.sql.ddl;

import persistence.sql.mapping.Table;

public class DropQueryBuilder {
    private final Table table;

    public DropQueryBuilder(Class<?> clazz) {
        this.table = Table.from(clazz);
    }

    public String build() {
        return String.format("DROP TABLE %s", table.getName());
    }
}
