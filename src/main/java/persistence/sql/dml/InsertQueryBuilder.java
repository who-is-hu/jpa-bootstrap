package persistence.sql.dml;

import persistence.sql.mapping.PersistentClass;
import persistence.sql.mapping.Values;

import java.util.Map;
import java.util.stream.Collectors;

public class InsertQueryBuilder {
    private final PersistentClass persistentClass;

    public InsertQueryBuilder(PersistentClass persistentClass) {
        this.persistentClass = persistentClass;
    }

    public String build(Object entity) {
        Values values = Values.fromEntity(entity, persistentClass.getColumns());

        return String.format(
                "insert into %s (%s) values (%s)",
                persistentClass.getTableName(),
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
