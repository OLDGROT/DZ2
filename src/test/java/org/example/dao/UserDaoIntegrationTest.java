package org.example.dao;

import org.example.model.Role;
import org.example.model.User;
import org.example.util.TestHibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoIntegrationTest {

    private SessionFactory sessionFactory;
    private UserDao userDao;

    @BeforeEach
    void cleanDatabase() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createMutationQuery("DELETE FROM User").executeUpdate();
            session.createMutationQuery("DELETE FROM Role").executeUpdate();
            session.getTransaction().commit();
        }
    }


    @BeforeAll
    void setUp() {
        sessionFactory = TestHibernateUtil.getSessionFactory();
        userDao = new UserDao(sessionFactory);

        System.out.println("Маппинги Hibernate: " + sessionFactory.getMetamodel().getEntities());
    }

    @AfterAll
    void tearDown() {
        TestHibernateUtil.shutdown();
    }

    @Test
    void testSaveAndGetAll() {
        Role role = new Role();
        role.setName("USER");

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(role);

            User user = new User("Alex", "alex@mail.com", 25, role);
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
        Role role = new Role();
        role.setName("ADMIN");

        User savedUser;

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(role);

            savedUser = new User("John", "john@mail.com", 30, role);
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
        Role role = new Role();
        role.setName("TESTER");

        User user;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(role);

            user = new User("Mike", "mike@mail.com", 28, role);
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

        // Проверяем обновление
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
