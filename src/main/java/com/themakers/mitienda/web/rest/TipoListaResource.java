package com.themakers.mitienda.web.rest;

import com.themakers.mitienda.service.TipoListaService;
import com.themakers.mitienda.web.rest.errors.BadRequestAlertException;
import com.themakers.mitienda.service.dto.TipoListaDTO;
import com.themakers.mitienda.service.dto.TipoListaCriteria;
import com.themakers.mitienda.service.TipoListaQueryService;

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
 * REST controller for managing {@link com.themakers.mitienda.domain.TipoLista}.
 */
@RestController
@RequestMapping("/api")
public class TipoListaResource {

    private final Logger log = LoggerFactory.getLogger(TipoListaResource.class);

    private static final String ENTITY_NAME = "tipoLista";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoListaService tipoListaService;

    private final TipoListaQueryService tipoListaQueryService;

    public TipoListaResource(TipoListaService tipoListaService, TipoListaQueryService tipoListaQueryService) {
        this.tipoListaService = tipoListaService;
        this.tipoListaQueryService = tipoListaQueryService;
    }

    /**
     * {@code POST  /tipo-listas} : Create a new tipoLista.
     *
     * @param tipoListaDTO the tipoListaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoListaDTO, or with status {@code 400 (Bad Request)} if the tipoLista has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tipo-listas")
    public ResponseEntity<TipoListaDTO> createTipoLista(@Valid @RequestBody TipoListaDTO tipoListaDTO) throws URISyntaxException {
        log.debug("REST request to save TipoLista : {}", tipoListaDTO);
        if (tipoListaDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoLista cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TipoListaDTO result = tipoListaService.save(tipoListaDTO);
        return ResponseEntity.created(new URI("/api/tipo-listas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tipo-listas} : Updates an existing tipoLista.
     *
     * @param tipoListaDTO the tipoListaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoListaDTO,
     * or with status {@code 400 (Bad Request)} if the tipoListaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoListaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tipo-listas")
    public ResponseEntity<TipoListaDTO> updateTipoLista(@Valid @RequestBody TipoListaDTO tipoListaDTO) throws URISyntaxException {
        log.debug("REST request to update TipoLista : {}", tipoListaDTO);
        if (tipoListaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TipoListaDTO result = tipoListaService.save(tipoListaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoListaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tipo-listas} : get all the tipoListas.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoListas in body.
     */
    @GetMapping("/tipo-listas")
    public ResponseEntity<List<TipoListaDTO>> getAllTipoListas(TipoListaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TipoListas by criteria: {}", criteria);
        Page<TipoListaDTO> page = tipoListaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /tipo-listas/count} : count all the tipoListas.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/tipo-listas/count")
    public ResponseEntity<Long> countTipoListas(TipoListaCriteria criteria) {
        log.debug("REST request to count TipoListas by criteria: {}", criteria);
        return ResponseEntity.ok().body(tipoListaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tipo-listas/:id} : get the "id" tipoLista.
     *
     * @param id the id of the tipoListaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoListaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tipo-listas/{id}")
    public ResponseEntity<TipoListaDTO> getTipoLista(@PathVariable Long id) {
        log.debug("REST request to get TipoLista : {}", id);
        Optional<TipoListaDTO> tipoListaDTO = tipoListaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoListaDTO);
    }

    /**
     * {@code DELETE  /tipo-listas/:id} : delete the "id" tipoLista.
     *
     * @param id the id of the tipoListaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tipo-listas/{id}")
    public ResponseEntity<Void> deleteTipoLista(@PathVariable Long id) {
        log.debug("REST request to delete TipoLista : {}", id);
        tipoListaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
