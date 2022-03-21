package com.netjob.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.netjob.app.domain.Favorito} entity.
 */
public class FavoritoDTO implements Serializable {

    private Long id;

    private UsuarioDTO usuario;

    private ServicioDTO servicio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(o instanceof FavoritoDTO)) {
            return false;
        }

        FavoritoDTO favoritoDTO = (FavoritoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, favoritoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FavoritoDTO{" +
            "id=" + getId() +
            ", usuario=" + getUsuario() +
            ", servicio=" + getServicio() +
            "}";
    }
}
