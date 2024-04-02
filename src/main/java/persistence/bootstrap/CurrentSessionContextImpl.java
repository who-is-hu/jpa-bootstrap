package persistence.bootstrap;

import persistence.entity.EntityManager;

import java.util.HashMap;
import java.util.Map;

public class CurrentSessionContextImpl implements CurrentSessionContext {
    private final Map<Thread, EntityManager> entityManagerMap = new HashMap<>();
    @Override
    public EntityManager currentSession() throws EntityManagerNotFoundException {
        if(hasNotBind()){
            throw new EntityManagerNotFoundException("No EntityManager found in the current thread");
        }
        return entityManagerMap.get(Thread.currentThread());
    }

    @Override
    public void putSession(EntityManager entityManager) {
        entityManagerMap.put(Thread.currentThread(), entityManager);
    }

    @Override
    public boolean hasBind() {
        try {
            return entityManagerMap.containsKey(Thread.currentThread());
        } catch (NullPointerException | ClassCastException e) {
            throw new RuntimeException("Error in hasBind method");
        }
    }

    private boolean hasNotBind() {
        return !hasBind();
    }
}
