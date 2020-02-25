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

import com.themakers.mitienda.domain.Sucursal;
import com.themakers.mitienda.domain.*; // for static metamodels
import com.themakers.mitienda.repository.SucursalRepository;
import com.themakers.mitienda.service.dto.SucursalCriteria;
import com.themakers.mitienda.service.dto.SucursalDTO;
import com.themakers.mitienda.service.mapper.SucursalMapper;

/**
 * Service for executing complex queries for {@link Sucursal} entities in the database.
 * The main input is a {@link SucursalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SucursalDTO} or a {@link Page} of {@link SucursalDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SucursalQueryService extends QueryService<Sucursal> {

    private final Logger log = LoggerFactory.getLogger(SucursalQueryService.class);

    private final SucursalRepository sucursalRepository;

    private final SucursalMapper sucursalMapper;

    public SucursalQueryService(SucursalRepository sucursalRepository, SucursalMapper sucursalMapper) {
        this.sucursalRepository = sucursalRepository;
        this.sucursalMapper = sucursalMapper;
    }

    /**
     * Return a {@link List} of {@link SucursalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SucursalDTO> findByCriteria(SucursalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Sucursal> specification = createSpecification(criteria);
        return sucursalMapper.toDto(sucursalRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SucursalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SucursalDTO> findByCriteria(SucursalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Sucursal> specification = createSpecification(criteria);
        return sucursalRepository.findAll(specification, page)
            .map(sucursalMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SucursalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Sucursal> specification = createSpecification(criteria);
        return sucursalRepository.count(specification);
    }

    /**
     * Function to convert {@link SucursalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Sucursal> createSpecification(SucursalCriteria criteria) {
        Specification<Sucursal> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Sucursal_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Sucursal_.nombre));
            }
            if (criteria.getDireccion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDireccion(), Sucursal_.direccion));
            }
        }
        return specification;
    }
}
