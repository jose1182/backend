package com.netjob.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Usuario.
 */
@Entity
@Table(name = "usuario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 40)
    @Column(name = "nombre", length = 40, nullable = false)
    private String nombre;

    @Size(max = 50)
    @Column(name = "apellidos", length = 50)
    private String apellidos;

    @NotNull
    @Size(max = 255)
    @Column(name = "correo", length = 255, nullable = false)
    private String correo;

    @NotNull
    @Pattern(regexp = "[0-9]{8}[TRWAGMYFPDXBNJZSQVHLCKE]")
    @Column(name = "dni", nullable = false)
    private String dni;

    @NotNull
    @Column(name = "direccion", nullable = false)
    private String direccion;

    @NotNull
    @Column(name = "localidad", nullable = false)
    private String localidad;

    @NotNull
    @Column(name = "provincia", nullable = false)
    private String provincia;

    @Column(name = "profesion")
    private String profesion;

    @Null
    @Column(name = "fn", nullable = true)
    private Instant fn;

    @Column(name = "fecha_registro")
    private Instant fechaRegistro;

    @Lob
    @Column(name = "imagen")
    private byte[] imagen;

    @Column(name = "imagen_content_type")
    private String imagenContentType;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @ManyToMany
    @JoinTable(
        name = "rel_usuario__conversacion",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "conversacion_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "usuarios" }, allowSetters = true)
    private Set<Conversacion> conversacions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Usuario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Usuario nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public Usuario apellidos(String apellidos) {
        this.setApellidos(apellidos);
        return this;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return this.correo;
    }

    public Usuario correo(String correo) {
        this.setCorreo(correo);
        return this;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDni() {
        return this.dni;
    }

    public Usuario dni(String dni) {
        this.setDni(dni);
        return this;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Usuario direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLocalidad() {
        return this.localidad;
    }

    public Usuario localidad(String localidad) {
        this.setLocalidad(localidad);
        return this;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return this.provincia;
    }

    public Usuario provincia(String provincia) {
        this.setProvincia(provincia);
        return this;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getProfesion() {
        return this.profesion;
    }

    public Usuario profesion(String profesion) {
        this.setProfesion(profesion);
        return this;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public Instant getFn() {
        return this.fn;
    }

    public Usuario fn(Instant fn) {
        this.setFn(fn);
        return this;
    }

    public void setFn(Instant fn) {
        this.fn = fn;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Usuario fechaRegistro(Instant fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public byte[] getImagen() {
        return this.imagen;
    }

    public Usuario imagen(byte[] imagen) {
        this.setImagen(imagen);
        return this;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getImagenContentType() {
        return this.imagenContentType;
    }

    public Usuario imagenContentType(String imagenContentType) {
        this.imagenContentType = imagenContentType;
        return this;
    }

    public void setImagenContentType(String imagenContentType) {
        this.imagenContentType = imagenContentType;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Usuario user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Conversacion> getConversacions() {
        return this.conversacions;
    }

    public void setConversacions(Set<Conversacion> conversacions) {
        this.conversacions = conversacions;
    }

    public Usuario conversacions(Set<Conversacion> conversacions) {
        this.setConversacions(conversacions);
        return this;
    }

    public Usuario addConversacion(Conversacion conversacion) {
        this.conversacions.add(conversacion);
        conversacion.getUsuarios().add(this);
        return this;
    }

    public Usuario removeConversacion(Conversacion conversacion) {
        this.conversacions.remove(conversacion);
        conversacion.getUsuarios().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Usuario)) {
            return false;
        }
        return id != null && id.equals(((Usuario) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Usuario{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellidos='" + getApellidos() + "'" +
            ", correo='" + getCorreo() + "'" +
            ", dni='" + getDni() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", localidad='" + getLocalidad() + "'" +
            ", provincia='" + getProvincia() + "'" +
            ", profesion='" + getProfesion() + "'" +
            ", fn='" + getFn() + "'" +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            ", imagen='" + getImagen() + "'" +
            ", imagenContentType='" + getImagenContentType() + "'" +
            "}";
    }
}
