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

import com.themakers.mitienda.domain.TipoLista;
import com.themakers.mitienda.domain.*; // for static metamodels
import com.themakers.mitienda.repository.TipoListaRepository;
import com.themakers.mitienda.service.dto.TipoListaCriteria;
import com.themakers.mitienda.service.dto.TipoListaDTO;
import com.themakers.mitienda.service.mapper.TipoListaMapper;

/**
 * Service for executing complex queries for {@link TipoLista} entities in the database.
 * The main input is a {@link TipoListaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TipoListaDTO} or a {@link Page} of {@link TipoListaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TipoListaQueryService extends QueryService<TipoLista> {

    private final Logger log = LoggerFactory.getLogger(TipoListaQueryService.class);

    private final TipoListaRepository tipoListaRepository;

    private final TipoListaMapper tipoListaMapper;

    public TipoListaQueryService(TipoListaRepository tipoListaRepository, TipoListaMapper tipoListaMapper) {
        this.tipoListaRepository = tipoListaRepository;
        this.tipoListaMapper = tipoListaMapper;
    }

    /**
     * Return a {@link List} of {@link TipoListaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TipoListaDTO> findByCriteria(TipoListaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TipoLista> specification = createSpecification(criteria);
        return tipoListaMapper.toDto(tipoListaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TipoListaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TipoListaDTO> findByCriteria(TipoListaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TipoLista> specification = createSpecification(criteria);
        return tipoListaRepository.findAll(specification, page)
            .map(tipoListaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TipoListaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TipoLista> specification = createSpecification(criteria);
        return tipoListaRepository.count(specification);
    }

    /**
     * Function to convert {@link TipoListaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TipoLista> createSpecification(TipoListaCriteria criteria) {
        Specification<TipoLista> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TipoLista_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), TipoLista_.nombre));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), TipoLista_.descripcion));
            }
            if (criteria.getPadreId() != null) {
                specification = specification.and(buildSpecification(criteria.getPadreId(),
                    root -> root.join(TipoLista_.padre, JoinType.LEFT).get(TipoLista_.id)));
            }
        }
        return specification;
    }
}
