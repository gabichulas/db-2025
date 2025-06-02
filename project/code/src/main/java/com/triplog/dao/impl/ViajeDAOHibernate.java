package com.triplog.dao.impl;

import com.triplog.dao.DAOException;
import com.triplog.dao.ViajeDAO;
import com.triplog.model.Viaje;
import com.triplog.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public abstract class ViajeDAOHibernate extends GenericDAOHibernate<Viaje, Long> implements ViajeDAO {
    public ViajeDAOHibernate() {
        super(Viaje.class);
    }

    @Override
    public List<Viaje> findByTitulo(String titulo) throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<Viaje> query = session.createQuery(
                        "FROM Viaje WHERE titulo LIKE :titulo", Viaje.class
                );
                query.setParameter("titulo", "%" + titulo + "%");
                List<Viaje> result = query.getResultList();
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
    public List<Viaje> findByDestino(String destino) throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<Viaje> query = session.createQuery(
                        "FROM Viaje WHERE destino LIKE :destino", Viaje.class
                );
                query.setParameter("destino", "%" + destino + "%");
                List<Viaje> result = query.getResultList();
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
    public List<Viaje> findByCreador(Long idCreador) throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<Viaje> query = session.createQuery(
                        "FROM Viaje v WHERE v.idCreador.id = :idCreador", Viaje.class
                );
                query.setParameter("idCreador", idCreador);
                List<Viaje> result = query.getResultList();
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
    public List<Viaje> findByParticipante(Long idUsuario) throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<Viaje> query = session.createQuery(
                        "SELECT DISTINCT v FROM Viaje v " +
                                "JOIN FETCH v.participas p " +       // Carga los participa
                                "JOIN FETCH p.idUsuario u " +          // Carga los usuarios de cada participa
                                "WHERE u.id = :idUsuario", Viaje.class
                );
                query.setParameter("idUsuario", idUsuario);
                List<Viaje> result = query.getResultList();
                System.out.println("Usuario " + idUsuario + " encontrado");
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
