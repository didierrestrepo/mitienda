package com.themakers.mitienda.service.mapper;

import com.themakers.mitienda.domain.*;
import com.themakers.mitienda.service.dto.SucursalDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sucursal} and its DTO {@link SucursalDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SucursalMapper extends EntityMapper<SucursalDTO, Sucursal> {



    default Sucursal fromId(Long id) {
        if (id == null) {
            return null;
        }
        Sucursal sucursal = new Sucursal();
        sucursal.setId(id);
        return sucursal;
    }
}
