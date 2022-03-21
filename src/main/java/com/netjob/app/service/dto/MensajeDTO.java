package com.netjob.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.netjob.app.domain.Mensaje} entity.
 */
public class MensajeDTO implements Serializable {

    private Long id;

    @NotNull
    private String texto;

    @NotNull
    private Instant fecha;

    private UsuarioDTO emisor;

    private UsuarioDTO receptor;

    private ConversacionDTO conversacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public UsuarioDTO getEmisor() {
        return emisor;
    }

    public void setEmisor(UsuarioDTO emisor) {
        this.emisor = emisor;
    }

    public UsuarioDTO getReceptor() {
        return receptor;
    }

    public void setReceptor(UsuarioDTO receptor) {
        this.receptor = receptor;
    }

    public ConversacionDTO getConversacion() {
        return conversacion;
    }

    public void setConversacion(ConversacionDTO conversacion) {
        this.conversacion = conversacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MensajeDTO)) {
            return false;
        }

        MensajeDTO mensajeDTO = (MensajeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mensajeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MensajeDTO{" +
            "id=" + getId() +
            ", texto='" + getTexto() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", emisor=" + getEmisor() +
            ", receptor=" + getReceptor() +
            ", conversacion=" + getConversacion() +
            "}";
    }
}
