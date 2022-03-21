package com.netjob.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.netjob.app.domain.Valoracion} entity.
 */
public class ValoracionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 500)
    private String descripcion;

    @NotNull
    private Instant fecha;

    @NotNull
    @Min(value = 0)
    @Max(value = 5)
    private Integer puntuacion;

    private UsuarioDTO usuario;

    private ServicioDTO servicio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        this.puntuacion = puntuacion;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public ServicioDTO getServicio() {
        return servicio;
    }

    public void setServicio(ServicioDTO servicio) {
        this.servicio = servicio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ValoracionDTO)) {
            return false;
        }

        ValoracionDTO valoracionDTO = (ValoracionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, valoracionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ValoracionDTO{" +
            "id=" + getId() +
            ", descripcion='" + getDescripcion() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", puntuacion=" + getPuntuacion() +
            ", usuario=" + getUsuario() +
            ", servicio=" + getServicio() +
            "}";
    }
}
