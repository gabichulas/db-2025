package com.triplog.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "ubicacion", schema = "triplog")
public class Ubicacion {
    @EmbeddedId
    private UbicacionId id;

    @MapsId("idViaje")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_viaje", nullable = false)
    private Viaje idViaje;

    @MapsId("idLugar")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_lugar", nullable = false)
    private Lugar idLugar;

    public UbicacionId getId() {
        return id;
    }

    public void setId(UbicacionId id) {
        this.id = id;
    }

    public Viaje getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(Viaje idViaje) {
        this.idViaje = idViaje;
    }

    public Lugar getIdLugar() {
        return idLugar;
    }

    public void setIdLugar(Lugar idLugar) {
        this.idLugar = idLugar;
    }

}