package com.triplog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GastaId implements Serializable {
    @Serial
    private static final long serialVersionUID = 2002374702961695612L;
    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "id_gasto", nullable = false)
    private Integer idGasto;

    public GastaId() {}

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdGasto() {
        return idGasto;
    }

    public void setIdGasto(Integer idGasto) {
        this.idGasto = idGasto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GastaId entity = (GastaId) o;
        return Objects.equals(this.idGasto, entity.idGasto) &&
                Objects.equals(this.idUsuario, entity.idUsuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idGasto, idUsuario);
    }

}