package org.example.dao;

import org.hibernate.Session;

import java.util.List;

interface GenericDao<T,ID> {
    void save(Session session,T entity);
    List<T> getAll(Session session);
    T getById(Session session, ID id);
    void update(Session session, T entity);
    void delete(Session session, T entity);

}
