package com.themakers.mitienda.service.mapper;

import com.themakers.mitienda.domain.*;
import com.themakers.mitienda.service.dto.StockDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Stock} and its DTO {@link StockDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProductoMapper.class, SucursalMapper.class})
public interface StockMapper extends EntityMapper<StockDTO, Stock> {

    @Mapping(source = "producto.id", target = "productoId")
    @Mapping(source = "sucursal.id", target = "sucursalId")
    StockDTO toDto(Stock stock);

    @Mapping(source = "productoId", target = "producto")
    @Mapping(source = "sucursalId", target = "sucursal")
    Stock toEntity(StockDTO stockDTO);

    default Stock fromId(Long id) {
        if (id == null) {
            return null;
        }
        Stock stock = new Stock();
        stock.setId(id);
        return stock;
    }
}
