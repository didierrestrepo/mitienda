package com.themakers.mitienda.service.mapper;

import com.themakers.mitienda.domain.*;
import com.themakers.mitienda.service.dto.ProductoCategoriaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductoCategoria} and its DTO {@link ProductoCategoriaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProductoCategoriaMapper extends EntityMapper<ProductoCategoriaDTO, ProductoCategoria> {


    @Mapping(target = "productos", ignore = true)
    @Mapping(target = "removeProducto", ignore = true)
    ProductoCategoria toEntity(ProductoCategoriaDTO productoCategoriaDTO);

    default ProductoCategoria fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductoCategoria productoCategoria = new ProductoCategoria();
        productoCategoria.setId(id);
        return productoCategoria;
    }
}
