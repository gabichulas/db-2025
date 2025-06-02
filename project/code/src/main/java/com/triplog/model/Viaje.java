package com.triplog.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "viaje", schema = "triplog")
public class Viaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_viaje", nullable = false)
    private Long id;

    @Column(name = "titulo", nullable = false, length = 50)
    private String titulo;

    @Column(name = "destino", nullable = false, length = 100)
    private String destino;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_creador")
    private Usuario idCreador;

    @OneToMany(mappedBy = "idViaje")
    private Set<Gasta> gastas = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idViaje")
    private Set<Participa> participas = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idViaje")
    private Set<Tarea> tareas = new LinkedHashSet<>();

    @ManyToMany
    private Set<Lugar> lugars = new LinkedHashSet<>();

    public Viaje(){}

    public Viaje(String titulo, String destino, LocalDate fechaInicio, LocalDate fechaFin, Usuario idCreador) {
        this.titulo = titulo;
        this.destino = destino;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.idCreador = idCreador;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Usuario getIdCreador() {
        return idCreador;
    }

    public void setIdCreador(Usuario idCreador) {
        this.idCreador = idCreador;
    }

    public Set<Gasta> getGastas() {
        return gastas;
    }

    public void setGastas(Set<Gasta> gastas) {
        this.gastas = gastas;
    }

    public Set<Participa> getParticipas() {
        return participas;
    }

    public void setParticipas(Set<Participa> participas) {
        this.participas = participas;
    }

    public Set<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(Set<Tarea> tareas) {
        this.tareas = tareas;
    }

    public Set<Lugar> getLugars() {
        return lugars;
    }

    public void setLugars(Set<Lugar> lugars) {
        this.lugars = lugars;
    }

}