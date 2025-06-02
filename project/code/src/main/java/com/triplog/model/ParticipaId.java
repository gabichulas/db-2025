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
    private Long idViaje;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    public ParticipaId(Long id, Long id1) {
    }

    public ParticipaId() {

    }

    public Long getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(Long idViaje) {
        this.idViaje = idViaje;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
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