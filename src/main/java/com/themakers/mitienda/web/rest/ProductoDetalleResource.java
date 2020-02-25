package com.themakers.mitienda.web.rest;

import com.themakers.mitienda.service.ProductoDetalleService;
import com.themakers.mitienda.web.rest.errors.BadRequestAlertException;
import com.themakers.mitienda.service.dto.ProductoDetalleDTO;
import com.themakers.mitienda.service.dto.ProductoDetalleCriteria;
import com.themakers.mitienda.service.ProductoDetalleQueryService;

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
 * REST controller for managing {@link com.themakers.mitienda.domain.ProductoDetalle}.
 */
@RestController
@RequestMapping("/api")
public class ProductoDetalleResource {

    private final Logger log = LoggerFactory.getLogger(ProductoDetalleResource.class);

    private static final String ENTITY_NAME = "productoDetalle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductoDetalleService productoDetalleService;

    private final ProductoDetalleQueryService productoDetalleQueryService;

    public ProductoDetalleResource(ProductoDetalleService productoDetalleService, ProductoDetalleQueryService productoDetalleQueryService) {
        this.productoDetalleService = productoDetalleService;
        this.productoDetalleQueryService = productoDetalleQueryService;
    }

    /**
     * {@code POST  /producto-detalles} : Create a new productoDetalle.
     *
     * @param productoDetalleDTO the productoDetalleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productoDetalleDTO, or with status {@code 400 (Bad Request)} if the productoDetalle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/producto-detalles")
    public ResponseEntity<ProductoDetalleDTO> createProductoDetalle(@Valid @RequestBody ProductoDetalleDTO productoDetalleDTO) throws URISyntaxException {
        log.debug("REST request to save ProductoDetalle : {}", productoDetalleDTO);
        if (productoDetalleDTO.getId() != null) {
            throw new BadRequestAlertException("A new productoDetalle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductoDetalleDTO result = productoDetalleService.save(productoDetalleDTO);
        return ResponseEntity.created(new URI("/api/producto-detalles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /producto-detalles} : Updates an existing productoDetalle.
     *
     * @param productoDetalleDTO the productoDetalleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productoDetalleDTO,
     * or with status {@code 400 (Bad Request)} if the productoDetalleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productoDetalleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/producto-detalles")
    public ResponseEntity<ProductoDetalleDTO> updateProductoDetalle(@Valid @RequestBody ProductoDetalleDTO productoDetalleDTO) throws URISyntaxException {
        log.debug("REST request to update ProductoDetalle : {}", productoDetalleDTO);
        if (productoDetalleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductoDetalleDTO result = productoDetalleService.save(productoDetalleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productoDetalleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /producto-detalles} : get all the productoDetalles.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productoDetalles in body.
     */
    @GetMapping("/producto-detalles")
    public ResponseEntity<List<ProductoDetalleDTO>> getAllProductoDetalles(ProductoDetalleCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProductoDetalles by criteria: {}", criteria);
        Page<ProductoDetalleDTO> page = productoDetalleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /producto-detalles/count} : count all the productoDetalles.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/producto-detalles/count")
    public ResponseEntity<Long> countProductoDetalles(ProductoDetalleCriteria criteria) {
        log.debug("REST request to count ProductoDetalles by criteria: {}", criteria);
        return ResponseEntity.ok().body(productoDetalleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /producto-detalles/:id} : get the "id" productoDetalle.
     *
     * @param id the id of the productoDetalleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productoDetalleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/producto-detalles/{id}")
    public ResponseEntity<ProductoDetalleDTO> getProductoDetalle(@PathVariable Long id) {
        log.debug("REST request to get ProductoDetalle : {}", id);
        Optional<ProductoDetalleDTO> productoDetalleDTO = productoDetalleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productoDetalleDTO);
    }

    /**
     * {@code DELETE  /producto-detalles/:id} : delete the "id" productoDetalle.
     *
     * @param id the id of the productoDetalleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/producto-detalles/{id}")
    public ResponseEntity<Void> deleteProductoDetalle(@PathVariable Long id) {
        log.debug("REST request to delete ProductoDetalle : {}", id);
        productoDetalleService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
