package org.example.dao;

import org.example.exception.RepositoryException;
import org.example.model.Role;
import org.example.model.User;
import org.example.util.TestHibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoIntegrationTest {

    private SessionFactory sessionFactory;
    private UserDao userDao;
    private User user;

    @BeforeAll
    void setUp() {
        sessionFactory = TestHibernateUtil.getSessionFactory();
        userDao = new UserDao(sessionFactory);
    }

    @AfterEach
    void cleanDatabase() {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.createMutationQuery("DELETE FROM User").executeUpdate();
            session.createMutationQuery("DELETE FROM Role").executeUpdate();
            tx.commit();
        }
    }

    @BeforeEach
    void initUser() {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Role role = new Role("Test role");
            session.persist(role);
            this.user = new User("Tester", "test@email.com", 23, role);
            userDao.save(session, user);
            tx.commit();

        }
    }

    @AfterAll
    void tearDown() {
        TestHibernateUtil.shutdown();
    }

    @Test
    void testSave() {
        Role role = new Role("USER");
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(role);
            User user = new User("Alex", "alex@mail.com", 25, role);
            userDao.save(session, user);
            tx.commit();
        }

    }

    @Test
    void testGetAll(){
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            List<User> users = userDao.getAll(session);
            tx.commit();
            assertEquals(1, users.size());
        }
    }

    @Test
    void testGetById() {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            User getsUser = userDao.getById(session, user.getId());
            tx.commit();
            assertNotNull(user);
            assertEquals("Tester", user.getName());
        } catch (RepositoryException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testUpdateAndDelete() {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            user.setAge(29);
            userDao.update(session, user);
            tx.commit();
        }
    }

    @Test
    void testDelete() {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            userDao.delete(session, user);
            tx.commit();
        }

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            User deletedUser = userDao.getById(session, user.getId());
            tx.commit();
            assertNull(deletedUser);
        }
    }
}
