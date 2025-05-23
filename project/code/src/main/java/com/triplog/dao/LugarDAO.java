package com.triplog.dao;

import com.triplog.model.Lugar;

import java.time.LocalDate;
import java.util.List;

public interface LugarDAO extends GenericDAO<Lugar, Long> {
    List<Lugar> findByNombreLugar(String nombre) throws DAOException;
    List<Lugar> findByDireccion(String direccion) throws DAOException;
    List<Lugar> findByFecha(LocalDate fecha) throws DAOException;
    List<Lugar> findByViaje(Long idViaje) throws DAOException;
}
