package persistence.sql.mapping;

public class Table {
    private final String name;

    private Table(String name) {
        this.name = name;
    }

    public static Table from(Class<?> entityClazz) {
        return new Table(extractName(entityClazz));
    }

    private static String extractName(Class<?> entityClazz) {
        jakarta.persistence.Table table = entityClazz.getAnnotation(jakarta.persistence.Table.class);
        if (table == null) {
            return entityClazz.getSimpleName().toLowerCase();
        }
        return table.name();
    }
    public String getName() {
        return name;
    }
}
