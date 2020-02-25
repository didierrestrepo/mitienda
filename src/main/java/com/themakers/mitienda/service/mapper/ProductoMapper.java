package com.themakers.mitienda.service.mapper;

import com.themakers.mitienda.domain.*;
import com.themakers.mitienda.service.dto.ProductoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Producto} and its DTO {@link ProductoDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProductoCategoriaMapper.class})
public interface ProductoMapper extends EntityMapper<ProductoDTO, Producto> {

    @Mapping(source = "productCategoria.id", target = "productCategoriaId")
    ProductoDTO toDto(Producto producto);

    @Mapping(target = "productoDetalles", ignore = true)
    @Mapping(target = "removeProductoDetalle", ignore = true)
    @Mapping(source = "productCategoriaId", target = "productCategoria")
    Producto toEntity(ProductoDTO productoDTO);

    default Producto fromId(Long id) {
        if (id == null) {
            return null;
        }
        Producto producto = new Producto();
        producto.setId(id);
        return producto;
    }
}
