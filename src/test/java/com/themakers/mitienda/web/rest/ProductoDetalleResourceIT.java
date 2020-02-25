package com.themakers.mitienda.web.rest;

import com.themakers.mitienda.MitiendaApp;
import com.themakers.mitienda.domain.ProductoDetalle;
import com.themakers.mitienda.domain.ElementoLista;
import com.themakers.mitienda.domain.Producto;
import com.themakers.mitienda.repository.ProductoDetalleRepository;
import com.themakers.mitienda.service.ProductoDetalleService;
import com.themakers.mitienda.service.dto.ProductoDetalleDTO;
import com.themakers.mitienda.service.mapper.ProductoDetalleMapper;
import com.themakers.mitienda.web.rest.errors.ExceptionTranslator;
import com.themakers.mitienda.service.dto.ProductoDetalleCriteria;
import com.themakers.mitienda.service.ProductoDetalleQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.themakers.mitienda.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProductoDetalleResource} REST controller.
 */
@SpringBootTest(classes = MitiendaApp.class)
public class ProductoDetalleResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    @Autowired
    private ProductoDetalleRepository productoDetalleRepository;

    @Autowired
    private ProductoDetalleMapper productoDetalleMapper;

    @Autowired
    private ProductoDetalleService productoDetalleService;

    @Autowired
    private ProductoDetalleQueryService productoDetalleQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restProductoDetalleMockMvc;

    private ProductoDetalle productoDetalle;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductoDetalleResource productoDetalleResource = new ProductoDetalleResource(productoDetalleService, productoDetalleQueryService);
        this.restProductoDetalleMockMvc = MockMvcBuilders.standaloneSetup(productoDetalleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductoDetalle createEntity(EntityManager em) {
        ProductoDetalle productoDetalle = new ProductoDetalle()
            .codigo(DEFAULT_CODIGO);
        // Add required entity
        ElementoLista elementoLista;
        if (TestUtil.findAll(em, ElementoLista.class).isEmpty()) {
            elementoLista = ElementoListaResourceIT.createEntity(em);
            em.persist(elementoLista);
            em.flush();
        } else {
            elementoLista = TestUtil.findAll(em, ElementoLista.class).get(0);
        }
        productoDetalle.setTalla(elementoLista);
        // Add required entity
        productoDetalle.setColor(elementoLista);
        return productoDetalle;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductoDetalle createUpdatedEntity(EntityManager em) {
        ProductoDetalle productoDetalle = new ProductoDetalle()
            .codigo(UPDATED_CODIGO);
        // Add required entity
        ElementoLista elementoLista;
        if (TestUtil.findAll(em, ElementoLista.class).isEmpty()) {
            elementoLista = ElementoListaResourceIT.createUpdatedEntity(em);
            em.persist(elementoLista);
            em.flush();
        } else {
            elementoLista = TestUtil.findAll(em, ElementoLista.class).get(0);
        }
        productoDetalle.setTalla(elementoLista);
        // Add required entity
        productoDetalle.setColor(elementoLista);
        return productoDetalle;
    }

    @BeforeEach
    public void initTest() {
        productoDetalle = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductoDetalle() throws Exception {
        int databaseSizeBeforeCreate = productoDetalleRepository.findAll().size();

        // Create the ProductoDetalle
        ProductoDetalleDTO productoDetalleDTO = productoDetalleMapper.toDto(productoDetalle);
        restProductoDetalleMockMvc.perform(post("/api/producto-detalles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productoDetalleDTO)))
            .andExpect(status().isCreated());

        // Validate the ProductoDetalle in the database
        List<ProductoDetalle> productoDetalleList = productoDetalleRepository.findAll();
        assertThat(productoDetalleList).hasSize(databaseSizeBeforeCreate + 1);
        ProductoDetalle testProductoDetalle = productoDetalleList.get(productoDetalleList.size() - 1);
        assertThat(testProductoDetalle.getCodigo()).isEqualTo(DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    public void createProductoDetalleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productoDetalleRepository.findAll().size();

        // Create the ProductoDetalle with an existing ID
        productoDetalle.setId(1L);
        ProductoDetalleDTO productoDetalleDTO = productoDetalleMapper.toDto(productoDetalle);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductoDetalleMockMvc.perform(post("/api/producto-detalles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productoDetalleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductoDetalle in the database
        List<ProductoDetalle> productoDetalleList = productoDetalleRepository.findAll();
        assertThat(productoDetalleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllProductoDetalles() throws Exception {
        // Initialize the database
        productoDetalleRepository.saveAndFlush(productoDetalle);

        // Get all the productoDetalleList
        restProductoDetalleMockMvc.perform(get("/api/producto-detalles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productoDetalle.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)));
    }
    
    @Test
    @Transactional
    public void getProductoDetalle() throws Exception {
        // Initialize the database
        productoDetalleRepository.saveAndFlush(productoDetalle);

        // Get the productoDetalle
        restProductoDetalleMockMvc.perform(get("/api/producto-detalles/{id}", productoDetalle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productoDetalle.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO));
    }


    @Test
    @Transactional
    public void getProductoDetallesByIdFiltering() throws Exception {
        // Initialize the database
        productoDetalleRepository.saveAndFlush(productoDetalle);

        Long id = productoDetalle.getId();

        defaultProductoDetalleShouldBeFound("id.equals=" + id);
        defaultProductoDetalleShouldNotBeFound("id.notEquals=" + id);

        defaultProductoDetalleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductoDetalleShouldNotBeFound("id.greaterThan=" + id);

        defaultProductoDetalleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductoDetalleShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProductoDetallesByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        productoDetalleRepository.saveAndFlush(productoDetalle);

        // Get all the productoDetalleList where codigo equals to DEFAULT_CODIGO
        defaultProductoDetalleShouldBeFound("codigo.equals=" + DEFAULT_CODIGO);

        // Get all the productoDetalleList where codigo equals to UPDATED_CODIGO
        defaultProductoDetalleShouldNotBeFound("codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllProductoDetallesByCodigoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productoDetalleRepository.saveAndFlush(productoDetalle);

        // Get all the productoDetalleList where codigo not equals to DEFAULT_CODIGO
        defaultProductoDetalleShouldNotBeFound("codigo.notEquals=" + DEFAULT_CODIGO);

        // Get all the productoDetalleList where codigo not equals to UPDATED_CODIGO
        defaultProductoDetalleShouldBeFound("codigo.notEquals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllProductoDetallesByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        productoDetalleRepository.saveAndFlush(productoDetalle);

        // Get all the productoDetalleList where codigo in DEFAULT_CODIGO or UPDATED_CODIGO
        defaultProductoDetalleShouldBeFound("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO);

        // Get all the productoDetalleList where codigo equals to UPDATED_CODIGO
        defaultProductoDetalleShouldNotBeFound("codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllProductoDetallesByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        productoDetalleRepository.saveAndFlush(productoDetalle);

        // Get all the productoDetalleList where codigo is not null
        defaultProductoDetalleShouldBeFound("codigo.specified=true");

        // Get all the productoDetalleList where codigo is null
        defaultProductoDetalleShouldNotBeFound("codigo.specified=false");
    }
                @Test
    @Transactional
    public void getAllProductoDetallesByCodigoContainsSomething() throws Exception {
        // Initialize the database
        productoDetalleRepository.saveAndFlush(productoDetalle);

        // Get all the productoDetalleList where codigo contains DEFAULT_CODIGO
        defaultProductoDetalleShouldBeFound("codigo.contains=" + DEFAULT_CODIGO);

        // Get all the productoDetalleList where codigo contains UPDATED_CODIGO
        defaultProductoDetalleShouldNotBeFound("codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllProductoDetallesByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        productoDetalleRepository.saveAndFlush(productoDetalle);

        // Get all the productoDetalleList where codigo does not contain DEFAULT_CODIGO
        defaultProductoDetalleShouldNotBeFound("codigo.doesNotContain=" + DEFAULT_CODIGO);

        // Get all the productoDetalleList where codigo does not contain UPDATED_CODIGO
        defaultProductoDetalleShouldBeFound("codigo.doesNotContain=" + UPDATED_CODIGO);
    }


    @Test
    @Transactional
    public void getAllProductoDetallesByTallaIsEqualToSomething() throws Exception {
        // Get already existing entity
        ElementoLista talla = productoDetalle.getTalla();
        productoDetalleRepository.saveAndFlush(productoDetalle);
        Long tallaId = talla.getId();

        // Get all the productoDetalleList where talla equals to tallaId
        defaultProductoDetalleShouldBeFound("tallaId.equals=" + tallaId);

        // Get all the productoDetalleList where talla equals to tallaId + 1
        defaultProductoDetalleShouldNotBeFound("tallaId.equals=" + (tallaId + 1));
    }


    @Test
    @Transactional
    public void getAllProductoDetallesByColorIsEqualToSomething() throws Exception {
        // Get already existing entity
        ElementoLista color = productoDetalle.getColor();
        productoDetalleRepository.saveAndFlush(productoDetalle);
        Long colorId = color.getId();

        // Get all the productoDetalleList where color equals to colorId
        defaultProductoDetalleShouldBeFound("colorId.equals=" + colorId);

        // Get all the productoDetalleList where color equals to colorId + 1
        defaultProductoDetalleShouldNotBeFound("colorId.equals=" + (colorId + 1));
    }


    @Test
    @Transactional
    public void getAllProductoDetallesByProductoIsEqualToSomething() throws Exception {
        // Initialize the database
        productoDetalleRepository.saveAndFlush(productoDetalle);
        Producto producto = ProductoResourceIT.createEntity(em);
        em.persist(producto);
        em.flush();
        productoDetalle.setProducto(producto);
        productoDetalleRepository.saveAndFlush(productoDetalle);
        Long productoId = producto.getId();

        // Get all the productoDetalleList where producto equals to productoId
        defaultProductoDetalleShouldBeFound("productoId.equals=" + productoId);

        // Get all the productoDetalleList where producto equals to productoId + 1
        defaultProductoDetalleShouldNotBeFound("productoId.equals=" + (productoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductoDetalleShouldBeFound(String filter) throws Exception {
        restProductoDetalleMockMvc.perform(get("/api/producto-detalles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productoDetalle.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)));

        // Check, that the count call also returns 1
        restProductoDetalleMockMvc.perform(get("/api/producto-detalles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductoDetalleShouldNotBeFound(String filter) throws Exception {
        restProductoDetalleMockMvc.perform(get("/api/producto-detalles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductoDetalleMockMvc.perform(get("/api/producto-detalles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProductoDetalle() throws Exception {
        // Get the productoDetalle
        restProductoDetalleMockMvc.perform(get("/api/producto-detalles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductoDetalle() throws Exception {
        // Initialize the database
        productoDetalleRepository.saveAndFlush(productoDetalle);

        int databaseSizeBeforeUpdate = productoDetalleRepository.findAll().size();

        // Update the productoDetalle
        ProductoDetalle updatedProductoDetalle = productoDetalleRepository.findById(productoDetalle.getId()).get();
        // Disconnect from session so that the updates on updatedProductoDetalle are not directly saved in db
        em.detach(updatedProductoDetalle);
        updatedProductoDetalle
            .codigo(UPDATED_CODIGO);
        ProductoDetalleDTO productoDetalleDTO = productoDetalleMapper.toDto(updatedProductoDetalle);

        restProductoDetalleMockMvc.perform(put("/api/producto-detalles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productoDetalleDTO)))
            .andExpect(status().isOk());

        // Validate the ProductoDetalle in the database
        List<ProductoDetalle> productoDetalleList = productoDetalleRepository.findAll();
        assertThat(productoDetalleList).hasSize(databaseSizeBeforeUpdate);
        ProductoDetalle testProductoDetalle = productoDetalleList.get(productoDetalleList.size() - 1);
        assertThat(testProductoDetalle.getCodigo()).isEqualTo(UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void updateNonExistingProductoDetalle() throws Exception {
        int databaseSizeBeforeUpdate = productoDetalleRepository.findAll().size();

        // Create the ProductoDetalle
        ProductoDetalleDTO productoDetalleDTO = productoDetalleMapper.toDto(productoDetalle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoDetalleMockMvc.perform(put("/api/producto-detalles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productoDetalleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductoDetalle in the database
        List<ProductoDetalle> productoDetalleList = productoDetalleRepository.findAll();
        assertThat(productoDetalleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProductoDetalle() throws Exception {
        // Initialize the database
        productoDetalleRepository.saveAndFlush(productoDetalle);

        int databaseSizeBeforeDelete = productoDetalleRepository.findAll().size();

        // Delete the productoDetalle
        restProductoDetalleMockMvc.perform(delete("/api/producto-detalles/{id}", productoDetalle.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductoDetalle> productoDetalleList = productoDetalleRepository.findAll();
        assertThat(productoDetalleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
