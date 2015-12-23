package app.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class MyEntity implements Serializable {

    @Id
    private String name;

    protected MyEntity() {
    }

    public MyEntity(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "MyEntity{" +
                "name=" + name +
                '}';
    }
}
