package com.themakers.mitienda.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.themakers.mitienda.domain.ProductoDetalle;
import com.themakers.mitienda.domain.*; // for static metamodels
import com.themakers.mitienda.repository.ProductoDetalleRepository;
import com.themakers.mitienda.service.dto.ProductoDetalleCriteria;
import com.themakers.mitienda.service.dto.ProductoDetalleDTO;
import com.themakers.mitienda.service.mapper.ProductoDetalleMapper;

/**
 * Service for executing complex queries for {@link ProductoDetalle} entities in the database.
 * The main input is a {@link ProductoDetalleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductoDetalleDTO} or a {@link Page} of {@link ProductoDetalleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductoDetalleQueryService extends QueryService<ProductoDetalle> {

    private final Logger log = LoggerFactory.getLogger(ProductoDetalleQueryService.class);

    private final ProductoDetalleRepository productoDetalleRepository;

    private final ProductoDetalleMapper productoDetalleMapper;

    public ProductoDetalleQueryService(ProductoDetalleRepository productoDetalleRepository, ProductoDetalleMapper productoDetalleMapper) {
        this.productoDetalleRepository = productoDetalleRepository;
        this.productoDetalleMapper = productoDetalleMapper;
    }

    /**
     * Return a {@link List} of {@link ProductoDetalleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductoDetalleDTO> findByCriteria(ProductoDetalleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductoDetalle> specification = createSpecification(criteria);
        return productoDetalleMapper.toDto(productoDetalleRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductoDetalleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductoDetalleDTO> findByCriteria(ProductoDetalleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductoDetalle> specification = createSpecification(criteria);
        return productoDetalleRepository.findAll(specification, page)
            .map(productoDetalleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductoDetalleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductoDetalle> specification = createSpecification(criteria);
        return productoDetalleRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductoDetalleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductoDetalle> createSpecification(ProductoDetalleCriteria criteria) {
        Specification<ProductoDetalle> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductoDetalle_.id));
            }
            if (criteria.getCodigo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodigo(), ProductoDetalle_.codigo));
            }
            if (criteria.getTallaId() != null) {
                specification = specification.and(buildSpecification(criteria.getTallaId(),
                    root -> root.join(ProductoDetalle_.talla, JoinType.LEFT).get(ElementoLista_.id)));
            }
            if (criteria.getColorId() != null) {
                specification = specification.and(buildSpecification(criteria.getColorId(),
                    root -> root.join(ProductoDetalle_.color, JoinType.LEFT).get(ElementoLista_.id)));
            }
            if (criteria.getProductoId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductoId(),
                    root -> root.join(ProductoDetalle_.producto, JoinType.LEFT).get(Producto_.id)));
            }
        }
        return specification;
    }
}
