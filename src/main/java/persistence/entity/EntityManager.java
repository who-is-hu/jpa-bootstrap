package persistence.entity;

public interface EntityManager {
    <T> T find(Class<T> clazz, Object id);

    Object persist(Object entity);

    Object merge(Object entity);

    void remove(Object entity);
}
