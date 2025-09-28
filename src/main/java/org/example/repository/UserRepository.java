package org.example.repository;

import org.example.model.User;
import org.hibernate.SessionFactory;

public class UserRepository extends GenericRepositoryImpl<User, Long> {
    public UserRepository(SessionFactory sessionFactory) {
        super(sessionFactory, User.class);
    }
}
