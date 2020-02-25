package com.themakers.mitienda.service;

import com.themakers.mitienda.domain.ElementoLista;
import com.themakers.mitienda.repository.ElementoListaRepository;
import com.themakers.mitienda.service.dto.ElementoListaDTO;
import com.themakers.mitienda.service.mapper.ElementoListaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ElementoLista}.
 */
@Service
@Transactional
public class ElementoListaService {

    private final Logger log = LoggerFactory.getLogger(ElementoListaService.class);

    private final ElementoListaRepository elementoListaRepository;

    private final ElementoListaMapper elementoListaMapper;

    public ElementoListaService(ElementoListaRepository elementoListaRepository, ElementoListaMapper elementoListaMapper) {
        this.elementoListaRepository = elementoListaRepository;
        this.elementoListaMapper = elementoListaMapper;
    }

    /**
     * Save a elementoLista.
     *
     * @param elementoListaDTO the entity to save.
     * @return the persisted entity.
     */
    public ElementoListaDTO save(ElementoListaDTO elementoListaDTO) {
        log.debug("Request to save ElementoLista : {}", elementoListaDTO);
        ElementoLista elementoLista = elementoListaMapper.toEntity(elementoListaDTO);
        elementoLista = elementoListaRepository.save(elementoLista);
        return elementoListaMapper.toDto(elementoLista);
    }

    /**
     * Get all the elementoListas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ElementoListaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ElementoListas");
        return elementoListaRepository.findAll(pageable)
            .map(elementoListaMapper::toDto);
    }


    /**
     * Get one elementoLista by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ElementoListaDTO> findOne(Long id) {
        log.debug("Request to get ElementoLista : {}", id);
        return elementoListaRepository.findById(id)
            .map(elementoListaMapper::toDto);
    }

    /**
     * Delete the elementoLista by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ElementoLista : {}", id);
        elementoListaRepository.deleteById(id);
    }
}
