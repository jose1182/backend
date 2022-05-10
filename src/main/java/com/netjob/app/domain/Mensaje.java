package com.netjob.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Mensaje.
 */
@Entity
@Table(name = "mensaje")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Mensaje implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "texto", nullable = false)
    private String texto;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "conversacions" }, allowSetters = true)
    private Usuario emisor;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "conversacions" }, allowSetters = true)
    private Usuario receptor;

    @ManyToOne(cascade = { CascadeType.ALL })
    @JsonIgnoreProperties(value = { "usuarios" }, allowSetters = true)
    private Conversacion conversacion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Mensaje id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return this.texto;
    }

    public Mensaje texto(String texto) {
        this.setTexto(texto);
        return this;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Instant getFecha() {
        return this.fecha;
    }

    public Mensaje fecha(Instant fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Usuario getEmisor() {
        return this.emisor;
    }

    public void setEmisor(Usuario usuario) {
        this.emisor = usuario;
    }

    public Mensaje emisor(Usuario usuario) {
        this.setEmisor(usuario);
        return this;
    }

    public Usuario getReceptor() {
        return this.receptor;
    }

    public void setReceptor(Usuario usuario) {
        this.receptor = usuario;
    }

    public Mensaje receptor(Usuario usuario) {
        this.setReceptor(usuario);
        return this;
    }

    public Conversacion getConversacion() {
        return this.conversacion;
    }

    public void setConversacion(Conversacion conversacion) {
        this.conversacion = conversacion;
    }

    public Mensaje conversacion(Conversacion conversacion) {
        this.setConversacion(conversacion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mensaje)) {
            return false;
        }
        return id != null && id.equals(((Mensaje) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mensaje{" +
            "id=" + getId() +
            ", texto='" + getTexto() + "'" +
            ", fecha='" + getFecha() + "'" +
            "}";
    }
}
