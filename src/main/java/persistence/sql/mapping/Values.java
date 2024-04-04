package persistence.sql.mapping;

import java.util.*;
import java.util.stream.Collectors;

public class Values {
    private final Map<String, Object> values;

    private Values(Map<String, Object> values) {
        this.values = values;
    }

    public static Values fromEntity(Object entity, Columns columns) {
        Map<String, Object> map = columns.getNonPkColumns()
                .stream()
                .collect(Collectors.toMap(Column::getName, (columnData -> columnData.getValue(entity))));
        return new Values(map);
    }

    public List<Map.Entry<String, Object>> getEntries() {
        return new ArrayList<>(values.entrySet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Values values1 = (Values) o;

        return Objects.equals(values, values1.values);
    }

    @Override
    public int hashCode() {
        return values != null ? values.hashCode() : 0;
    }
}
