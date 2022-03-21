package com.netjob.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.netjob.app.domain.Contrato} entity.
 */
public class ContratoDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    private Float preciofinal;

    @NotNull
    private Instant fecha;

    private UsuarioDTO usuario;

    private ServicioDTO servicio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPreciofinal() {
        return preciofinal;
    }

    public void setPreciofinal(Float preciofinal) {
        this.preciofinal = preciofinal;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
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
        if (!(o instanceof ContratoDTO)) {
            return false;
        }

        ContratoDTO contratoDTO = (ContratoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contratoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContratoDTO{" +
            "id=" + getId() +
            ", preciofinal=" + getPreciofinal() +
            ", fecha='" + getFecha() + "'" +
            ", usuario=" + getUsuario() +
            ", servicio=" + getServicio() +
            "}";
    }
}
