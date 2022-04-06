package com.netjob.app.service.dto;

import com.netjob.app.domain.enumeration.Disponibilidad;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.netjob.app.domain.Servicio} entity.
 */
public class ServicioDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 60)
    private String titulo;

    @NotNull
    private String descripcion;

    @NotNull
    private Disponibilidad disponibilidad;

    @NotNull
    @DecimalMin(value = "0")
    private Float preciohora;

    @NotNull
    @DecimalMin(value = "0")
    private Float preciotraslado;

    @NotNull
    private Instant fechacreacion;

    @NotNull
    private Instant fechaactualizacion;

    private UsuarioDTO usuario;

    private Set<CategoriaDTO> categorias = new HashSet<>();

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Disponibilidad getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(Disponibilidad disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public Float getPreciohora() {
        return preciohora;
    }

    public void setPreciohora(Float preciohora) {
        this.preciohora = preciohora;
    }

    public Float getPreciotraslado() {
        return preciotraslado;
    }

    public void setPreciotraslado(Float preciotraslado) {
        this.preciotraslado = preciotraslado;
    }

    public Instant getFechacreacion() {
        return fechacreacion;
    }

    public void setFechacreacion(Instant fechacreacion) {
        this.fechacreacion = fechacreacion;
    }

    public Instant getFechaactualizacion() {
        return fechaactualizacion;
    }

    public void setFechaactualizacion(Instant fechaactualizacion) {
        this.fechaactualizacion = fechaactualizacion;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public Set<CategoriaDTO> getCategorias() {
        return categorias;
    }

    public void setCategorias(Set<CategoriaDTO> categorias) {
        this.categorias = categorias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServicioDTO)) {
            return false;
        }

        ServicioDTO servicioDTO = (ServicioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, servicioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServicioDTO{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", disponibilidad='" + getDisponibilidad() + "'" +
            ", preciohora=" + getPreciohora() +
            ", preciotraslado=" + getPreciotraslado() +
            ", fechacreacion='" + getFechacreacion() + "'" +
            ", fechaactualizacion='" + getFechaactualizacion() + "'" +
            ", usuario=" + getUsuario() +
            ", categorias=" + getCategorias() +
            "}";
    }
}
