package org.example.service;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.example.exception.ServiceException;
import org.example.model.Role;
import org.example.model.User;
import org.example.dao.RoleDao;
import org.example.dao.UserDao;
import org.example.util.ValidationUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import org.example.util.HibernateUtil;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final SessionFactory sessionFactory;

    public UserService(UserDao userDao, RoleDao roleDao, SessionFactory sessionFactory) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.sessionFactory = sessionFactory;
    }
    @Valid
    public void createUser(String name, String email, int age, Long roleId) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()){
            logger.info("Создание пользователя: {}, email={}, age={}, roleId={}", name, email, age, roleId);

            tx = session.beginTransaction();
            Role role = roleDao.getById(session, roleId);
            if (role == null) {
                logger.warn("Роль с id={} не найдена", roleId);
                throw new ServiceException("Роль не найдена");
            }

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setAge(age);
            user.setRole(role);
            user.setCreated_at(LocalDateTime.now());
            ValidationUtil.validateEntity(user);

            userDao.save(session, user);
            logger.info("Пользователь {} успешно создан", name);
            tx.commit();
        } catch (ValidationException e){
            logger.error(e.getMessage());
        }
        catch (Exception e) {
            HibernateUtil.rollbackQuietly(tx, e.getMessage());
            logger.error("Ошибка при создании пользователя {}: {}", name, e.getMessage(), e);
            throw new ServiceException("Ошибка при создании пользователя", e);
        }
    }

    public List<User> getAllUsers() {
        Transaction tx = null;
        try(Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            logger.info("Получение всех пользователей");
            List<User> users = userDao.getAll(session);
            tx.commit();
            return users;
        } catch (Exception e) {
            HibernateUtil.rollbackQuietly(tx, e.getMessage());
            logger.error("Ошибка при получении всех пользователей: {}", e.getMessage(), e);
            throw new ServiceException("Ошибка при получении всех пользователей", e);
        }
    }

    public void updateUser(User user) {
        ValidationUtil.validateEntity(user);
        Transaction tx = null;
        try(Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            logger.info("Обновление пользователя: {}", user.getId());
            userDao.update(session, user);
            logger.info("Пользователь {} успешно обновлён", user.getId());
            tx.commit();
        } catch (ValidationException e){
            logger.error(e.getMessage());
        } catch (Exception e) {
            HibernateUtil.rollbackQuietly(tx, e.getMessage());
            logger.error("Ошибка при обновлении пользователя {}: {}", user.getId(), e.getMessage(), e);
            throw new ServiceException("Ошибка при обновлении пользователя", e);
        }
    }

    public void deleteUser(User user) {
        ValidationUtil.validateEntity(user);
        Transaction tx = null;
        try(Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            logger.info("Удаление пользователя: {}", user.getId());
            userDao.delete(session, user);
            logger.info("Пользователь {} успешно удалён", user.getId());
            tx.commit();
        } catch (ValidationException e){
            logger.error(e.getMessage());
        } catch (Exception e) {
            HibernateUtil.rollbackQuietly(tx, e.getMessage());
            logger.error("Ошибка при удалении пользователя {}: {}", user.getId(), e.getMessage(), e);
            throw new ServiceException("Ошибка при удалении пользователя", e);
        }
    }
}
