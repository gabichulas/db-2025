package com.triplog.model;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

import com.triplog.util.PasswordHasher;
@Entity
@Table(name = "usuario", schema = "triplog")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "email", nullable = false, length = Integer.MAX_VALUE)
    private String email;

    @Column(name = "contrasena", nullable = false, length = Integer.MAX_VALUE)
    private String contrasena;

    @OneToMany(mappedBy = "idUsuario")
    private Set<Gasta> gastas = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idUsuario")
    private Set<Participa> participas = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idUsuarioAsignado")
    private Set<Tarea> tareas = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idCreador")
    private Set<Viaje> viajes = new LinkedHashSet<>();

    public Usuario(String nombre, String email, String contrasena) {
        this.nombre = nombre;
        this.email = email;
        setContrasena(contrasena);
    }

    public Usuario() {}

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return this.contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = PasswordHasher.hashPassword(contrasena);
    }

    public boolean verifyContrasena(String contrasena){
        return PasswordHasher.verifyPassword(contrasena, this.contrasena);
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

    public Set<Viaje> getViajes() {
        return viajes;
    }

    public void setViajes(Set<Viaje> viajes) {
        this.viajes = viajes;
    }

}