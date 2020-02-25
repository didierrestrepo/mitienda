package com.themakers.mitienda.service.mapper;

import com.themakers.mitienda.domain.*;
import com.themakers.mitienda.service.dto.ElementoListaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ElementoLista} and its DTO {@link ElementoListaDTO}.
 */
@Mapper(componentModel = "spring", uses = {TipoListaMapper.class})
public interface ElementoListaMapper extends EntityMapper<ElementoListaDTO, ElementoLista> {

    @Mapping(source = "tipoLista.id", target = "tipoListaId")
    @Mapping(source = "tipoLista.nombre", target = "tipoListaNombre")
    @Mapping(source = "padre.id", target = "padreId")
    @Mapping(source = "padre.nombre", target = "padreNombre")
    ElementoListaDTO toDto(ElementoLista elementoLista);

    @Mapping(source = "tipoListaId", target = "tipoLista")
    @Mapping(source = "padreId", target = "padre")
    ElementoLista toEntity(ElementoListaDTO elementoListaDTO);

    default ElementoLista fromId(Long id) {
        if (id == null) {
            return null;
        }
        ElementoLista elementoLista = new ElementoLista();
        elementoLista.setId(id);
        return elementoLista;
    }
}
