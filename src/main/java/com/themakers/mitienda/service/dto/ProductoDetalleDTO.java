package com.themakers.mitienda.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.themakers.mitienda.domain.ProductoDetalle} entity.
 */
public class ProductoDetalleDTO implements Serializable {

    private Long id;

    private String codigo;


    private Long tallaId;

    private String tallaNombre;

    private Long colorId;

    private String colorNombre;

    private Long productoId;

    private String productoNombre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Long getTallaId() {
        return tallaId;
    }

    public void setTallaId(Long elementoListaId) {
        this.tallaId = elementoListaId;
    }

    public String getTallaNombre() {
        return tallaNombre;
    }

    public void setTallaNombre(String elementoListaNombre) {
        this.tallaNombre = elementoListaNombre;
    }

    public Long getColorId() {
        return colorId;
    }

    public void setColorId(Long elementoListaId) {
        this.colorId = elementoListaId;
    }

    public String getColorNombre() {
        return colorNombre;
    }

    public void setColorNombre(String elementoListaNombre) {
        this.colorNombre = elementoListaNombre;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductoDetalleDTO productoDetalleDTO = (ProductoDetalleDTO) o;
        if (productoDetalleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productoDetalleDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductoDetalleDTO{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", tallaId=" + getTallaId() +
            ", tallaNombre='" + getTallaNombre() + "'" +
            ", colorId=" + getColorId() +
            ", colorNombre='" + getColorNombre() + "'" +
            ", productoId=" + getProductoId() +
            ", productoNombre='" + getProductoNombre() + "'" +
            "}";
    }
}
