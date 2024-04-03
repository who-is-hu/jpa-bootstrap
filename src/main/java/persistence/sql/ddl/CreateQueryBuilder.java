package persistence.sql.ddl;

import persistence.sql.dialect.Dialect;
import persistence.sql.mapping.Column;
import persistence.sql.mapping.Columns;
import persistence.sql.mapping.Table;

import java.util.ArrayList;

public class CreateQueryBuilder {
    private final Dialect dialect;
    private final Table table;
    private final Columns columns;


    public CreateQueryBuilder(Dialect dialect, Class<?> clazz) {
        this.dialect = dialect;
        this.table = Table.from(clazz);
        this.columns = Columns.createColumns(clazz);
    }

    public String build() {
        final String tableNameClause = table.getName();
        final String columnClause = getColumnClause();
        final String keyClause = getKeyClause();

        return String.format("CREATE TABLE %s (%s, %s)", tableNameClause, columnClause, keyClause);
    }

    private String getColumnClause() {
        ArrayList<String> columnStrings = new ArrayList<>();
        for(Column column : columns) {
            columnStrings.add(getColumnString(column));
        }
        return String.join(", ", columnStrings);
    }

    private String getColumnString(Column column) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(column.getName());
        stringBuilder.append(" ");
        stringBuilder.append(dialect.mapDataType(column.getType()));

        if(column.isNotNullable()) {
            stringBuilder.append(" NOT NULL");
        }
        if (column.hasGenerationType()) {
            stringBuilder.append(" ");
            stringBuilder.append(dialect.mapGenerationType(column.getGenerationType()));
        }

        return stringBuilder.toString();
    }

    private String getKeyClause() {
        Column keyColumn = columns.getPkColumn();
        return String.format("%s KEY (%s)", "PRIMARY", keyColumn.getName());
    }
}
