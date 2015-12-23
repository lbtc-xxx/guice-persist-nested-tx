package app.service;

import app.entity.MyEntity;
import com.google.inject.persist.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.IOException;

public class EnclosingService {

    public static final String SAVED_BY_ENCLOSING = "saved by enclosing";
    private final EntityManager em;
    private final EnclosedService enclosedService;

    @Inject
    EnclosingService(final EntityManager em, final EnclosedService enclosedService) {
        this.em = em;
        this.enclosedService = enclosedService;
    }

    private void doSave() {
        em.persist(new MyEntity(SAVED_BY_ENCLOSING));
        em.flush();
    }


    // noException

    @Transactional
    public void noException_noException() {
        doSave();
        enclosedService.noException();
    }

    @Transactional
    public void noException_runtimeException() {
        doSave();
        try {
            enclosedService.runtimeException();
        } catch (RuntimeException e) {
            // nop
        }
    }

    @Transactional
    public void noException_ioException() {
        doSave();
        try {
            enclosedService.ioException();
        } catch (IOException e) {
            // nop
        }
    }

    @Transactional
    public void noException_ioExceptionWithRollbackOn() {
        doSave();
        try {
            enclosedService.ioExceptionWithRollbackOn();
        } catch (IOException e) {
            // nop
        }
    }


    // runtimeException

    @Transactional
    public void runtimeException_noException() {
        doSave();
        enclosedService.noException();
        throw new RuntimeException();
    }

    @Transactional
    public void runtimeException_ioException() {
        doSave();
        try {
            enclosedService.ioException();
        } catch (IOException e) {
            // nop
        }
        throw new RuntimeException();
    }

    @Transactional
    public void runtimeException_runtimeException() {
        doSave();
        enclosedService.runtimeException();
    }

    @Transactional
    public void runtimeException_ioExceptionWithRollbackOn() {
        doSave();
        try {
            enclosedService.ioExceptionWithRollbackOn();
        } catch (IOException e) {
            // nop
        }
        throw new RuntimeException();
    }


    // ioException

    @Transactional
    public void ioException_noException() throws IOException {
        doSave();
        enclosedService.noException();
        throw new IOException();
    }

    @Transactional
    public void ioException_ioException() throws IOException {
        doSave();
        enclosedService.ioException();
    }

    @Transactional
    public void ioException_runtimeException() throws IOException {
        doSave();
        try {
            enclosedService.runtimeException();
        } catch (RuntimeException e) {
            // nop
        }
        throw new IOException();
    }

    @Transactional
    public void ioException_ioExceptionWithRollbackOn() throws IOException {
        doSave();
        enclosedService.ioExceptionWithRollbackOn();
    }


    // ioExceptionWithRollbackOn

    @Transactional(rollbackOn = IOException.class)
    public void ioExceptionWithRollbackOn_noException() throws IOException {
        doSave();
        enclosedService.noException();
        throw new IOException();
    }

    @Transactional(rollbackOn = IOException.class)
    public void ioExceptionWithRollbackOn_ioException() throws IOException {
        doSave();
        enclosedService.ioException();
    }

    @Transactional(rollbackOn = IOException.class)
    public void ioExceptionWithRollbackOn_runtimeException() throws IOException {
        doSave();
        try {
            enclosedService.runtimeException();
        } catch (RuntimeException e) {
            // nop
        }
        throw new IOException();
    }

    @Transactional(rollbackOn = IOException.class)
    public void ioExceptionWithRollbackOn_ioExceptionWithRollbackOn() throws IOException {
        doSave();
        enclosedService.ioExceptionWithRollbackOn();
    }
}
