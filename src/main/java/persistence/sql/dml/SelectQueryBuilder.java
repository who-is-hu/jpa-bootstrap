package persistence.sql.dml;

import persistence.sql.mapping.*;

import java.util.ArrayList;

public class SelectQueryBuilder {
    private final TableData table;
    private final Columns columns;
    private final Associations associations;

    public SelectQueryBuilder(PersistentClass persistentClass) {
        this.table = persistentClass.getTable();
        this.columns = persistentClass.getColumns();
        this.associations = persistentClass.getAssociations();
    }

    public String build(WhereBuilder whereBuilder) {
        StringBuilder query = new StringBuilder();
        query.append("select ");
        query.append(selectClause(columns));
        if(associations.isNotEmpty()){
            query.append(getJoinTableSelect());
        }

        query.append(" from ");
        query.append(table.getName());

        if(associations.hasEagerLoad()) {
            JoinBuilder joinBuilder = new JoinBuilder(table, columns, associations);
            query.append(joinBuilder.build());
        }

        if(whereBuilder.isEmpty()) {
            return query.toString();
        }

        query.append(whereBuilder.toClause());

        return query.toString();
    }

    private String getJoinTableSelect() {
        StringBuilder stringBuilder = new StringBuilder();
        for(OneToManyData association : associations.getEagerAssociations()) {
            stringBuilder.append(", ");
            String line = selectClause(association.getReferencePersistentClass().getColumns());
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }

    private String selectClause(Columns columns) {
        return String.join(", ", columns.getNames());
    }
}
