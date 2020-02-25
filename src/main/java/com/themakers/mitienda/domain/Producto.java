package com.themakers.mitienda.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.themakers.mitienda.domain.enumeration.EstadoProducto;

/**
 * Product sold by the Online store
 */
@Entity
@Table(name = "producto")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "precio_compra", precision = 21, scale = 2, nullable = false)
    private BigDecimal precioCompra;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "precio_venta", precision = 21, scale = 2, nullable = false)
    private BigDecimal precioVenta;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoProducto estado;

    @OneToMany(mappedBy = "producto")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProductoDetalle> productoDetalles = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("productos")
    private ProductoCategoria productCategoria;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Producto nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Producto descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public Producto precioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
        return this;
    }

    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public Producto precioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
        return this;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public EstadoProducto getEstado() {
        return estado;
    }

    public Producto estado(EstadoProducto estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(EstadoProducto estado) {
        this.estado = estado;
    }

    public Set<ProductoDetalle> getProductoDetalles() {
        return productoDetalles;
    }

    public Producto productoDetalles(Set<ProductoDetalle> productoDetalles) {
        this.productoDetalles = productoDetalles;
        return this;
    }

    public Producto addProductoDetalle(ProductoDetalle productoDetalle) {
        this.productoDetalles.add(productoDetalle);
        productoDetalle.setProducto(this);
        return this;
    }

    public Producto removeProductoDetalle(ProductoDetalle productoDetalle) {
        this.productoDetalles.remove(productoDetalle);
        productoDetalle.setProducto(null);
        return this;
    }

    public void setProductoDetalles(Set<ProductoDetalle> productoDetalles) {
        this.productoDetalles = productoDetalles;
    }

    public ProductoCategoria getProductCategoria() {
        return productCategoria;
    }

    public Producto productCategoria(ProductoCategoria productoCategoria) {
        this.productCategoria = productoCategoria;
        return this;
    }

    public void setProductCategoria(ProductoCategoria productoCategoria) {
        this.productCategoria = productoCategoria;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Producto)) {
            return false;
        }
        return id != null && id.equals(((Producto) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Producto{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", precioCompra=" + getPrecioCompra() +
            ", precioVenta=" + getPrecioVenta() +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
