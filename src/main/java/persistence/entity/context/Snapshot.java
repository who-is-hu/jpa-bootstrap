package persistence.entity.context;

import persistence.sql.mapping.Columns;
import persistence.sql.mapping.Values;

import java.util.Map;
import java.util.Objects;

public class Snapshot {
    private final Values values;
    public Snapshot(Object entity) {
        Columns columns = Columns.createColumns(entity.getClass());
        values = Values.fromEntity(entity, columns);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Snapshot snapshot = (Snapshot) o;
        return Objects.equals(values, snapshot.values);
    }

    @Override
    public int hashCode() {
        return values != null ? values.hashCode() : 0;
    }
}
