package persistence.bootstrap;

import jakarta.persistence.Entity;
import persistence.sql.mapping.PersistentClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InFlightMetadataCollector {
    private final ComponentScanner componentScanner;
    private final Map<Class<?>, PersistentClass> persistentClassMap = new HashMap<>();

    public InFlightMetadataCollector(ComponentScanner componentScanner) {
        this.componentScanner = componentScanner;
    }

    public void collectMetadata(String basePackage) throws ClassNotFoundException {
        List<Class<?>> classes = componentScanner.scan(basePackage);
        classes.stream()
                .filter(clazz -> clazz.isAnnotationPresent(Entity.class))
                .forEach(clazz -> persistentClassMap.put(clazz, PersistentClass.from(clazz)));
    }

    public PersistentClass getPersistentClass(Class<?> clazz) {
        return persistentClassMap.get(clazz);
    }

    public Map<Class<?>, PersistentClass> getPersistentClassMap() {
        return persistentClassMap;
    }
}
