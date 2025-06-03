package com.triplog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UbicacionId implements Serializable {
    @Serial
    private static final long serialVersionUID = 7182360432873063130L;
    @Column(name = "id_viaje", nullable = false)
    private Integer idViaje;

    @Column(name = "id_lugar", nullable = false)
    private Integer idLugar;

    public UbicacionId() {}

    public Integer getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(Integer idViaje) {
        this.idViaje = idViaje;
    }

    public Integer getIdLugar() {
        return idLugar;
    }

    public void setIdLugar(Integer idLugar) {
        this.idLugar = idLugar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UbicacionId entity = (UbicacionId) o;
        return Objects.equals(this.idLugar, entity.idLugar) &&
                Objects.equals(this.idViaje, entity.idViaje);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idLugar, idViaje);
    }

}