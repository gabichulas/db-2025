package com.triplog.dao;

import com.triplog.model.Viaje;

import java.time.LocalDate;
import java.util.List;

public interface ViajeDAO extends GenericDAO<Viaje, Long> {
    List<Viaje> findByTitulo(String titulo) throws DAOException;
    List<Viaje> findByDestino(String destino) throws DAOException;
    //List<Viaje> findByFechaInicioBetween(LocalDate inicio, LocalDate fin) throws DAOException;
    List<Viaje> findByCreador(Long idCreador) throws DAOException;
    List<Viaje> findByParticipante(Long idUsuario) throws DAOException;
    List<Viaje> findByLugar(Long idLugar) throws DAOException;
}
