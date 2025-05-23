package com.triplog.dao;

import com.triplog.model.Tarea;

import java.time.LocalDate;
import java.util.List;

public interface TareaDAO extends GenericDAO<Tarea, Long> {
    List<Tarea> findByEstado(Boolean estado) throws DAOException;
    List<Tarea> findByUsuario(Long idUsuario) throws DAOException;
    List<Tarea> findByViaje(Long idViaje) throws DAOException;
    List<Tarea> findByFecha(LocalDate fecha) throws DAOException; // Viajes que terminan antes de esa fecha

}
