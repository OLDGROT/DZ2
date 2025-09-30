package org.example.dao;

import java.util.List;

interface GenericDao<T,ID> {
    void save(T entity);
    List<T> getAll();
    T getById(ID id);
    void update(T entity);
    void delete(T entity);

}
