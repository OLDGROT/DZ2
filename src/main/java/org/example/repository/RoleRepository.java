package org.example.repository;

import org.example.model.Role;
import org.hibernate.SessionFactory;

public class RoleRepository extends GenericRepositoryImpl<Role, Long> {
    public RoleRepository(SessionFactory sessionFactory) {
        super(sessionFactory, Role.class);
    }
}
