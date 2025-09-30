package org.example.dao;

import org.example.model.Role;
import org.hibernate.SessionFactory;

public class RoleDao extends GenericDaoImpl<Role, Long> {
    public RoleDao(SessionFactory sessionFactory) {
        super(sessionFactory, Role.class);
    }
}
