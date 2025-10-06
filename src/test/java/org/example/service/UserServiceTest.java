package org.example.service;

import jakarta.xml.bind.ValidationException;
import org.example.dao.RoleDao;
import org.example.dao.UserDao;
import org.example.exception.ServiceException;
import org.example.model.Role;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import org.example.exception.ValidException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserDao userDao;
    private RoleDao roleDao;
    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;
    private UserService userService;

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserServiceTest.class);

    @BeforeEach
    void setUp() {
        logger.info("----Начало теста----");
        userDao = mock(UserDao.class);
        roleDao = mock(RoleDao.class);
        sessionFactory = mock(SessionFactory.class);
        session = mock(Session.class);
        transaction = mock(Transaction.class);

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);

        userService = new UserService(userDao, roleDao, sessionFactory);
    }

    @Test
    void createUserSuccess() {
        Role role = new Role("ADMIN");
        role.setId(1L);
        when(roleDao.getById(session, 1L)).thenReturn(role);

        userService.createUser("Alex", "alex@mail.com", 25, 1L );

        verify(userDao, times(1)).save(eq(session), any(User.class));
        verify(transaction, times(1)).commit();
        logger.info("createUserSuccess выполнен успешно");
    }

    @Test
    void createUserRoleNotFoundThrowsServiceException() {
        when(roleDao.getById(session, 999L)).thenReturn(null);

        assertThrows(ServiceException.class, () ->
                userService.createUser("Alex", "alex@mail.com", 25, 999L));

        verify(userDao, never()).save(any(), any());
        verify(transaction, never()).commit();
        logger.info("createUserRoleNotFoundThrowsServiceException выполнен успешно");
    }



    @Test
    void getAllUsersSuccess() {
        List<User> users = Collections.singletonList(new User());
        when(userDao.getAll(session)).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        verify(transaction, times(1)).commit();
        logger.info("getAllUsersSuccess выполнен успешно");
    }

    @Test
    void updateUserSuccess() {
        User user = new User();
        user.setId(1L);
        user.setName("Alex");
        user.setEmail("alex@mail.com");
        user.setAge(25);
        user.setCreated_at(LocalDateTime.now());

        userService.updateUser(user);

        verify(userDao, times(1)).update(eq(session), eq(user));
        verify(transaction, times(1)).commit();
        logger.info("updateUserSuccess выполнен успешно");
    }

    @Test
    void updateUserValidationFails() {
        User user = new User();
        user.setId(1L);
        user.setEmail("");

        assertThrows(ValidException.class, () -> userService.updateUser(user));
        verify(userDao, never()).update(any(), any());
        logger.info("updateUserValidationFails выполнен успешно");
    }

    @Test
    void deleteUserSuccess() {
        User user = new User();
        user.setId(1L);
        user.setName("Alex");
        user.setEmail("alex@email.com");

        userService.deleteUser(user);

        verify(userDao, times(1)).delete(eq(session), eq(user));
        verify(transaction, times(1)).commit();
        logger.info("deleteUserSuccess выполнен успешно");
    }

    @Test
    void deleteUserValidationFails() {
        User user = new User();
        user.setId(1L);
        user.setEmail("");

        assertThrows(ValidException.class, () -> userService.deleteUser(user));
        verify(userDao, never()).delete(any(), any());
        logger.info("deleteUserValidationFails выполнен успешно");
    }

    @Test
    void getByIdSuccess() {
        User user = new User();
        user.setId(1L);
        when(userDao.getById(session, 1L)).thenReturn(user);

        User result = userService.getById(1L);

        assertNotNull(result);
        verify(transaction, times(1)).commit();
        logger.info("getByIdSuccess выполнен успешно");
    }

    @AfterEach
    void tearDown() {
        logger.info("----Конец теста----");
    }
}
