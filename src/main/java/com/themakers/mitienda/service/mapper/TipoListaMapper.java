package com.themakers.mitienda.service.mapper;

import com.themakers.mitienda.domain.*;
import com.themakers.mitienda.service.dto.TipoListaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoLista} and its DTO {@link TipoListaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TipoListaMapper extends EntityMapper<TipoListaDTO, TipoLista> {

    @Mapping(source = "padre.id", target = "padreId")
    @Mapping(source = "padre.nombre", target = "padreNombre")
    TipoListaDTO toDto(TipoLista tipoLista);

    @Mapping(source = "padreId", target = "padre")
    TipoLista toEntity(TipoListaDTO tipoListaDTO);

    default TipoLista fromId(Long id) {
        if (id == null) {
            return null;
        }
        TipoLista tipoLista = new TipoLista();
        tipoLista.setId(id);
        return tipoLista;
    }
}
