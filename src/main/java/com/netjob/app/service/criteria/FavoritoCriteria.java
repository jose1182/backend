package com.netjob.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.netjob.app.domain.Favorito} entity. This class is used
 * in {@link com.netjob.app.web.rest.FavoritoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /favoritos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FavoritoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter usuarioId;

    private LongFilter servicioId;

    private Boolean distinct;

    public FavoritoCriteria() {}

    public FavoritoCriteria(FavoritoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.usuarioId = other.usuarioId == null ? null : other.usuarioId.copy();
        this.servicioId = other.servicioId == null ? null : other.servicioId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FavoritoCriteria copy() {
        return new FavoritoCriteria(this);
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
        final FavoritoCriteria that = (FavoritoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(servicioId, that.servicioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, usuarioId, servicioId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FavoritoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (usuarioId != null ? "usuarioId=" + usuarioId + ", " : "") +
            (servicioId != null ? "servicioId=" + servicioId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
