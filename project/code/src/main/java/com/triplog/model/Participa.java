package com.triplog.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "participa", schema = "triplog")
public class Participa {
    @EmbeddedId
    private ParticipaId id;

    @MapsId("idViaje")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_viaje", nullable = false)
    private Viaje idViaje;

    @MapsId("idUsuario")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario idUsuario;

    @Column(name = "es_organizador", nullable = false)
    private Boolean esOrganizador = false;

    public Participa() {
    }

    // Constructor conveniente
    public Participa(Viaje viaje, Usuario usuario, boolean esOrganizador) {
        this.idViaje = viaje;
        this.idUsuario = usuario;
        this.esOrganizador = esOrganizador;
        this.id = new ParticipaId(viaje.getId(), usuario.getId());
    }

    public ParticipaId getId() {
        return id;
    }

    public void setId(ParticipaId id) {
        this.id = id;
    }

    public Viaje getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(Viaje idViaje) {
        this.idViaje = idViaje;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Boolean getEsOrganizador() {
        return esOrganizador;
    }

    public void setEsOrganizador(Boolean esOrganizador) {
        this.esOrganizador = esOrganizador;
    }

}