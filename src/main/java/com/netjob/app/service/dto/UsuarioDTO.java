package com.netjob.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.netjob.app.domain.Usuario} entity.
 */
public class UsuarioDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 40)
    private String nombre;

    @Size(max = 50)
    private String apellidos;

    @NotNull
    @Size(max = 20)
    private String correo;

    @NotNull
    @Pattern(regexp = "[0-9]{8}[TRWAGMYFPDXBNJZSQVHLCKE]")
    private String dni;

    @NotNull
    private String direccion;

    @NotNull
    private String localidad;

    @NotNull
    private String provincia;

    private String profesion;

    @NotNull
    private Instant fn;

    private Instant fechaRegistro;

    private UserDTO user;

    private Set<ConversacionDTO> conversacions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public Instant getFn() {
        return fn;
    }

    public void setFn(Instant fn) {
        this.fn = fn;
    }

    public Instant getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Set<ConversacionDTO> getConversacions() {
        return conversacions;
    }

    public void setConversacions(Set<ConversacionDTO> conversacions) {
        this.conversacions = conversacions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UsuarioDTO)) {
            return false;
        }

        UsuarioDTO usuarioDTO = (UsuarioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, usuarioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioDTO{" +
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
            ", user=" + getUser() +
            ", conversacions=" + getConversacions() +
            
            "}";
    }
}
