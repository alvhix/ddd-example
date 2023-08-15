package shared.infrastructure.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testcontainers.containers.MariaDBContainer;

public class MariaDB {

    protected SessionFactory sessionFactory;

    public MariaDB() {
        this.sessionFactory = new Configuration()
                .configure()
                .setProperty("hibernate.connection.url", System.getenv("database.url"))
                .setProperty("hibernate.connection.username", System.getenv("database.username"))
                .setProperty("hibernate.connection.password", System.getenv("database.password"))
                .buildSessionFactory();
    }

    public MariaDB(MariaDBContainer<?> mariadb) {
        this.sessionFactory = new Configuration()
                .configure()
                .setProperty("hibernate.connection.url", mariadb.getJdbcUrl())
                .setProperty("hibernate.connection.username", mariadb.getUsername())
                .setProperty("hibernate.connection.password", mariadb.getPassword())
                .buildSessionFactory();
    }
}