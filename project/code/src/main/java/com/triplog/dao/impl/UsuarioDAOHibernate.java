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

    public Boolean verifyUsuario(String email, String password) throws DAOException {
        if (email == null || email.isBlank() || password == null || password.isEmpty()) {
            return false;
        }
        if (!email.matches("[^@]+@[^@]+\\.[^@]+") || email.length() > 254) {
            return false;
        }
        Usuario usuario = findByEmail(email).orElse(null);
        if (usuario != null) {
            System.out.println("Usuario encontrado");
            System.out.println(usuario.verifyContrasena(password));
            System.out.println(password);
            System.out.println(usuario.getContrasena());
            return usuario.verifyContrasena(password);
        }
        return false;

    }

    public List<Usuario> getParticipantesDelViaje(Long viajeId) throws DAOException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Query<Usuario> query = session.createQuery(
                        "SELECT u FROM Usuario u " +
                                "JOIN Participa p ON u.id = p.idUsuario.id " +
                                "WHERE p.idViaje.id = :viajeId", Usuario.class
                );
                query.setParameter("viajeId", viajeId);
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
