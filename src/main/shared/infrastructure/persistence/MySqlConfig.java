package main.shared.infrastructure.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class MySqlConfig {

    protected SessionFactory sessionFactory;

    public MySqlConfig() {
        this.sessionFactory = new Configuration()
                .configure("main/shared/infrastructure/persistence/orm/hibernate.cfg.xml")
                .buildSessionFactory();

    }
}