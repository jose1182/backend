package com.netjob.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Contrato.
 */
@Entity
@Table(name = "contrato")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Contrato implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "preciofinal", nullable = false)
    private Float preciofinal;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private Instant fecha;

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

    public Contrato id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPreciofinal() {
        return this.preciofinal;
    }

    public Contrato preciofinal(Float preciofinal) {
        this.setPreciofinal(preciofinal);
        return this;
    }

    public void setPreciofinal(Float preciofinal) {
        this.preciofinal = preciofinal;
    }

    public Instant getFecha() {
        return this.fecha;
    }

    public Contrato fecha(Instant fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Contrato usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    public Servicio getServicio() {
        return this.servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public Contrato servicio(Servicio servicio) {
        this.setServicio(servicio);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contrato)) {
            return false;
        }
        return id != null && id.equals(((Contrato) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contrato{" +
            "id=" + getId() +
            ", preciofinal=" + getPreciofinal() +
            ", fecha='" + getFecha() + "'" +
            "}";
    }
}
