package com.themakers.mitienda.service;

import com.themakers.mitienda.domain.ProductoCategoria;
import com.themakers.mitienda.repository.ProductoCategoriaRepository;
import com.themakers.mitienda.service.dto.ProductoCategoriaDTO;
import com.themakers.mitienda.service.mapper.ProductoCategoriaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ProductoCategoria}.
 */
@Service
@Transactional
public class ProductoCategoriaService {

    private final Logger log = LoggerFactory.getLogger(ProductoCategoriaService.class);

    private final ProductoCategoriaRepository productoCategoriaRepository;

    private final ProductoCategoriaMapper productoCategoriaMapper;

    public ProductoCategoriaService(ProductoCategoriaRepository productoCategoriaRepository, ProductoCategoriaMapper productoCategoriaMapper) {
        this.productoCategoriaRepository = productoCategoriaRepository;
        this.productoCategoriaMapper = productoCategoriaMapper;
    }

    /**
     * Save a productoCategoria.
     *
     * @param productoCategoriaDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductoCategoriaDTO save(ProductoCategoriaDTO productoCategoriaDTO) {
        log.debug("Request to save ProductoCategoria : {}", productoCategoriaDTO);
        ProductoCategoria productoCategoria = productoCategoriaMapper.toEntity(productoCategoriaDTO);
        productoCategoria = productoCategoriaRepository.save(productoCategoria);
        return productoCategoriaMapper.toDto(productoCategoria);
    }

    /**
     * Get all the productoCategorias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductoCategoriaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductoCategorias");
        return productoCategoriaRepository.findAll(pageable)
            .map(productoCategoriaMapper::toDto);
    }


    /**
     * Get one productoCategoria by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductoCategoriaDTO> findOne(Long id) {
        log.debug("Request to get ProductoCategoria : {}", id);
        return productoCategoriaRepository.findById(id)
            .map(productoCategoriaMapper::toDto);
    }

    /**
     * Delete the productoCategoria by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductoCategoria : {}", id);
        productoCategoriaRepository.deleteById(id);
    }
}
