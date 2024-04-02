package persistence.bootstrap;

public class EntityManagerNotFoundException extends RuntimeException {
    public EntityManagerNotFoundException(String message) {
        super(message);
    }
}
