package com.themakers.mitienda.service;

import com.themakers.mitienda.domain.ProductoDetalle;
import com.themakers.mitienda.repository.ProductoDetalleRepository;
import com.themakers.mitienda.service.dto.ProductoDetalleDTO;
import com.themakers.mitienda.service.mapper.ProductoDetalleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ProductoDetalle}.
 */
@Service
@Transactional
public class ProductoDetalleService {

    private final Logger log = LoggerFactory.getLogger(ProductoDetalleService.class);

    private final ProductoDetalleRepository productoDetalleRepository;

    private final ProductoDetalleMapper productoDetalleMapper;

    public ProductoDetalleService(ProductoDetalleRepository productoDetalleRepository, ProductoDetalleMapper productoDetalleMapper) {
        this.productoDetalleRepository = productoDetalleRepository;
        this.productoDetalleMapper = productoDetalleMapper;
    }

    /**
     * Save a productoDetalle.
     *
     * @param productoDetalleDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductoDetalleDTO save(ProductoDetalleDTO productoDetalleDTO) {
        log.debug("Request to save ProductoDetalle : {}", productoDetalleDTO);
        ProductoDetalle productoDetalle = productoDetalleMapper.toEntity(productoDetalleDTO);
        productoDetalle = productoDetalleRepository.save(productoDetalle);
        return productoDetalleMapper.toDto(productoDetalle);
    }

    /**
     * Get all the productoDetalles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductoDetalleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductoDetalles");
        return productoDetalleRepository.findAll(pageable)
            .map(productoDetalleMapper::toDto);
    }


    /**
     * Get one productoDetalle by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductoDetalleDTO> findOne(Long id) {
        log.debug("Request to get ProductoDetalle : {}", id);
        return productoDetalleRepository.findById(id)
            .map(productoDetalleMapper::toDto);
    }

    /**
     * Delete the productoDetalle by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductoDetalle : {}", id);
        productoDetalleRepository.deleteById(id);
    }
}
