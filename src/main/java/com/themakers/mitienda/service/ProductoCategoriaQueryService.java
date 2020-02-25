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

import com.themakers.mitienda.domain.ProductoCategoria;
import com.themakers.mitienda.domain.*; // for static metamodels
import com.themakers.mitienda.repository.ProductoCategoriaRepository;
import com.themakers.mitienda.service.dto.ProductoCategoriaCriteria;
import com.themakers.mitienda.service.dto.ProductoCategoriaDTO;
import com.themakers.mitienda.service.mapper.ProductoCategoriaMapper;

/**
 * Service for executing complex queries for {@link ProductoCategoria} entities in the database.
 * The main input is a {@link ProductoCategoriaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductoCategoriaDTO} or a {@link Page} of {@link ProductoCategoriaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductoCategoriaQueryService extends QueryService<ProductoCategoria> {

    private final Logger log = LoggerFactory.getLogger(ProductoCategoriaQueryService.class);

    private final ProductoCategoriaRepository productoCategoriaRepository;

    private final ProductoCategoriaMapper productoCategoriaMapper;

    public ProductoCategoriaQueryService(ProductoCategoriaRepository productoCategoriaRepository, ProductoCategoriaMapper productoCategoriaMapper) {
        this.productoCategoriaRepository = productoCategoriaRepository;
        this.productoCategoriaMapper = productoCategoriaMapper;
    }

    /**
     * Return a {@link List} of {@link ProductoCategoriaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductoCategoriaDTO> findByCriteria(ProductoCategoriaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductoCategoria> specification = createSpecification(criteria);
        return productoCategoriaMapper.toDto(productoCategoriaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductoCategoriaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductoCategoriaDTO> findByCriteria(ProductoCategoriaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductoCategoria> specification = createSpecification(criteria);
        return productoCategoriaRepository.findAll(specification, page)
            .map(productoCategoriaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductoCategoriaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductoCategoria> specification = createSpecification(criteria);
        return productoCategoriaRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductoCategoriaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductoCategoria> createSpecification(ProductoCategoriaCriteria criteria) {
        Specification<ProductoCategoria> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductoCategoria_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), ProductoCategoria_.nombre));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), ProductoCategoria_.descripcion));
            }
            if (criteria.getProductoId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductoId(),
                    root -> root.join(ProductoCategoria_.productos, JoinType.LEFT).get(Producto_.id)));
            }
        }
        return specification;
    }
}
