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
 * Criteria class for the {@link com.netjob.app.domain.Contrato} entity. This class is used
 * in {@link com.netjob.app.web.rest.ContratoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contratoes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContratoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FloatFilter preciofinal;

    private InstantFilter fecha;

    private LongFilter usuarioId;

    private LongFilter servicioId;

    private Boolean distinct;

    public ContratoCriteria() {}

    public ContratoCriteria(ContratoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.preciofinal = other.preciofinal == null ? null : other.preciofinal.copy();
        this.fecha = other.fecha == null ? null : other.fecha.copy();
        this.usuarioId = other.usuarioId == null ? null : other.usuarioId.copy();
        this.servicioId = other.servicioId == null ? null : other.servicioId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ContratoCriteria copy() {
        return new ContratoCriteria(this);
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

    public FloatFilter getPreciofinal() {
        return preciofinal;
    }

    public FloatFilter preciofinal() {
        if (preciofinal == null) {
            preciofinal = new FloatFilter();
        }
        return preciofinal;
    }

    public void setPreciofinal(FloatFilter preciofinal) {
        this.preciofinal = preciofinal;
    }

    public InstantFilter getFecha() {
        return fecha;
    }

    public InstantFilter fecha() {
        if (fecha == null) {
            fecha = new InstantFilter();
        }
        return fecha;
    }

    public void setFecha(InstantFilter fecha) {
        this.fecha = fecha;
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

    public LongFilter getServicioId() {
        return servicioId;
    }

    public LongFilter servicioId() {
        if (servicioId == null) {
            servicioId = new LongFilter();
        }
        return servicioId;
    }

    public void setServicioId(LongFilter servicioId) {
        this.servicioId = servicioId;
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
        final ContratoCriteria that = (ContratoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(preciofinal, that.preciofinal) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(servicioId, that.servicioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, preciofinal, fecha, usuarioId, servicioId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContratoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (preciofinal != null ? "preciofinal=" + preciofinal + ", " : "") +
            (fecha != null ? "fecha=" + fecha + ", " : "") +
            (usuarioId != null ? "usuarioId=" + usuarioId + ", " : "") +
            (servicioId != null ? "servicioId=" + servicioId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
