package com.triplog.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "lugar", schema = "triplog")
public class Lugar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lugar", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = Integer.MAX_VALUE)
    private String nombre;

    @Column(name = "direccion", length = Integer.MAX_VALUE)
    private String direccion;

    @Column(name = "fecha_visita")
    private LocalDate fechaVisita;

    @ManyToMany
    @JoinTable(name = "ubicacion",
            joinColumns = @JoinColumn(name = "id_lugar"),
            inverseJoinColumns = @JoinColumn(name = "id_viaje"))
    private Set<Viaje> viajes = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDate getFechaVisita() {
        return fechaVisita;
    }

    public void setFechaVisita(LocalDate fechaVisita) {
        this.fechaVisita = fechaVisita;
    }

    public Set<Viaje> getViajes() {
        return viajes;
    }

    public void setViajes(Set<Viaje> viajes) {
        this.viajes = viajes;
    }

}