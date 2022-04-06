package com.netjob.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.netjob.app.domain.enumeration.Disponibilidad;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Servicio.
 */
@Entity
@Table(name = "servicio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Servicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 60)
    @Column(name = "titulo", length = 60, nullable = false)
    private String titulo;

    @NotNull
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "disponibilidad", nullable = false)
    private Disponibilidad disponibilidad;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "preciohora", nullable = false)
    private Float preciohora;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "preciotraslado", nullable = false)
    private Float preciotraslado;

    @NotNull
    @Column(name = "fechacreacion", nullable = false)
    private Instant fechacreacion;

    @NotNull
    @Column(name = "fechaactualizacion", nullable = false)
    private Instant fechaactualizacion;

    @NotNull
    @Column(name = "destacado", nullable = false)
    private Boolean destacado;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "conversacions" }, allowSetters = true)
    private Usuario usuario;

    @ManyToMany
    @JoinTable(
        name = "rel_servicio__categoria",
        joinColumns = @JoinColumn(name = "servicio_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "servicios" }, allowSetters = true)
    private Set<Categoria> categorias = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Servicio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Servicio titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Servicio descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Disponibilidad getDisponibilidad() {
        return this.disponibilidad;
    }

    public Servicio disponibilidad(Disponibilidad disponibilidad) {
        this.setDisponibilidad(disponibilidad);
        return this;
    }

    public void setDisponibilidad(Disponibilidad disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public Float getPreciohora() {
        return this.preciohora;
    }

    public Servicio preciohora(Float preciohora) {
        this.setPreciohora(preciohora);
        return this;
    }

    public void setPreciohora(Float preciohora) {
        this.preciohora = preciohora;
    }

    public Float getPreciotraslado() {
        return this.preciotraslado;
    }

    public Servicio preciotraslado(Float preciotraslado) {
        this.setPreciotraslado(preciotraslado);
        return this;
    }

    public void setPreciotraslado(Float preciotraslado) {
        this.preciotraslado = preciotraslado;
    }

    public Instant getFechacreacion() {
        return this.fechacreacion;
    }

    public Servicio fechacreacion(Instant fechacreacion) {
        this.setFechacreacion(fechacreacion);
        return this;
    }

    public void setFechacreacion(Instant fechacreacion) {
        this.fechacreacion = fechacreacion;
    }

    public Instant getFechaactualizacion() {
        return this.fechaactualizacion;
    }

    public Servicio fechaactualizacion(Instant fechaactualizacion) {
        this.setFechaactualizacion(fechaactualizacion);
        return this;
    }

    public void setFechaactualizacion(Instant fechaactualizacion) {
        this.fechaactualizacion = fechaactualizacion;
    }

    public Boolean getDestacado() {
        return this.destacado;
    }

    public Servicio destacado(Boolean destacado) {
        this.setDestacado(destacado);
        return this;
    }

    public void setDestacado(Boolean destacado) {
        this.destacado = destacado;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Servicio usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    public Set<Categoria> getCategorias() {
        return this.categorias;
    }

    public void setCategorias(Set<Categoria> categorias) {
        this.categorias = categorias;
    }

    public Servicio categorias(Set<Categoria> categorias) {
        this.setCategorias(categorias);
        return this;
    }

    public Servicio addCategoria(Categoria categoria) {
        this.categorias.add(categoria);
        categoria.getServicios().add(this);
        return this;
    }

    public Servicio removeCategoria(Categoria categoria) {
        this.categorias.remove(categoria);
        categoria.getServicios().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Servicio)) {
            return false;
        }
        return id != null && id.equals(((Servicio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Servicio{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", disponibilidad='" + getDisponibilidad() + "'" +
            ", preciohora=" + getPreciohora() +
            ", preciotraslado=" + getPreciotraslado() +
            ", fechacreacion='" + getFechacreacion() + "'" +
            ", fechaactualizacion='" + getFechaactualizacion() + "'" +
            ", destacado='" + getDestacado() + "'" +
            "}";
    }
}
