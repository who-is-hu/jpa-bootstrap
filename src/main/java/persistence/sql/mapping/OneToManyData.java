package persistence.sql.mapping;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import persistence.entity.collection.PersistentCollection;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class OneToManyData {
    private final PersistentClass referencePersistentClass;
    private final String joinColumnName;
    private final FetchType fetchType;
    private final Field field;

    private OneToManyData(
            String joinColumnName,
            FetchType fetchType,
            Field field,
            PersistentClass referencePersistentClass
    ) {
        this.joinColumnName = joinColumnName;
        this.fetchType = fetchType;
        this.field = field;
        this.referencePersistentClass = referencePersistentClass;
    }

    public static OneToManyData from(Field field) {
        ParameterizedType genericType = (ParameterizedType) field.getGenericType();
        Class<?> referenceClazz = (Class<?>) genericType.getActualTypeArguments()[0];
        PersistentClass referencePersistentClass = PersistentClass.from(referenceClazz);

        OneToMany oneToMany = field.getAnnotation(OneToMany.class);
        JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);

        return new OneToManyData(
                joinColumn.name(),
                oneToMany.fetch(),
                field,
                referencePersistentClass
        );
    }

    public String getJoinColumnName() {
        return String.format("%s.%s", referencePersistentClass.getTableName(), joinColumnName);
    }

    public String getJoinTableName() {
        return referencePersistentClass.getTableName();
    }

    public boolean isLazyLoad() {
        return fetchType == FetchType.LAZY;
    }

    public PersistentClass getReferencePersistentClass() {
        return this.referencePersistentClass;
    }

    public Field getField() {
        return field;
    }

    public <T> void setCollectionToField(Object entity, PersistentCollection<T> collection) {
        field.setAccessible(true);
        try {
            field.set(entity, collection);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isEagerLoad() {
        return fetchType == FetchType.EAGER;
    }
}
