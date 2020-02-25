package com.themakers.mitienda.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.themakers.mitienda.domain.enumeration.EstadoProducto;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;

/**
 * Criteria class for the {@link com.themakers.mitienda.domain.Producto} entity. This class is used
 * in {@link com.themakers.mitienda.web.rest.ProductoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /productos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductoCriteria implements Serializable, Criteria {
    /**
     * Class for filtering EstadoProducto
     */
    public static class EstadoProductoFilter extends Filter<EstadoProducto> {

        public EstadoProductoFilter() {
        }

        public EstadoProductoFilter(EstadoProductoFilter filter) {
            super(filter);
        }

        @Override
        public EstadoProductoFilter copy() {
            return new EstadoProductoFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter descripcion;

    private BigDecimalFilter precioCompra;

    private BigDecimalFilter precioVenta;

    private EstadoProductoFilter estado;

    private LongFilter productoDetalleId;

    private LongFilter productCategoriaId;

    public ProductoCriteria(){
    }

    public ProductoCriteria(ProductoCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.descripcion = other.descripcion == null ? null : other.descripcion.copy();
        this.precioCompra = other.precioCompra == null ? null : other.precioCompra.copy();
        this.precioVenta = other.precioVenta == null ? null : other.precioVenta.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.productoDetalleId = other.productoDetalleId == null ? null : other.productoDetalleId.copy();
        this.productCategoriaId = other.productCategoriaId == null ? null : other.productCategoriaId.copy();
    }

    @Override
    public ProductoCriteria copy() {
        return new ProductoCriteria(this);
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

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimalFilter getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(BigDecimalFilter precioCompra) {
        this.precioCompra = precioCompra;
    }

    public BigDecimalFilter getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimalFilter precioVenta) {
        this.precioVenta = precioVenta;
    }

    public EstadoProductoFilter getEstado() {
        return estado;
    }

    public void setEstado(EstadoProductoFilter estado) {
        this.estado = estado;
    }

    public LongFilter getProductoDetalleId() {
        return productoDetalleId;
    }

    public void setProductoDetalleId(LongFilter productoDetalleId) {
        this.productoDetalleId = productoDetalleId;
    }

    public LongFilter getProductCategoriaId() {
        return productCategoriaId;
    }

    public void setProductCategoriaId(LongFilter productCategoriaId) {
        this.productCategoriaId = productCategoriaId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductoCriteria that = (ProductoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(precioCompra, that.precioCompra) &&
            Objects.equals(precioVenta, that.precioVenta) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(productoDetalleId, that.productoDetalleId) &&
            Objects.equals(productCategoriaId, that.productCategoriaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nombre,
        descripcion,
        precioCompra,
        precioVenta,
        estado,
        productoDetalleId,
        productCategoriaId
        );
    }

    @Override
    public String toString() {
        return "ProductoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nombre != null ? "nombre=" + nombre + ", " : "") +
                (descripcion != null ? "descripcion=" + descripcion + ", " : "") +
                (precioCompra != null ? "precioCompra=" + precioCompra + ", " : "") +
                (precioVenta != null ? "precioVenta=" + precioVenta + ", " : "") +
                (estado != null ? "estado=" + estado + ", " : "") +
                (productoDetalleId != null ? "productoDetalleId=" + productoDetalleId + ", " : "") +
                (productCategoriaId != null ? "productCategoriaId=" + productCategoriaId + ", " : "") +
            "}";
    }

}
