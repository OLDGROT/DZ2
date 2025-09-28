package org.example.service;

import org.example.exception.ServiceException;
import org.example.model.Role;
import org.example.model.User;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;

    public UserService(UserRepository userRepo, RoleRepository roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    public void createUser(String name, String email, int age, Long roleId) {
        try {
            logger.info("Создание пользователя: {}, email={}, age={}, roleId={}", name, email, age, roleId);

            Role role = roleRepo.getById(roleId);
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

            userRepo.save(user);
            logger.info("Пользователь {} успешно создан", name);
        } catch (Exception e) {
            logger.error("Ошибка при создании пользователя {}: {}", name, e.getMessage(), e);
            throw new ServiceException("Ошибка при создании пользователя", e);
        }
    }

    public List<User> getAllUsers() {
        try {
            logger.info("Получение всех пользователей");
            return userRepo.getAll();
        } catch (Exception e) {
            logger.error("Ошибка при получении всех пользователей: {}", e.getMessage(), e);
            throw new ServiceException("Ошибка при получении всех пользователей", e);
        }
    }

    public void updateUser(User user) {
        try {
            logger.info("Обновление пользователя: {}", user.getId());
            userRepo.update(user);
            logger.info("Пользователь {} успешно обновлён", user.getId());
        } catch (Exception e) {
            logger.error("Ошибка при обновлении пользователя {}: {}", user.getId(), e.getMessage(), e);
            throw new ServiceException("Ошибка при обновлении пользователя", e);
        }
    }

    public void deleteUser(User user) {
        try {
            logger.info("Удаление пользователя: {}", user.getId());
            userRepo.delete(user);
            logger.info("Пользователь {} успешно удалён", user.getId());
        } catch (Exception e) {
            logger.error("Ошибка при удалении пользователя {}: {}", user.getId(), e.getMessage(), e);
            throw new ServiceException("Ошибка при удалении пользователя", e);
        }
    }
}
