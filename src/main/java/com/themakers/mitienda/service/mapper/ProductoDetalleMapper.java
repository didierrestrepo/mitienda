package com.themakers.mitienda.service.mapper;

import com.themakers.mitienda.domain.*;
import com.themakers.mitienda.service.dto.ProductoDetalleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductoDetalle} and its DTO {@link ProductoDetalleDTO}.
 */
@Mapper(componentModel = "spring", uses = {ElementoListaMapper.class, ProductoMapper.class})
public interface ProductoDetalleMapper extends EntityMapper<ProductoDetalleDTO, ProductoDetalle> {

    @Mapping(source = "talla.id", target = "tallaId")
    @Mapping(source = "talla.nombre", target = "tallaNombre")
    @Mapping(source = "color.id", target = "colorId")
    @Mapping(source = "color.nombre", target = "colorNombre")
    @Mapping(source = "producto.id", target = "productoId")
    @Mapping(source = "producto.nombre", target = "productoNombre")
    ProductoDetalleDTO toDto(ProductoDetalle productoDetalle);

    @Mapping(source = "tallaId", target = "talla")
    @Mapping(source = "colorId", target = "color")
    @Mapping(source = "productoId", target = "producto")
    ProductoDetalle toEntity(ProductoDetalleDTO productoDetalleDTO);

    default ProductoDetalle fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductoDetalle productoDetalle = new ProductoDetalle();
        productoDetalle.setId(id);
        return productoDetalle;
    }
}
