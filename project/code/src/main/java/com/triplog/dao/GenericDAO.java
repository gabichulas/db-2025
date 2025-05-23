package com.triplog.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDAO<T, ID> {
    Optional<T> findById(ID id) throws DAOException;
    List<T> findAll() throws DAOException;
    T save(T entity) throws DAOException;
    T update(T entity) throws DAOException;
    void delete(T entity) throws DAOException;
    void deleteById(ID id) throws DAOException;
}
