package com.triplog.dao;

import com.triplog.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioDAO extends GenericDAO<Usuario, Long> {
    Optional<Usuario> findByEmail(String email) throws DAOException;
    List<Usuario> findByNombre(String nombre) throws DAOException;
    List<Usuario> findByGasto(Long idGasto) throws DAOException;
}
