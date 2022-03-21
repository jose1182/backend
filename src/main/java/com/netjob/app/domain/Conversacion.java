package com.netjob.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Conversacion.
 */
@Entity
@Table(name = "conversacion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Conversacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToMany(mappedBy = "conversacions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "conversacions" }, allowSetters = true)
    private Set<Usuario> usuarios = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Conversacion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Usuario> getUsuarios() {
        return this.usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        if (this.usuarios != null) {
            this.usuarios.forEach(i -> i.removeConversacion(this));
        }
        if (usuarios != null) {
            usuarios.forEach(i -> i.addConversacion(this));
        }
        this.usuarios = usuarios;
    }

    public Conversacion usuarios(Set<Usuario> usuarios) {
        this.setUsuarios(usuarios);
        return this;
    }

    public Conversacion addUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
        usuario.getConversacions().add(this);
        return this;
    }

    public Conversacion removeUsuario(Usuario usuario) {
        this.usuarios.remove(usuario);
        usuario.getConversacions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Conversacion)) {
            return false;
        }
        return id != null && id.equals(((Conversacion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Conversacion{" +
            "id=" + getId() +
            "}";
    }
}
