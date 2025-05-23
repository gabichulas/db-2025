package com.triplog.dao;

import com.triplog.model.Gasto;

import java.util.List;

public interface GastoDAO extends GenericDAO<Gasto, Long> {
    List<Gasto> findByUsuario(Long idUsuario) throws DAOException;
}
