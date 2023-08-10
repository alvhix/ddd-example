package shared.infrastructure.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class MySql {

    protected SessionFactory sessionFactory;

    public MySql() {
        this.sessionFactory = new Configuration()
                .configure("main/java/shared/infrastructure/persistence/orm/hibernate.cfg.xml")
                .buildSessionFactory();

    }
}