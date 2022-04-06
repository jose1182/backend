package com.netjob.app.service.criteria;

import com.netjob.app.domain.enumeration.Disponibilidad;
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
 * Criteria class for the {@link com.netjob.app.domain.Servicio} entity. This class is used
 * in {@link com.netjob.app.web.rest.ServicioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /servicios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ServicioCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Disponibilidad
     */
    public static class DisponibilidadFilter extends Filter<Disponibilidad> {

        public DisponibilidadFilter() {}

        public DisponibilidadFilter(DisponibilidadFilter filter) {
            super(filter);
        }

        @Override
        public DisponibilidadFilter copy() {
            return new DisponibilidadFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter titulo;

    private StringFilter descripcion;

    private DisponibilidadFilter disponibilidad;

    private FloatFilter preciohora;

    private FloatFilter preciotraslado;

    private InstantFilter fechacreacion;

    private InstantFilter fechaactualizacion;

    private LongFilter usuarioId;

    private LongFilter categoriaId;

    private Boolean distinct;

    public ServicioCriteria() {}

    public ServicioCriteria(ServicioCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.titulo = other.titulo == null ? null : other.titulo.copy();
        this.descripcion = other.descripcion == null ? null : other.descripcion.copy();
        this.disponibilidad = other.disponibilidad == null ? null : other.disponibilidad.copy();
        this.preciohora = other.preciohora == null ? null : other.preciohora.copy();
        this.preciotraslado = other.preciotraslado == null ? null : other.preciotraslado.copy();
        this.fechacreacion = other.fechacreacion == null ? null : other.fechacreacion.copy();
        this.fechaactualizacion = other.fechaactualizacion == null ? null : other.fechaactualizacion.copy();
        this.usuarioId = other.usuarioId == null ? null : other.usuarioId.copy();
        this.categoriaId = other.categoriaId == null ? null : other.categoriaId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ServicioCriteria copy() {
        return new ServicioCriteria(this);
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

    public StringFilter getTitulo() {
        return titulo;
    }

    public StringFilter titulo() {
        if (titulo == null) {
            titulo = new StringFilter();
        }
        return titulo;
    }

    public void setTitulo(StringFilter titulo) {
        this.titulo = titulo;
    }

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public StringFilter descripcion() {
        if (descripcion == null) {
            descripcion = new StringFilter();
        }
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public DisponibilidadFilter getDisponibilidad() {
        return disponibilidad;
    }

    public DisponibilidadFilter disponibilidad() {
        if (disponibilidad == null) {
            disponibilidad = new DisponibilidadFilter();
        }
        return disponibilidad;
    }

    public void setDisponibilidad(DisponibilidadFilter disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public FloatFilter getPreciohora() {
        return preciohora;
    }

    public FloatFilter preciohora() {
        if (preciohora == null) {
            preciohora = new FloatFilter();
        }
        return preciohora;
    }

    public void setPreciohora(FloatFilter preciohora) {
        this.preciohora = preciohora;
    }

    public FloatFilter getPreciotraslado() {
        return preciotraslado;
    }

    public FloatFilter preciotraslado() {
        if (preciotraslado == null) {
            preciotraslado = new FloatFilter();
        }
        return preciotraslado;
    }

    public void setPreciotraslado(FloatFilter preciotraslado) {
        this.preciotraslado = preciotraslado;
    }

    public InstantFilter getFechacreacion() {
        return fechacreacion;
    }

    public InstantFilter fechacreacion() {
        if (fechacreacion == null) {
            fechacreacion = new InstantFilter();
        }
        return fechacreacion;
    }

    public void setFechacreacion(InstantFilter fechacreacion) {
        this.fechacreacion = fechacreacion;
    }

    public InstantFilter getFechaactualizacion() {
        return fechaactualizacion;
    }

    public InstantFilter fechaactualizacion() {
        if (fechaactualizacion == null) {
            fechaactualizacion = new InstantFilter();
        }
        return fechaactualizacion;
    }

    public void setFechaactualizacion(InstantFilter fechaactualizacion) {
        this.fechaactualizacion = fechaactualizacion;
    }

    public LongFilter getUsuarioId() {
        return usuarioId;
    }

    public LongFilter usuarioId() {
        if (usuarioId == null) {
            usuarioId = new LongFilter();
        }
        return usuarioId;
    }

    public void setUsuarioId(LongFilter usuarioId) {
        this.usuarioId = usuarioId;
    }

    public LongFilter getCategoriaId() {
        return categoriaId;
    }

    public LongFilter categoriaId() {
        if (categoriaId == null) {
            categoriaId = new LongFilter();
        }
        return categoriaId;
    }

    public void setCategoriaId(LongFilter categoriaId) {
        this.categoriaId = categoriaId;
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
        final ServicioCriteria that = (ServicioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(titulo, that.titulo) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(disponibilidad, that.disponibilidad) &&
            Objects.equals(preciohora, that.preciohora) &&
            Objects.equals(preciotraslado, that.preciotraslado) &&
            Objects.equals(fechacreacion, that.fechacreacion) &&
            Objects.equals(fechaactualizacion, that.fechaactualizacion) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(categoriaId, that.categoriaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            titulo,
            descripcion,
            disponibilidad,
            preciohora,
            preciotraslado,
            fechacreacion,
            fechaactualizacion,
            usuarioId,
            categoriaId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServicioCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (titulo != null ? "titulo=" + titulo + ", " : "") +
            (descripcion != null ? "descripcion=" + descripcion + ", " : "") +
            (disponibilidad != null ? "disponibilidad=" + disponibilidad + ", " : "") +
            (preciohora != null ? "preciohora=" + preciohora + ", " : "") +
            (preciotraslado != null ? "preciotraslado=" + preciotraslado + ", " : "") +
            (fechacreacion != null ? "fechacreacion=" + fechacreacion + ", " : "") +
            (fechaactualizacion != null ? "fechaactualizacion=" + fechaactualizacion + ", " : "") +
            (usuarioId != null ? "usuarioId=" + usuarioId + ", " : "") +
            (categoriaId != null ? "categoriaId=" + categoriaId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
