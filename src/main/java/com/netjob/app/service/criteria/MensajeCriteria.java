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
 * Criteria class for the {@link com.netjob.app.domain.Mensaje} entity. This class is used
 * in {@link com.netjob.app.web.rest.MensajeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /mensajes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MensajeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter texto;

    private InstantFilter fecha;

    private LongFilter emisorId;

    private LongFilter receptorId;

    private LongFilter conversacionId;

    private Boolean distinct;

    public MensajeCriteria() {}

    public MensajeCriteria(MensajeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.texto = other.texto == null ? null : other.texto.copy();
        this.fecha = other.fecha == null ? null : other.fecha.copy();
        this.emisorId = other.emisorId == null ? null : other.emisorId.copy();
        this.receptorId = other.receptorId == null ? null : other.receptorId.copy();
        this.conversacionId = other.conversacionId == null ? null : other.conversacionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MensajeCriteria copy() {
        return new MensajeCriteria(this);
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

    public StringFilter getTexto() {
        return texto;
    }

    public StringFilter texto() {
        if (texto == null) {
            texto = new StringFilter();
        }
        return texto;
    }

    public void setTexto(StringFilter texto) {
        this.texto = texto;
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

    public LongFilter getEmisorId() {
        return emisorId;
    }

    public LongFilter emisorId() {
        if (emisorId == null) {
            emisorId = new LongFilter();
        }
        return emisorId;
    }

    public void setEmisorId(LongFilter emisorId) {
        this.emisorId = emisorId;
    }

    public LongFilter getReceptorId() {
        return receptorId;
    }

    public LongFilter receptorId() {
        if (receptorId == null) {
            receptorId = new LongFilter();
        }
        return receptorId;
    }

    public void setReceptorId(LongFilter receptorId) {
        this.receptorId = receptorId;
    }

    public LongFilter getConversacionId() {
        return conversacionId;
    }

    public LongFilter conversacionId() {
        if (conversacionId == null) {
            conversacionId = new LongFilter();
        }
        return conversacionId;
    }

    public void setConversacionId(LongFilter conversacionId) {
        this.conversacionId = conversacionId;
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
        final MensajeCriteria that = (MensajeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(texto, that.texto) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(emisorId, that.emisorId) &&
            Objects.equals(receptorId, that.receptorId) &&
            Objects.equals(conversacionId, that.conversacionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, texto, fecha, emisorId, receptorId, conversacionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MensajeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (texto != null ? "texto=" + texto + ", " : "") +
            (fecha != null ? "fecha=" + fecha + ", " : "") +
            (emisorId != null ? "emisorId=" + emisorId + ", " : "") +
            (receptorId != null ? "receptorId=" + receptorId + ", " : "") +
            (conversacionId != null ? "conversacionId=" + conversacionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
