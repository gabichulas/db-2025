package com.triplog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ParticipaId implements Serializable {
    @Serial
    private static final long serialVersionUID = -2587110505206155265L;
    @Column(name = "id_viaje", nullable = false)
    private Integer idViaje;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    public Integer getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(Integer idViaje) {
        this.idViaje = idViaje;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ParticipaId entity = (ParticipaId) o;
        return Objects.equals(this.idUsuario, entity.idUsuario) &&
                Objects.equals(this.idViaje, entity.idViaje);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, idViaje);
    }

}