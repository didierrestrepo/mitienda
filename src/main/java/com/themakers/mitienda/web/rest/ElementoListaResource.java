package com.themakers.mitienda.web.rest;

import com.themakers.mitienda.service.ElementoListaService;
import com.themakers.mitienda.web.rest.errors.BadRequestAlertException;
import com.themakers.mitienda.service.dto.ElementoListaDTO;
import com.themakers.mitienda.service.dto.ElementoListaCriteria;
import com.themakers.mitienda.service.ElementoListaQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.themakers.mitienda.domain.ElementoLista}.
 */
@RestController
@RequestMapping("/api")
public class ElementoListaResource {

    private final Logger log = LoggerFactory.getLogger(ElementoListaResource.class);

    private static final String ENTITY_NAME = "elementoLista";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ElementoListaService elementoListaService;

    private final ElementoListaQueryService elementoListaQueryService;

    public ElementoListaResource(ElementoListaService elementoListaService, ElementoListaQueryService elementoListaQueryService) {
        this.elementoListaService = elementoListaService;
        this.elementoListaQueryService = elementoListaQueryService;
    }

    /**
     * {@code POST  /elemento-listas} : Create a new elementoLista.
     *
     * @param elementoListaDTO the elementoListaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new elementoListaDTO, or with status {@code 400 (Bad Request)} if the elementoLista has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/elemento-listas")
    public ResponseEntity<ElementoListaDTO> createElementoLista(@Valid @RequestBody ElementoListaDTO elementoListaDTO) throws URISyntaxException {
        log.debug("REST request to save ElementoLista : {}", elementoListaDTO);
        if (elementoListaDTO.getId() != null) {
            throw new BadRequestAlertException("A new elementoLista cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ElementoListaDTO result = elementoListaService.save(elementoListaDTO);
        return ResponseEntity.created(new URI("/api/elemento-listas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /elemento-listas} : Updates an existing elementoLista.
     *
     * @param elementoListaDTO the elementoListaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated elementoListaDTO,
     * or with status {@code 400 (Bad Request)} if the elementoListaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the elementoListaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/elemento-listas")
    public ResponseEntity<ElementoListaDTO> updateElementoLista(@Valid @RequestBody ElementoListaDTO elementoListaDTO) throws URISyntaxException {
        log.debug("REST request to update ElementoLista : {}", elementoListaDTO);
        if (elementoListaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ElementoListaDTO result = elementoListaService.save(elementoListaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, elementoListaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /elemento-listas} : get all the elementoListas.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of elementoListas in body.
     */
    @GetMapping("/elemento-listas")
    public ResponseEntity<List<ElementoListaDTO>> getAllElementoListas(ElementoListaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ElementoListas by criteria: {}", criteria);
        Page<ElementoListaDTO> page = elementoListaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /elemento-listas/count} : count all the elementoListas.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/elemento-listas/count")
    public ResponseEntity<Long> countElementoListas(ElementoListaCriteria criteria) {
        log.debug("REST request to count ElementoListas by criteria: {}", criteria);
        return ResponseEntity.ok().body(elementoListaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /elemento-listas/:id} : get the "id" elementoLista.
     *
     * @param id the id of the elementoListaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the elementoListaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/elemento-listas/{id}")
    public ResponseEntity<ElementoListaDTO> getElementoLista(@PathVariable Long id) {
        log.debug("REST request to get ElementoLista : {}", id);
        Optional<ElementoListaDTO> elementoListaDTO = elementoListaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(elementoListaDTO);
    }

    /**
     * {@code DELETE  /elemento-listas/:id} : delete the "id" elementoLista.
     *
     * @param id the id of the elementoListaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/elemento-listas/{id}")
    public ResponseEntity<Void> deleteElementoLista(@PathVariable Long id) {
        log.debug("REST request to delete ElementoLista : {}", id);
        elementoListaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
