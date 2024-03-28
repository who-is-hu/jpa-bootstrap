package persistence.sql.mapping;

import jakarta.persistence.*;
import org.apache.commons.lang3.StringUtils;
import persistence.sql.mapping.exception.GenerationTypeMissingException;

import java.lang.reflect.Field;

public class ColumnData {
    private final Field field;
    private final String tableName;
    private final String name;
    private final int type;
    private final boolean isPk;
    private final GenerationType generationType;
    private final boolean isNullable;

    private ColumnData(
            Field field,
            String tableName,
            String name,
            int type,
            boolean isPk,
            GenerationType generationType,
            boolean isNullable
    ) {
        this.field = field;
        this.tableName = tableName;
        this.name = name;
        this.type = type;
        this.isPk = isPk;
        this.generationType = generationType;
        this.isNullable = isNullable;
    }

    public static ColumnData createColumn(String tableName, Field field) {
        Column column = field.getAnnotation(Column.class);
        return new ColumnData(
                field,
                tableName,
                extractName(field, column),
                extractDataType(field),
                extractIsPrimaryKey(field),
                extractGenerationType(field),
                extractIsNullable(column)
        );
    }
    private static String extractName(Field field, Column column) {
        String columnName = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(field.getName()), '_').toLowerCase();
        if (column != null && !column.name().isEmpty()) {
            return column.name();
        }
        return columnName;
    }

    private static int extractDataType(Field field) {
        return new DataTypeMapper().mapToSqlType(field.getType());
    }

    private static boolean extractIsNullable(Column column) {
        if (column == null) {
            return true;
        }
        return column.nullable();
    }

    private static GenerationType extractGenerationType(Field field) {
        GeneratedValue generatedValue = field.getAnnotation(GeneratedValue.class);
        if (generatedValue == null) {
            return null;
        }
        return generatedValue.strategy();
    }

    private static boolean extractIsPrimaryKey(Field field) {
        return field.isAnnotationPresent(Id.class);
    }

    public String getName() {
        return name;
    }
    public String getNameWithTable() {
        return String.format("%s.%s", tableName, name);
    }

    public boolean isPrimaryKey() {
        return isPk;
    }

    public boolean isNotPrimaryKey() {
        return !isPk;
    }

    public Object getValue(Object entity) {
        try {
            field.setAccessible(true);
            return field.get(entity);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public int getType() {
        return type;
    }

    public boolean isNotNullable() {
        return !isNullable;
    }

    public boolean hasGenerationType() {
        return generationType != null;
    }

    public GenerationType getGenerationType() {
        if(!hasGenerationType()) {
            throw new GenerationTypeMissingException();
        }
        return generationType;
    }
}
