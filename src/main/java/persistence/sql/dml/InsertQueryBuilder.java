package persistence.sql.dml;

import persistence.sql.mapping.Columns;
import persistence.sql.mapping.TableData;
import persistence.sql.mapping.Values;

import java.util.Map;
import java.util.stream.Collectors;

public class InsertQueryBuilder {
    private final TableData table;

    public InsertQueryBuilder(Class<?> clazz) {
        this.table = TableData.from(clazz);
    }

    public String build(Object entity) {
        Columns columns = Columns.createColumns(entity.getClass());
        Values values = Values.fromEntity(entity, columns);

        return String.format(
                "insert into %s (%s) values (%s)",
                table.getName(),
                columnClause(values),
                valueClause(values)
        );
    }

    private String columnClause(Values values) {
        return values.getEntries().stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(", "));
    }

    private String valueClause(Values values) {
        return values.getEntries().stream()
                .map(entry -> ValueUtil.getValueString(entry.getValue()))
                .collect(Collectors.joining(", "));
    }
}
