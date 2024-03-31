package persistence.bootstrap;

import jakarta.persistence.Entity;
import persistence.sql.mapping.PersistentClass;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InFlightMetadataCollector {
    private final ComponentScanner componentScanner;
    private final Map<String, PersistentClass> persistentClassMap = new HashMap<>();

    public InFlightMetadataCollector(ComponentScanner componentScanner) {
        this.componentScanner = componentScanner;
    }

    public void collectMetadata() throws ClassNotFoundException {
        List<Class<?>> classes = componentScanner.scan("persistence.bootstrap");
        classes.stream()
                .filter(clazz -> clazz.isAnnotationPresent(Entity.class))
                .forEach(clazz -> persistentClassMap.put(clazz.getName(), PersistentClass.from(clazz)));
    }

    public PersistentClass getPersistentClass(Class<?> clazz) {
        return persistentClassMap.get(clazz.getName());
    }
}
