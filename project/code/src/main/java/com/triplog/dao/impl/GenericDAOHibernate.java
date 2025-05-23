package com.triplog.dao.impl;

import com.triplog.dao.DAOException;
import com.triplog.dao.GenericDAO;
import com.triplog.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public abstract class GenericDAOHibernate<T, ID> implements GenericDAO<T, ID> {
    private final Class<T> persistentClass;

    protected GenericDAOHibernate(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    @Override
    public Optional<T> findById(ID id) throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                T entity = session.find(persistentClass, id);
                transaction.commit();
                return Optional.ofNullable(entity);
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new DAOException("Error al buscar entidad por ID: " + id, e);
            }
        }
    }

    @Override
    public List<T> findAll() throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<T> query = session.createQuery(
                        "FROM " + persistentClass.getName(), persistentClass);
                List<T> result = query.getResultList();
                transaction.commit();
                return result;
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new DAOException("Error al recuperar todas las entidades", e);
            }
        }
    }

    @Override
    public T save(T entity) throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(entity);
                transaction.commit();
                return entity;
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new DAOException("Error al guardar la entidad", e);
            }
        }
    }

    @Override
    public T update(T entity) throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                T mergedEntity = session.merge(entity);
                transaction.commit();
                return mergedEntity;
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new DAOException("Error al actualizar la entidad", e);
            }
        }
    }

    @Override
    public void delete(T entity) throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.remove(entity);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new DAOException("Error al eliminar la entidad", e);
            }
        }
    }

    @Override
    public void deleteById(ID id) throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                T entity = session.find(persistentClass, id);
                if (entity != null) {
                    session.remove(entity);
                }
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new DAOException("Error al eliminar la entidad por id", e);
            }
        }
    }
}