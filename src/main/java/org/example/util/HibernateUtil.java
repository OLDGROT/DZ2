package org.example.util;

import org.example.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Role.class);
            configuration.addAnnotatedClass(UserDiscount.class);
            configuration.addAnnotatedClass(UserCourses.class);
            configuration.addAnnotatedClass(Course.class);
            StandardServiceRegistryBuilder registryBuilder =
                    new StandardServiceRegistryBuilder()
                            .applySettings(configuration.getProperties());

            return configuration.buildSessionFactory(registryBuilder.build());
        } catch (Throwable ex) {
            System.err.println("Ошибка создания SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
