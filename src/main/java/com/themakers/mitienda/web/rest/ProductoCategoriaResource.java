package com.themakers.mitienda.web.rest;

import com.themakers.mitienda.service.ProductoCategoriaService;
import com.themakers.mitienda.web.rest.errors.BadRequestAlertException;
import com.themakers.mitienda.service.dto.ProductoCategoriaDTO;
import com.themakers.mitienda.service.dto.ProductoCategoriaCriteria;
import com.themakers.mitienda.service.ProductoCategoriaQueryService;

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
 * REST controller for managing {@link com.themakers.mitienda.domain.ProductoCategoria}.
 */
@RestController
@RequestMapping("/api")
public class ProductoCategoriaResource {

    private final Logger log = LoggerFactory.getLogger(ProductoCategoriaResource.class);

    private static final String ENTITY_NAME = "productoCategoria";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductoCategoriaService productoCategoriaService;

    private final ProductoCategoriaQueryService productoCategoriaQueryService;

    public ProductoCategoriaResource(ProductoCategoriaService productoCategoriaService, ProductoCategoriaQueryService productoCategoriaQueryService) {
        this.productoCategoriaService = productoCategoriaService;
        this.productoCategoriaQueryService = productoCategoriaQueryService;
    }

    /**
     * {@code POST  /producto-categorias} : Create a new productoCategoria.
     *
     * @param productoCategoriaDTO the productoCategoriaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productoCategoriaDTO, or with status {@code 400 (Bad Request)} if the productoCategoria has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/producto-categorias")
    public ResponseEntity<ProductoCategoriaDTO> createProductoCategoria(@Valid @RequestBody ProductoCategoriaDTO productoCategoriaDTO) throws URISyntaxException {
        log.debug("REST request to save ProductoCategoria : {}", productoCategoriaDTO);
        if (productoCategoriaDTO.getId() != null) {
            throw new BadRequestAlertException("A new productoCategoria cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductoCategoriaDTO result = productoCategoriaService.save(productoCategoriaDTO);
        return ResponseEntity.created(new URI("/api/producto-categorias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /producto-categorias} : Updates an existing productoCategoria.
     *
     * @param productoCategoriaDTO the productoCategoriaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productoCategoriaDTO,
     * or with status {@code 400 (Bad Request)} if the productoCategoriaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productoCategoriaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/producto-categorias")
    public ResponseEntity<ProductoCategoriaDTO> updateProductoCategoria(@Valid @RequestBody ProductoCategoriaDTO productoCategoriaDTO) throws URISyntaxException {
        log.debug("REST request to update ProductoCategoria : {}", productoCategoriaDTO);
        if (productoCategoriaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductoCategoriaDTO result = productoCategoriaService.save(productoCategoriaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productoCategoriaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /producto-categorias} : get all the productoCategorias.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productoCategorias in body.
     */
    @GetMapping("/producto-categorias")
    public ResponseEntity<List<ProductoCategoriaDTO>> getAllProductoCategorias(ProductoCategoriaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProductoCategorias by criteria: {}", criteria);
        Page<ProductoCategoriaDTO> page = productoCategoriaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /producto-categorias/count} : count all the productoCategorias.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/producto-categorias/count")
    public ResponseEntity<Long> countProductoCategorias(ProductoCategoriaCriteria criteria) {
        log.debug("REST request to count ProductoCategorias by criteria: {}", criteria);
        return ResponseEntity.ok().body(productoCategoriaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /producto-categorias/:id} : get the "id" productoCategoria.
     *
     * @param id the id of the productoCategoriaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productoCategoriaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/producto-categorias/{id}")
    public ResponseEntity<ProductoCategoriaDTO> getProductoCategoria(@PathVariable Long id) {
        log.debug("REST request to get ProductoCategoria : {}", id);
        Optional<ProductoCategoriaDTO> productoCategoriaDTO = productoCategoriaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productoCategoriaDTO);
    }

    /**
     * {@code DELETE  /producto-categorias/:id} : delete the "id" productoCategoria.
     *
     * @param id the id of the productoCategoriaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/producto-categorias/{id}")
    public ResponseEntity<Void> deleteProductoCategoria(@PathVariable Long id) {
        log.debug("REST request to delete ProductoCategoria : {}", id);
        productoCategoriaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
