package com.triplog.dao.impl;

import com.triplog.dao.DAOException;
import com.triplog.dao.ParticipaDAO;
import com.triplog.model.Participa;
import com.triplog.model.ParticipaId;
import com.triplog.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public abstract class ParticipaDAOHibernate extends GenericDAOHibernate<Participa, Long> implements ParticipaDAO {
    public ParticipaDAOHibernate(){
        super(Participa.class);
    }

    public List<Participa> findByViaje(Long idViaje) throws DAOException {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                            "FROM Participa p WHERE p.idViaje.id = :idViaje", Participa.class)
                    .setParameter("idViaje", idViaje)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Error obteniendo participantes del viaje", e);
        }
    }
}
