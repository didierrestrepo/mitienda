package com.themakers.mitienda.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.themakers.mitienda.domain.ElementoLista} entity. This class is used
 * in {@link com.themakers.mitienda.web.rest.ElementoListaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /elemento-listas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ElementoListaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter codigo;

    private LongFilter tipoListaId;

    private LongFilter padreId;

    public ElementoListaCriteria(){
    }

    public ElementoListaCriteria(ElementoListaCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.codigo = other.codigo == null ? null : other.codigo.copy();
        this.tipoListaId = other.tipoListaId == null ? null : other.tipoListaId.copy();
        this.padreId = other.padreId == null ? null : other.padreId.copy();
    }

    @Override
    public ElementoListaCriteria copy() {
        return new ElementoListaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNombre() {
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getCodigo() {
        return codigo;
    }

    public void setCodigo(StringFilter codigo) {
        this.codigo = codigo;
    }

    public LongFilter getTipoListaId() {
        return tipoListaId;
    }

    public void setTipoListaId(LongFilter tipoListaId) {
        this.tipoListaId = tipoListaId;
    }

    public LongFilter getPadreId() {
        return padreId;
    }

    public void setPadreId(LongFilter padreId) {
        this.padreId = padreId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ElementoListaCriteria that = (ElementoListaCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(codigo, that.codigo) &&
            Objects.equals(tipoListaId, that.tipoListaId) &&
            Objects.equals(padreId, that.padreId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nombre,
        codigo,
        tipoListaId,
        padreId
        );
    }

    @Override
    public String toString() {
        return "ElementoListaCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nombre != null ? "nombre=" + nombre + ", " : "") +
                (codigo != null ? "codigo=" + codigo + ", " : "") +
                (tipoListaId != null ? "tipoListaId=" + tipoListaId + ", " : "") +
                (padreId != null ? "padreId=" + padreId + ", " : "") +
            "}";
    }

}
