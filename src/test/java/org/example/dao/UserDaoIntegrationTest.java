package org.example.dao;

import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private SessionFactory sessionFactory;
    private UserDao userDao;

    @BeforeAll
    void setUp() {
        Configuration configuration = new Configuration()
                .addAnnotatedClass(User.class)
                .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .setProperty("hibernate.hbm2ddl.auto", "update")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.connection.url", postgres.getJdbcUrl())
                .setProperty("hibernate.connection.username", postgres.getUsername())
                .setProperty("hibernate.connection.password", postgres.getPassword())
                .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");

        sessionFactory = configuration.buildSessionFactory();
        userDao = new UserDao(sessionFactory);
    }

    @AfterAll
    void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    void testSaveAndGetAll() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = new User();
            user.setName("Alex");
            user.setEmail("alex@mail.com");
            user.setAge(25);

            userDao.save(session, user);

            session.getTransaction().commit();
        }

        try (Session session = sessionFactory.openSession()) {
            List<User> users = userDao.getAll(session);
            assertEquals(1, users.size());
            assertEquals("Alex", users.get(0).getName());
        }
    }

    @Test
    void testGetById() {
        User savedUser;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            savedUser = new User();
            savedUser.setName("John");
            savedUser.setEmail("john@mail.com");
            savedUser.setAge(30);

            userDao.save(session, savedUser);

            session.getTransaction().commit();
        }

        try (Session session = sessionFactory.openSession()) {
            User user = userDao.getById(session, savedUser.getId());
            assertNotNull(user);
            assertEquals("John", user.getName());
        }
    }

    @Test
    void testUpdateAndDelete() {
        User user;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            user = new User();
            user.setName("Mike");
            user.setEmail("mike@mail.com");
            user.setAge(28);

            userDao.save(session, user);
            session.getTransaction().commit();
        }

        // Update
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            user.setAge(29);
            userDao.update(session, user);
            session.getTransaction().commit();
        }

        try (Session session = sessionFactory.openSession()) {
            User updatedUser = userDao.getById(session, user.getId());
            assertEquals(29, updatedUser.getAge());
        }

        // Delete
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            userDao.delete(session, user);
            session.getTransaction().commit();
        }

        try (Session session = sessionFactory.openSession()) {
            User deletedUser = userDao.getById(session, user.getId());
            assertNull(deletedUser);
        }
    }
}
