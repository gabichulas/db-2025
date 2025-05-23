package com.triplog.dao.impl;

import com.triplog.dao.DAOException;
import com.triplog.dao.TareaDAO;
import com.triplog.model.Tarea;
import com.triplog.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public abstract class TareaDAOHibernate extends GenericDAOHibernate<Tarea, Long> implements TareaDAO {
    public TareaDAOHibernate() {
        super(Tarea.class);
    }

    @Override
    public List<Tarea> findByEstado(Boolean estado) throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<Tarea> query = session.createQuery(
                        "FROM Tarea WHERE completada = :estado", Tarea.class
                );
                query.setParameter("estado", estado);
                List<Tarea> result = query.getResultList();
                transaction.commit();
                return result;
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new DAOException("Error al buscar tareas con estado " + estado, e);
            }



        }
    }

    @Override
    public List<Tarea> findByUsuario(Long idUsuario) throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<Tarea> query = session.createQuery(
                        "FROM Tarea WHERE idUsuarioAsignado = :idUsuario", Tarea.class
                );
                query.setParameter("idUsuario", idUsuario);
                List<Tarea> result = query.getResultList();
                transaction.commit();
                return result;
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new DAOException("Error al buscar entidad por usuario", e);
            }
        }
    }

    @Override
    public List<Tarea> findByViaje(Long idViaje) throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<Tarea> query = session.createQuery(
                        "FROM Tarea WHERE idViaje = :idViaje", Tarea.class
                );
                query.setParameter("idViaje", idViaje);
                List<Tarea> result = query.getResultList();
                transaction.commit();
                return result;
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new DAOException("Error al buscar entidad por viaje", e);
            }
        }
    }

    @Override
    public List<Tarea> findByFecha(LocalDate fecha) throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<Tarea> query = session.createQuery(
                        "FROM Tarea WHERE fechaLimite < :fecha", Tarea.class
                );
                query.setParameter("fecha", fecha);
                List<Tarea> result = query.getResultList();
                transaction.commit();
                return result;
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new DAOException("Error al buscar entidad por fecha", e);
            }
        }
    }
}
