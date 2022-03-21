package com.netjob.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Valoracion.
 */
@Entity
@Table(name = "valoracion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Valoracion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 500)
    @Column(name = "descripcion", length = 500, nullable = false)
    private String descripcion;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    @NotNull
    @Min(value = 0)
    @Max(value = 5)
    @Column(name = "puntuacion", nullable = false)
    private Integer puntuacion;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "conversacions" }, allowSetters = true)
    private Usuario usuario;

    @ManyToOne
    @JsonIgnoreProperties(value = { "usuario", "categorias" }, allowSetters = true)
    private Servicio servicio;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Valoracion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Valoracion descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFecha() {
        return this.fecha;
    }

    public Valoracion fecha(Instant fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Integer getPuntuacion() {
        return this.puntuacion;
    }

    public Valoracion puntuacion(Integer puntuacion) {
        this.setPuntuacion(puntuacion);
        return this;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Valoracion usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    public Servicio getServicio() {
        return this.servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public Valoracion servicio(Servicio servicio) {
        this.setServicio(servicio);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Valoracion)) {
            return false;
        }
        return id != null && id.equals(((Valoracion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Valoracion{" +
            "id=" + getId() +
            ", descripcion='" + getDescripcion() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", puntuacion=" + getPuntuacion() +
            "}";
    }
}
