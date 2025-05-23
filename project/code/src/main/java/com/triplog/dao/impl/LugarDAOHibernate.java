package com.triplog.dao.impl;

import com.triplog.dao.DAOException;
import com.triplog.dao.LugarDAO;
import com.triplog.model.Lugar;
import com.triplog.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public abstract class LugarDAOHibernate extends GenericDAOHibernate<Lugar, Long> implements LugarDAO {
    public LugarDAOHibernate() {
        super(Lugar.class);
    }

    @Override
    public List<Lugar> findByNombreLugar(String nombre) throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<Lugar> query = session.createQuery(
                        "FROM Lugar WHERE nombre LIKE :nombre", Lugar.class
                );
                query.setParameter("nombre", "%" + nombre + "%");
                List<Lugar> result = query.getResultList();
                transaction.commit();
                return result;
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new DAOException("Error al buscar entidad por nombre lugar", e);
            }
        }
    }

    @Override
    public List<Lugar> findByDireccion(String direccion) throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<Lugar> query = session.createQuery(
                        "FROM Lugar WHERE direccion LIKE :direccion", Lugar.class
                );
                query.setParameter("direccion", "%" + direccion + "%");
                List<Lugar> result = query.getResultList();
                transaction.commit();
                return result;
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new DAOException("Error al buscar entidad por titulo", e);
            }
        }
    }

    @Override
    public List<Lugar> findByFecha(LocalDate fecha) throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<Lugar> query = session.createQuery(
                        "FROM Lugar WHERE fechaVisita < :fecha", Lugar.class
                );
                query.setParameter("fecha", fecha);
                List<Lugar> result = query.getResultList();
                transaction.commit();
                return result;
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new DAOException("Error al buscar entidad por titulo", e);
            }
        }
    }

    @Override
    public List<Lugar> findByViaje(Long idViaje) throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<Lugar> query = session.createQuery(
                        "SELECT l FROM Lugar l JOIN Ubicacion u ON l.id = u.idLugar.id WHERE u.idViaje.id = :idViaje", Lugar.class
                );
                query.setParameter("idViaje", idViaje);
                List<Lugar> result = query.getResultList();
                transaction.commit();
                return result;
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new DAOException("Error al buscar entidad por titulo", e);
            }
        }
    }
}
