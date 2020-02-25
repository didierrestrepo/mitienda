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
 * Criteria class for the {@link com.themakers.mitienda.domain.ProductoDetalle} entity. This class is used
 * in {@link com.themakers.mitienda.web.rest.ProductoDetalleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /producto-detalles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductoDetalleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter codigo;

    private LongFilter tallaId;

    private LongFilter colorId;

    private LongFilter productoId;

    public ProductoDetalleCriteria(){
    }

    public ProductoDetalleCriteria(ProductoDetalleCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.codigo = other.codigo == null ? null : other.codigo.copy();
        this.tallaId = other.tallaId == null ? null : other.tallaId.copy();
        this.colorId = other.colorId == null ? null : other.colorId.copy();
        this.productoId = other.productoId == null ? null : other.productoId.copy();
    }

    @Override
    public ProductoDetalleCriteria copy() {
        return new ProductoDetalleCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCodigo() {
        return codigo;
    }

    public void setCodigo(StringFilter codigo) {
        this.codigo = codigo;
    }

    public LongFilter getTallaId() {
        return tallaId;
    }

    public void setTallaId(LongFilter tallaId) {
        this.tallaId = tallaId;
    }

    public LongFilter getColorId() {
        return colorId;
    }

    public void setColorId(LongFilter colorId) {
        this.colorId = colorId;
    }

    public LongFilter getProductoId() {
        return productoId;
    }

    public void setProductoId(LongFilter productoId) {
        this.productoId = productoId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductoDetalleCriteria that = (ProductoDetalleCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(codigo, that.codigo) &&
            Objects.equals(tallaId, that.tallaId) &&
            Objects.equals(colorId, that.colorId) &&
            Objects.equals(productoId, that.productoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        codigo,
        tallaId,
        colorId,
        productoId
        );
    }

    @Override
    public String toString() {
        return "ProductoDetalleCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (codigo != null ? "codigo=" + codigo + ", " : "") +
                (tallaId != null ? "tallaId=" + tallaId + ", " : "") +
                (colorId != null ? "colorId=" + colorId + ", " : "") +
                (productoId != null ? "productoId=" + productoId + ", " : "") +
            "}";
    }

}
