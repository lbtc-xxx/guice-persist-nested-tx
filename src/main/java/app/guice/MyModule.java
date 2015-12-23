package app.guice;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;

public class MyModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new JpaPersistModule("myJpaUnit"));
    }
}
