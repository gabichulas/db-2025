package com.triplog.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

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

    @ColumnDefault("'9999-01-01'")
    @Column(name = "fecha_fin_visita", nullable = false)
    private LocalDate fechaFinVisita;

    @ColumnDefault("'9999-01-01'")
    @Column(name = "fecha_inicio_visita", nullable = false)
    private LocalDate fechaInicioVisita;

    public LocalDate getFechaInicioVisita() {
        return fechaInicioVisita;
    }

    public void setFechaInicioVisita(LocalDate fechaInicioVisita) {
        this.fechaInicioVisita = fechaInicioVisita;
    }

    public LocalDate getFechaFinVisita() {
        return fechaFinVisita;
    }

    public void setFechaFinVisita(LocalDate fechaFinVisita) {
        this.fechaFinVisita = fechaFinVisita;
    }

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