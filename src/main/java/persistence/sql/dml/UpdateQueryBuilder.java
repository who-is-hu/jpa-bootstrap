package persistence.sql.dml;

import persistence.sql.mapping.PersistentClass;
import persistence.sql.mapping.Values;

import java.util.stream.Collectors;

public class UpdateQueryBuilder {
    public static final String SET_COLUMN_FORMAT = "%s = %s";
    private final PersistentClass persistentClass;

    public UpdateQueryBuilder(PersistentClass persistentClass) {
        this.persistentClass = persistentClass;
    }

    public String build(Object entity, WhereBuilder whereBuilder) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("update ");
        stringBuilder.append(persistentClass.getTableName());
        stringBuilder.append(" set ");
        stringBuilder.append(valueClause(entity));

        if (whereBuilder.isEmpty()) {
            return stringBuilder.toString();
        }

        stringBuilder.append(whereBuilder.toClause());

        return stringBuilder.toString();
    }

    private String valueClause(Object entity) {
        Values values = Values.fromEntity(entity, persistentClass.getColumns());
        return values.getEntries().stream()
                .map(entry -> String.format(SET_COLUMN_FORMAT, entry.getKey(), ValueUtil.getValueString(entry.getValue())))
                .collect(Collectors.joining(", "));
    }
}
