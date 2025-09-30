package org.example.dao;

import org.example.exception.RepositoryException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GenericDaoImpl<T, ID> implements GenericDao<T, ID> {
    private static final Logger logger = LoggerFactory.getLogger(GenericDaoImpl.class);

    private final SessionFactory sessionFactory;
    private final Class<T> type;

    public GenericDaoImpl(SessionFactory sessionFactory, Class<T> type) {
        this.sessionFactory = sessionFactory;
        this.type = type;
    }

    @Override
    public void save(T entity) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            logger.info("Сохраняем объект {}: {}", type.getSimpleName(), entity);
            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            logger.info("Объект {} успешно сохранён", type.getSimpleName());
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Ошибка при сохранении объекта {}: {}", type.getSimpleName(), e.getMessage(), e);
            throw new RepositoryException("Ошибка сохранения:" + type.getSimpleName(), e);
        }
    }

    @Override
    public List<T> getAll() {
        try (Session session = sessionFactory.openSession()) {
            logger.info("Получаем все объекты типа {}", type.getSimpleName());
            List<T> result = session.createQuery("from " + type.getSimpleName(), type).getResultList();
            logger.info("Найдено {} объектов типа {}", result.size(), type.getSimpleName());
            return result;
        } catch (Exception e) {
            logger.error("Ошибка при получении всех объектов {}: {}", type.getSimpleName(), e.getMessage(), e);
            throw new RepositoryException("Ошибка получения всех объектов:" + type.getSimpleName(), e);
        }
    }

    @Override
    public T getById(ID id) {
        try (Session session = sessionFactory.openSession()) {
            logger.info("Получаем объект {} с id={}", type.getSimpleName(), id);
            T entity = session.get(type, id);
            if (entity != null) {
                logger.info("Объект {} найден: {}", type.getSimpleName(), entity);
            } else {
                logger.warn("Объект {} с id={} не найден", type.getSimpleName(), id);
            }
            return entity;
        } catch (Exception e) {
            logger.error("Ошибка при получении объекта {} с id={}: {}", type.getSimpleName(), id, e.getMessage(), e);
            throw new RepositoryException("Ошибка получения объекта:" + type.getSimpleName(), e);
        }
    }

    @Override
    public void update(T entity) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            logger.info("Обновляем объект {}: {}", type.getSimpleName(), entity);
            tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
            logger.info("Объект {} успешно обновлён", type.getSimpleName());
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Ошибка при обновлении объекта {}: {}", type.getSimpleName(), e.getMessage(), e);
            throw new RepositoryException("Ошибка обновления:" + type.getSimpleName(), e);
        }
    }

    @Override
    public void delete(T entity) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            logger.info("Удаляем объект {}: {}", type.getSimpleName(), entity);
            tx = session.beginTransaction();
            session.remove(entity);
            tx.commit();
            logger.info("Объект {} успешно удалён", type.getSimpleName());
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logger.error("Ошибка при удалении объекта {}: {}", type.getSimpleName(), e.getMessage(), e);
            throw new RepositoryException("Ошибка удаления:" + type.getSimpleName(), e);
        }
    }
}
