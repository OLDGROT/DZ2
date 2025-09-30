package org.example.service;

import org.example.exception.ServiceException;
import org.example.model.Role;
import org.example.model.User;
import org.example.dao.RoleDao;
import org.example.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserDao userDao;
    private final RoleDao roleDao;

    public UserService(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    public void createUser(String name, String email, int age, Long roleId) {
        try {
            logger.info("Создание пользователя: {}, email={}, age={}, roleId={}", name, email, age, roleId);

            Role role = roleDao.getById(roleId);
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

            userDao.save(user);
            logger.info("Пользователь {} успешно создан", name);
        } catch (Exception e) {
            logger.error("Ошибка при создании пользователя {}: {}", name, e.getMessage(), e);
            throw new ServiceException("Ошибка при создании пользователя", e);
        }
    }

    public List<User> getAllUsers() {
        try {
            logger.info("Получение всех пользователей");
            return userDao.getAll();
        } catch (Exception e) {
            logger.error("Ошибка при получении всех пользователей: {}", e.getMessage(), e);
            throw new ServiceException("Ошибка при получении всех пользователей", e);
        }
    }

    public void updateUser(User user) {
        try {
            logger.info("Обновление пользователя: {}", user.getId());
            userDao.update(user);
            logger.info("Пользователь {} успешно обновлён", user.getId());
        } catch (Exception e) {
            logger.error("Ошибка при обновлении пользователя {}: {}", user.getId(), e.getMessage(), e);
            throw new ServiceException("Ошибка при обновлении пользователя", e);
        }
    }

    public void deleteUser(User user) {
        try {
            logger.info("Удаление пользователя: {}", user.getId());
            userDao.delete(user);
            logger.info("Пользователь {} успешно удалён", user.getId());
        } catch (Exception e) {
            logger.error("Ошибка при удалении пользователя {}: {}", user.getId(), e.getMessage(), e);
            throw new ServiceException("Ошибка при удалении пользователя", e);
        }
    }
}
