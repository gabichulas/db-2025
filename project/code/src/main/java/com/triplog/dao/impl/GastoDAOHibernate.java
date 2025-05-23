package com.triplog.dao.impl;

import com.triplog.dao.DAOException;
import com.triplog.dao.GastoDAO;
import com.triplog.model.Gasto;
import com.triplog.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public abstract class GastoDAOHibernate extends GenericDAOHibernate<Gasto, Long> implements GastoDAO {
    public GastoDAOHibernate() {
        super(Gasto.class);
    }

    @Override
    public List<Gasto> findByUsuario(Long idUsuario) throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<Gasto> query = session.createQuery(
                        "SELECT g FROM Gasto g JOIN Gasta gs ON g.id = gs.idGasto.id WHERE gs.idUsuario.id = :idUsuario", Gasto.class
                );
                query.setParameter("idUsuario", idUsuario);
                List<Gasto> result = query.getResultList();
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
