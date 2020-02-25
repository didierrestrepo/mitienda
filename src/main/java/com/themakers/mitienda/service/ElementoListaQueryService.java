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

import com.themakers.mitienda.domain.ElementoLista;
import com.themakers.mitienda.domain.*; // for static metamodels
import com.themakers.mitienda.repository.ElementoListaRepository;
import com.themakers.mitienda.service.dto.ElementoListaCriteria;
import com.themakers.mitienda.service.dto.ElementoListaDTO;
import com.themakers.mitienda.service.mapper.ElementoListaMapper;

/**
 * Service for executing complex queries for {@link ElementoLista} entities in the database.
 * The main input is a {@link ElementoListaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ElementoListaDTO} or a {@link Page} of {@link ElementoListaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ElementoListaQueryService extends QueryService<ElementoLista> {

    private final Logger log = LoggerFactory.getLogger(ElementoListaQueryService.class);

    private final ElementoListaRepository elementoListaRepository;

    private final ElementoListaMapper elementoListaMapper;

    public ElementoListaQueryService(ElementoListaRepository elementoListaRepository, ElementoListaMapper elementoListaMapper) {
        this.elementoListaRepository = elementoListaRepository;
        this.elementoListaMapper = elementoListaMapper;
    }

    /**
     * Return a {@link List} of {@link ElementoListaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ElementoListaDTO> findByCriteria(ElementoListaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ElementoLista> specification = createSpecification(criteria);
        return elementoListaMapper.toDto(elementoListaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ElementoListaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ElementoListaDTO> findByCriteria(ElementoListaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ElementoLista> specification = createSpecification(criteria);
        return elementoListaRepository.findAll(specification, page)
            .map(elementoListaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ElementoListaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ElementoLista> specification = createSpecification(criteria);
        return elementoListaRepository.count(specification);
    }

    /**
     * Function to convert {@link ElementoListaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ElementoLista> createSpecification(ElementoListaCriteria criteria) {
        Specification<ElementoLista> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ElementoLista_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), ElementoLista_.nombre));
            }
            if (criteria.getCodigo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodigo(), ElementoLista_.codigo));
            }
            if (criteria.getTipoListaId() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoListaId(),
                    root -> root.join(ElementoLista_.tipoLista, JoinType.LEFT).get(TipoLista_.id)));
            }
            if (criteria.getPadreId() != null) {
                specification = specification.and(buildSpecification(criteria.getPadreId(),
                    root -> root.join(ElementoLista_.padre, JoinType.LEFT).get(ElementoLista_.id)));
            }
        }
        return specification;
    }
}
