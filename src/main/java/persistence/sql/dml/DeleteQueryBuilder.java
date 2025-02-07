package persistence.sql.dml;

import persistence.sql.mapping.Table;

public class DeleteQueryBuilder {
    private final Table table;

    public DeleteQueryBuilder(Class<?> clazz) {
        this.table = Table.from(clazz);
    }

    public String build(WhereBuilder whereBuilder) {
        StringBuilder query = new StringBuilder();
        query.append("delete from ");
        query.append(table.getName());

        if(whereBuilder.isEmpty()) {
            return query.toString();
        }

        query.append(whereBuilder.toClause());

        return query.toString();
    }
}
