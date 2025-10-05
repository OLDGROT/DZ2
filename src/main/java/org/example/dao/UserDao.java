package org.example.dao;

import org.example.model.User;
import org.hibernate.SessionFactory;

public class UserDao extends GenericDaoImpl<User, Long> {
    public UserDao(SessionFactory sessionFactory) {
        super(User.class);
    }
}
