package com.themakers.mitienda.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A ProductoDetalle.
 */
@Entity
@Table(name = "producto_detalle")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProductoDetalle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "codigo")
    private String codigo;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("productoDetalles")
    private ElementoLista talla;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("productoDetalles")
    private ElementoLista color;

    @ManyToOne
    @JsonIgnoreProperties("productoDetalles")
    private Producto producto;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public ProductoDetalle codigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public ElementoLista getTalla() {
        return talla;
    }

    public ProductoDetalle talla(ElementoLista elementoLista) {
        this.talla = elementoLista;
        return this;
    }

    public void setTalla(ElementoLista elementoLista) {
        this.talla = elementoLista;
    }

    public ElementoLista getColor() {
        return color;
    }

    public ProductoDetalle color(ElementoLista elementoLista) {
        this.color = elementoLista;
        return this;
    }

    public void setColor(ElementoLista elementoLista) {
        this.color = elementoLista;
    }

    public Producto getProducto() {
        return producto;
    }

    public ProductoDetalle producto(Producto producto) {
        this.producto = producto;
        return this;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductoDetalle)) {
            return false;
        }
        return id != null && id.equals(((ProductoDetalle) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ProductoDetalle{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            "}";
    }
}
