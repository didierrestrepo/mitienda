package com.themakers.mitienda.web.rest;

import com.themakers.mitienda.service.SucursalService;
import com.themakers.mitienda.web.rest.errors.BadRequestAlertException;
import com.themakers.mitienda.service.dto.SucursalDTO;
import com.themakers.mitienda.service.dto.SucursalCriteria;
import com.themakers.mitienda.service.SucursalQueryService;

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
 * REST controller for managing {@link com.themakers.mitienda.domain.Sucursal}.
 */
@RestController
@RequestMapping("/api")
public class SucursalResource {

    private final Logger log = LoggerFactory.getLogger(SucursalResource.class);

    private static final String ENTITY_NAME = "sucursal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SucursalService sucursalService;

    private final SucursalQueryService sucursalQueryService;

    public SucursalResource(SucursalService sucursalService, SucursalQueryService sucursalQueryService) {
        this.sucursalService = sucursalService;
        this.sucursalQueryService = sucursalQueryService;
    }

    /**
     * {@code POST  /sucursals} : Create a new sucursal.
     *
     * @param sucursalDTO the sucursalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sucursalDTO, or with status {@code 400 (Bad Request)} if the sucursal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sucursals")
    public ResponseEntity<SucursalDTO> createSucursal(@Valid @RequestBody SucursalDTO sucursalDTO) throws URISyntaxException {
        log.debug("REST request to save Sucursal : {}", sucursalDTO);
        if (sucursalDTO.getId() != null) {
            throw new BadRequestAlertException("A new sucursal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SucursalDTO result = sucursalService.save(sucursalDTO);
        return ResponseEntity.created(new URI("/api/sucursals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sucursals} : Updates an existing sucursal.
     *
     * @param sucursalDTO the sucursalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sucursalDTO,
     * or with status {@code 400 (Bad Request)} if the sucursalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sucursalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sucursals")
    public ResponseEntity<SucursalDTO> updateSucursal(@Valid @RequestBody SucursalDTO sucursalDTO) throws URISyntaxException {
        log.debug("REST request to update Sucursal : {}", sucursalDTO);
        if (sucursalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SucursalDTO result = sucursalService.save(sucursalDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sucursalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sucursals} : get all the sucursals.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sucursals in body.
     */
    @GetMapping("/sucursals")
    public ResponseEntity<List<SucursalDTO>> getAllSucursals(SucursalCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Sucursals by criteria: {}", criteria);
        Page<SucursalDTO> page = sucursalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /sucursals/count} : count all the sucursals.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/sucursals/count")
    public ResponseEntity<Long> countSucursals(SucursalCriteria criteria) {
        log.debug("REST request to count Sucursals by criteria: {}", criteria);
        return ResponseEntity.ok().body(sucursalQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sucursals/:id} : get the "id" sucursal.
     *
     * @param id the id of the sucursalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sucursalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sucursals/{id}")
    public ResponseEntity<SucursalDTO> getSucursal(@PathVariable Long id) {
        log.debug("REST request to get Sucursal : {}", id);
        Optional<SucursalDTO> sucursalDTO = sucursalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sucursalDTO);
    }

    /**
     * {@code DELETE  /sucursals/:id} : delete the "id" sucursal.
     *
     * @param id the id of the sucursalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sucursals/{id}")
    public ResponseEntity<Void> deleteSucursal(@PathVariable Long id) {
        log.debug("REST request to delete Sucursal : {}", id);
        sucursalService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
