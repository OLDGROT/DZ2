package org.example;

import org.example.model.User;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.example.util.HibernateUtil;
import org.hibernate.SessionFactory;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Scanner scanner = new Scanner(System.in);
        UserRepository userRepo = new UserRepository(sessionFactory);
        RoleRepository roleRepo = new RoleRepository(sessionFactory);
        UserService userService = new UserService(userRepo, roleRepo);

        while (true) {
            System.out.println("1. Создать пользователя");
            System.out.println("2. Показать всех пользователей");
            System.out.println("3. Удалить пользователя");
            System.out.println("0. Выход");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Имя: ");
                    String name = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Возраст: ");
                    int age = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Доступные роли:");
                    roleRepo.getAll().forEach(role ->
                            System.out.println(role.getId() + " - " + role.getName())
                    );
                    System.out.print("Выберите ID роли: ");
                    Long roleId = scanner.nextLong();
                    scanner.nextLine();

                    userService.createUser(name, email, age, roleId);
                }
                case 2 -> userService.getAllUsers().forEach(u ->
                        System.out.println(u.getId() + " " + u.getName() + " " + u.getEmail() + " " +
                                (u.getRole() != null ? u.getRole().getName() : "нет роли"))
                );
                case 3 -> {
                    System.out.print("ID пользователя для удаления: ");
                    Long id = scanner.nextLong();
                    User user = userRepo.getById(id);
                    if (user != null) userService.deleteUser(user);
                    else System.out.println("Пользователь не найден");
                }
                case 0 -> System.exit(0);
            }
        }
    }
}