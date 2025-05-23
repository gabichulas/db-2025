package com.triplog.dao.impl;

import com.triplog.dao.DAOException;
import com.triplog.dao.UsuarioDAO;
import com.triplog.model.Usuario;
import com.triplog.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public abstract class UsuarioDAOHibernate extends GenericDAOHibernate<Usuario, Long> implements UsuarioDAO {

    public UsuarioDAOHibernate(){
        super(Usuario.class);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            try {
                Query<Usuario> query = session.createQuery(
                        "FROM Usuario WHERE email = :email", Usuario.class
                );
                query.setParameter("email", email);
                Optional<Usuario> result = Optional.ofNullable(query.uniqueResult());
                transaction.commit();
                return result;
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new DAOException("Error al buscar usuario por email: " + email, e);
            }
        }
    }

    @Override
    public List<Usuario> findByNombre(String nombre) throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<Usuario> query = session.createQuery(
                        "FROM Usuario WHERE nombre like :nombre", Usuario.class
                );
                query.setParameter("nombre", "%" + nombre + "%");
                List<Usuario> result = query.getResultList();
                transaction.commit();
                return result;
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw new DAOException("Error al buscar usuario por nombre", e);
            }
        }
    }
}
