package com.themakers.mitienda.service;

import com.themakers.mitienda.domain.TipoLista;
import com.themakers.mitienda.repository.TipoListaRepository;
import com.themakers.mitienda.service.dto.TipoListaDTO;
import com.themakers.mitienda.service.mapper.TipoListaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TipoLista}.
 */
@Service
@Transactional
public class TipoListaService {

    private final Logger log = LoggerFactory.getLogger(TipoListaService.class);

    private final TipoListaRepository tipoListaRepository;

    private final TipoListaMapper tipoListaMapper;

    public TipoListaService(TipoListaRepository tipoListaRepository, TipoListaMapper tipoListaMapper) {
        this.tipoListaRepository = tipoListaRepository;
        this.tipoListaMapper = tipoListaMapper;
    }

    /**
     * Save a tipoLista.
     *
     * @param tipoListaDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoListaDTO save(TipoListaDTO tipoListaDTO) {
        log.debug("Request to save TipoLista : {}", tipoListaDTO);
        TipoLista tipoLista = tipoListaMapper.toEntity(tipoListaDTO);
        tipoLista = tipoListaRepository.save(tipoLista);
        return tipoListaMapper.toDto(tipoLista);
    }

    /**
     * Get all the tipoListas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TipoListaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TipoListas");
        return tipoListaRepository.findAll(pageable)
            .map(tipoListaMapper::toDto);
    }


    /**
     * Get one tipoLista by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TipoListaDTO> findOne(Long id) {
        log.debug("Request to get TipoLista : {}", id);
        return tipoListaRepository.findById(id)
            .map(tipoListaMapper::toDto);
    }

    /**
     * Delete the tipoLista by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TipoLista : {}", id);
        tipoListaRepository.deleteById(id);
    }
}
