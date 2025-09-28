package org.example.repository;

import java.util.List;

interface GenericRepository<T,ID> {
    void save(T entity);
    List<T> getAll();
    T getById(ID id);
    void update(T entity);
    void delete(T entity);

}
