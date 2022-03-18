package com.netjob.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.netjob.app.domain.Usuario} entity. This class is used
 * in {@link com.netjob.app.web.rest.UsuarioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /usuarios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UsuarioCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter apellidos;

    private StringFilter correo;

    private StringFilter dni;

    private StringFilter direccion;

    private StringFilter localidad;

    private StringFilter provincia;

    private StringFilter profesion;

    private InstantFilter fn;

    private InstantFilter fechaRegistro;

    private LongFilter userId;

    private Boolean distinct;

    public UsuarioCriteria() {}

    public UsuarioCriteria(UsuarioCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.apellidos = other.apellidos == null ? null : other.apellidos.copy();
        this.correo = other.correo == null ? null : other.correo.copy();
        this.dni = other.dni == null ? null : other.dni.copy();
        this.direccion = other.direccion == null ? null : other.direccion.copy();
        this.localidad = other.localidad == null ? null : other.localidad.copy();
        this.provincia = other.provincia == null ? null : other.provincia.copy();
        this.profesion = other.profesion == null ? null : other.profesion.copy();
        this.fn = other.fn == null ? null : other.fn.copy();
        this.fechaRegistro = other.fechaRegistro == null ? null : other.fechaRegistro.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UsuarioCriteria copy() {
        return new UsuarioCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNombre() {
        return nombre;
    }

    public StringFilter nombre() {
        if (nombre == null) {
            nombre = new StringFilter();
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getApellidos() {
        return apellidos;
    }

    public StringFilter apellidos() {
        if (apellidos == null) {
            apellidos = new StringFilter();
        }
        return apellidos;
    }

    public void setApellidos(StringFilter apellidos) {
        this.apellidos = apellidos;
    }

    public StringFilter getCorreo() {
        return correo;
    }

    public StringFilter correo() {
        if (correo == null) {
            correo = new StringFilter();
        }
        return correo;
    }

    public void setCorreo(StringFilter correo) {
        this.correo = correo;
    }

    public StringFilter getDni() {
        return dni;
    }

    public StringFilter dni() {
        if (dni == null) {
            dni = new StringFilter();
        }
        return dni;
    }

    public void setDni(StringFilter dni) {
        this.dni = dni;
    }

    public StringFilter getDireccion() {
        return direccion;
    }

    public StringFilter direccion() {
        if (direccion == null) {
            direccion = new StringFilter();
        }
        return direccion;
    }

    public void setDireccion(StringFilter direccion) {
        this.direccion = direccion;
    }

    public StringFilter getLocalidad() {
        return localidad;
    }

    public StringFilter localidad() {
        if (localidad == null) {
            localidad = new StringFilter();
        }
        return localidad;
    }

    public void setLocalidad(StringFilter localidad) {
        this.localidad = localidad;
    }

    public StringFilter getProvincia() {
        return provincia;
    }

    public StringFilter provincia() {
        if (provincia == null) {
            provincia = new StringFilter();
        }
        return provincia;
    }

    public void setProvincia(StringFilter provincia) {
        this.provincia = provincia;
    }

    public StringFilter getProfesion() {
        return profesion;
    }

    public StringFilter profesion() {
        if (profesion == null) {
            profesion = new StringFilter();
        }
        return profesion;
    }

    public void setProfesion(StringFilter profesion) {
        this.profesion = profesion;
    }

    public InstantFilter getFn() {
        return fn;
    }

    public InstantFilter fn() {
        if (fn == null) {
            fn = new InstantFilter();
        }
        return fn;
    }

    public void setFn(InstantFilter fn) {
        this.fn = fn;
    }

    public InstantFilter getFechaRegistro() {
        return fechaRegistro;
    }

    public InstantFilter fechaRegistro() {
        if (fechaRegistro == null) {
            fechaRegistro = new InstantFilter();
        }
        return fechaRegistro;
    }

    public void setFechaRegistro(InstantFilter fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UsuarioCriteria that = (UsuarioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(apellidos, that.apellidos) &&
            Objects.equals(correo, that.correo) &&
            Objects.equals(dni, that.dni) &&
            Objects.equals(direccion, that.direccion) &&
            Objects.equals(localidad, that.localidad) &&
            Objects.equals(provincia, that.provincia) &&
            Objects.equals(profesion, that.profesion) &&
            Objects.equals(fn, that.fn) &&
            Objects.equals(fechaRegistro, that.fechaRegistro) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nombre,
            apellidos,
            correo,
            dni,
            direccion,
            localidad,
            provincia,
            profesion,
            fn,
            fechaRegistro,
            userId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (apellidos != null ? "apellidos=" + apellidos + ", " : "") +
            (correo != null ? "correo=" + correo + ", " : "") +
            (dni != null ? "dni=" + dni + ", " : "") +
            (direccion != null ? "direccion=" + direccion + ", " : "") +
            (localidad != null ? "localidad=" + localidad + ", " : "") +
            (provincia != null ? "provincia=" + provincia + ", " : "") +
            (profesion != null ? "profesion=" + profesion + ", " : "") +
            (fn != null ? "fn=" + fn + ", " : "") +
            (fechaRegistro != null ? "fechaRegistro=" + fechaRegistro + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
