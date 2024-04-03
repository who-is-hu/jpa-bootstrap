package persistence.sql.mapping;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import persistence.sql.ddl.exception.AnnotationMissingException;
import persistence.sql.ddl.exception.IdAnnotationMissingException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Columns implements Iterable<Column> {

    private final List<Column> columns;

    private Columns(List<Column> columns) {
        this.columns = columns;
    }

    @Override
    public Iterator<Column> iterator() {
        return columns.iterator();
    }

    public static Columns createColumns(Class<?> clazz) {
        checkIsEntity(clazz);
        Table table = Table.from(clazz);
        List<Column> columns = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Transient.class))
                .filter(field -> !field.isAnnotationPresent(OneToMany.class))
                .map(field -> Column.createColumn(table.getName(), field))
                .collect(Collectors.toList());

        checkHasPrimaryKey(columns);
        return new Columns(columns);
    }

    public List<String> getNames() {
        return columns.stream()
                .map(Column::getNameWithTable)
                .collect(Collectors.toList());
    }

    public List<Column> getNonPkColumns() {
        return columns.stream()
                .filter(Column::isNotPrimaryKey)
                .collect(Collectors.toList());
    }

    public Column getPkColumn() {
        return columns.stream()
                .filter(Column::isPrimaryKey)
                .findFirst()
                .orElseThrow(IdAnnotationMissingException::new);
    }

    public String getPkColumnName() {
        return getPkColumn().getNameWithTable();
    }

    private static void checkIsEntity(Class<?> entityClazz) {
        if (!entityClazz.isAnnotationPresent(Entity.class)) {
            throw new AnnotationMissingException("Entity 어노테이션이 없습니다.");
        }
    }

    private static void checkHasPrimaryKey(List<Column> columns) {
        if (columns.stream().noneMatch(Column::isPrimaryKey)) {
            throw new IdAnnotationMissingException();
        }
    }
}
