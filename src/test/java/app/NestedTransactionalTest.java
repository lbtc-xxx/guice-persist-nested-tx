package app;

import app.entity.MyEntity;
import app.guice.MyModule;
import app.service.EnclosingService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import org.junit.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.io.IOException;

import static app.service.EnclosedService.SAVED_BY_ENCLOSED;
import static app.service.EnclosingService.SAVED_BY_ENCLOSING;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(Enclosed.class)
public class NestedTransactionalTest {
    public static abstract class AbstractNestedTransactionTest {
        protected static EntityManagerFactory emf;
        protected static EntityManager em;
        protected static Injector injector;

        protected EnclosingService sut;

        @BeforeClass
        public static void initClass() {
            emf = Persistence.createEntityManagerFactory("myJpaUnit");
            em = emf.createEntityManager();
            injector = Guice.createInjector(new MyModule());
            injector.getInstance(PersistService.class).start();
        }

        @Before
        public void cleanTable() {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM MyEntity").executeUpdate();
            em.getTransaction().commit();
        }

        @Before
        public void getSut() {
            sut = injector.getInstance(EnclosingService.class);
        }

        @AfterClass
        public static void afterClass() {
            em.close();
            emf.close();
        }

        protected void assertBothEntitySaved() {
            assertThat(em.find(MyEntity.class, SAVED_BY_ENCLOSING).getName(), is(SAVED_BY_ENCLOSING));
            assertThat(em.find(MyEntity.class, SAVED_BY_ENCLOSED).getName(), is(SAVED_BY_ENCLOSED));
        }

        protected void assertBothEntityNotSaved() {
            assertThat(em.find(MyEntity.class, SAVED_BY_ENCLOSING), is(nullValue()));
            assertThat(em.find(MyEntity.class, SAVED_BY_ENCLOSED), is(nullValue()));
        }
    }

    public static class NoExceptionShouldCommitTest extends AbstractNestedTransactionTest {
        @Test
        public void noException_noException() {
            sut.noException_noException();
        }

        @Test
        public void noException_ioException() {
            sut.noException_ioException();
        }

        @Test
        public void noException_runtimeException() {
            sut.noException_runtimeException();
        }

        @Test
        public void noException_ioExceptionWillRollbackOn() {
            sut.noException_ioExceptionWithRollbackOn();
        }

        @After
        public void doAssert() {
            assertBothEntitySaved();
        }
    }

    public static class RuntimeExceptionShouldRollbackTest extends AbstractNestedTransactionTest {
        @Test(expected = RuntimeException.class)
        public void runtimeException_noException() {
            sut.runtimeException_noException();
        }

        @Test(expected = RuntimeException.class)
        public void runtimeException_ioException() {
            sut.runtimeException_ioException();
        }

        @Test(expected = RuntimeException.class)
        public void runtimeException_runtimeException() {
            sut.runtimeException_runtimeException();
        }

        @Test(expected = RuntimeException.class)
        public void runtimeException_ioExceptionWillRollbackOn() {
            sut.runtimeException_ioExceptionWithRollbackOn();
        }

        @After
        public void doAssert() {
            assertBothEntityNotSaved();
        }
    }

    public static class IOExceptionShouldCommitTest extends AbstractNestedTransactionTest {
        @Test(expected = IOException.class)
        public void ioException_noException() throws IOException {
            sut.ioException_noException();
        }

        @Test(expected = IOException.class)
        public void ioException_ioException() throws IOException {
            sut.ioException_ioException();
        }

        @Test(expected = IOException.class)
        public void noException_runtimeException() throws IOException {
            sut.ioException_runtimeException();
        }

        @Test(expected = IOException.class)
        public void noException_ioExceptionWillRollbackOn() throws IOException {
            sut.ioException_ioExceptionWithRollbackOn();
        }

        @After
        public void doAssert() {
            assertBothEntitySaved();
        }
    }

    public static class IOExceptionWithRollbackOnShouldRollbackTest extends AbstractNestedTransactionTest {
        @Test(expected = IOException.class)
        public void ioExceptionWithRollbackOn_noException() throws IOException {
            sut.ioExceptionWithRollbackOn_noException();
        }

        @Test(expected = IOException.class)
        public void ioExceptionWithRollbackOn_ioException() throws IOException {
            sut.ioExceptionWithRollbackOn_ioException();
        }

        @Test(expected = IOException.class)
        public void ioExceptionWithRollbackOn_runtimeException() throws IOException {
            sut.ioExceptionWithRollbackOn_runtimeException();
        }

        @Test(expected = IOException.class)
        public void ioExceptionWithRollbackOn_ioExceptionWillRollbackOn() throws IOException {
            sut.ioExceptionWithRollbackOn_ioExceptionWithRollbackOn();
        }

        @After
        public void doAssert() {
            assertBothEntityNotSaved();
        }
    }

}