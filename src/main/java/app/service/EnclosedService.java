package app.service;

import app.entity.MyEntity;
import com.google.inject.persist.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.IOException;

public class EnclosedService {

    public static final String SAVED_BY_ENCLOSED = "saved by enclosed";
    private final EntityManager em;

    @Inject
    EnclosedService(final EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void noException() {
        doSave();
    }

    @Transactional
    public void runtimeException() {
        doSave();
        throw new RuntimeException("should be rolled back");
    }

    @Transactional
    public void ioException() throws IOException {
        doSave();
        throw new IOException("should NOT be rolled back because not specified as rollbackOn");
    }

    @Transactional(rollbackOn = IOException.class)
    public void ioExceptionWithRollbackOn() throws IOException {
        doSave();
        throw new IOException("should be rolled back");
    }

    private void doSave() {
        em.persist(new MyEntity(SAVED_BY_ENCLOSED));
        em.flush();
    }
}
