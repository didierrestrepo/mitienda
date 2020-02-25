package com.themakers.mitienda.web.rest;

import com.themakers.mitienda.MitiendaApp;
import com.themakers.mitienda.domain.ProductoCategoria;
import com.themakers.mitienda.domain.Producto;
import com.themakers.mitienda.repository.ProductoCategoriaRepository;
import com.themakers.mitienda.service.ProductoCategoriaService;
import com.themakers.mitienda.service.dto.ProductoCategoriaDTO;
import com.themakers.mitienda.service.mapper.ProductoCategoriaMapper;
import com.themakers.mitienda.web.rest.errors.ExceptionTranslator;
import com.themakers.mitienda.service.dto.ProductoCategoriaCriteria;
import com.themakers.mitienda.service.ProductoCategoriaQueryService;

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
 * Integration tests for the {@link ProductoCategoriaResource} REST controller.
 */
@SpringBootTest(classes = MitiendaApp.class)
public class ProductoCategoriaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private ProductoCategoriaRepository productoCategoriaRepository;

    @Autowired
    private ProductoCategoriaMapper productoCategoriaMapper;

    @Autowired
    private ProductoCategoriaService productoCategoriaService;

    @Autowired
    private ProductoCategoriaQueryService productoCategoriaQueryService;

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

    private MockMvc restProductoCategoriaMockMvc;

    private ProductoCategoria productoCategoria;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductoCategoriaResource productoCategoriaResource = new ProductoCategoriaResource(productoCategoriaService, productoCategoriaQueryService);
        this.restProductoCategoriaMockMvc = MockMvcBuilders.standaloneSetup(productoCategoriaResource)
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
    public static ProductoCategoria createEntity(EntityManager em) {
        ProductoCategoria productoCategoria = new ProductoCategoria()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION);
        return productoCategoria;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductoCategoria createUpdatedEntity(EntityManager em) {
        ProductoCategoria productoCategoria = new ProductoCategoria()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION);
        return productoCategoria;
    }

    @BeforeEach
    public void initTest() {
        productoCategoria = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductoCategoria() throws Exception {
        int databaseSizeBeforeCreate = productoCategoriaRepository.findAll().size();

        // Create the ProductoCategoria
        ProductoCategoriaDTO productoCategoriaDTO = productoCategoriaMapper.toDto(productoCategoria);
        restProductoCategoriaMockMvc.perform(post("/api/producto-categorias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productoCategoriaDTO)))
            .andExpect(status().isCreated());

        // Validate the ProductoCategoria in the database
        List<ProductoCategoria> productoCategoriaList = productoCategoriaRepository.findAll();
        assertThat(productoCategoriaList).hasSize(databaseSizeBeforeCreate + 1);
        ProductoCategoria testProductoCategoria = productoCategoriaList.get(productoCategoriaList.size() - 1);
        assertThat(testProductoCategoria.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testProductoCategoria.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createProductoCategoriaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productoCategoriaRepository.findAll().size();

        // Create the ProductoCategoria with an existing ID
        productoCategoria.setId(1L);
        ProductoCategoriaDTO productoCategoriaDTO = productoCategoriaMapper.toDto(productoCategoria);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductoCategoriaMockMvc.perform(post("/api/producto-categorias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productoCategoriaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductoCategoria in the database
        List<ProductoCategoria> productoCategoriaList = productoCategoriaRepository.findAll();
        assertThat(productoCategoriaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = productoCategoriaRepository.findAll().size();
        // set the field null
        productoCategoria.setNombre(null);

        // Create the ProductoCategoria, which fails.
        ProductoCategoriaDTO productoCategoriaDTO = productoCategoriaMapper.toDto(productoCategoria);

        restProductoCategoriaMockMvc.perform(post("/api/producto-categorias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productoCategoriaDTO)))
            .andExpect(status().isBadRequest());

        List<ProductoCategoria> productoCategoriaList = productoCategoriaRepository.findAll();
        assertThat(productoCategoriaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductoCategorias() throws Exception {
        // Initialize the database
        productoCategoriaRepository.saveAndFlush(productoCategoria);

        // Get all the productoCategoriaList
        restProductoCategoriaMockMvc.perform(get("/api/producto-categorias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productoCategoria.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }
    
    @Test
    @Transactional
    public void getProductoCategoria() throws Exception {
        // Initialize the database
        productoCategoriaRepository.saveAndFlush(productoCategoria);

        // Get the productoCategoria
        restProductoCategoriaMockMvc.perform(get("/api/producto-categorias/{id}", productoCategoria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productoCategoria.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }


    @Test
    @Transactional
    public void getProductoCategoriasByIdFiltering() throws Exception {
        // Initialize the database
        productoCategoriaRepository.saveAndFlush(productoCategoria);

        Long id = productoCategoria.getId();

        defaultProductoCategoriaShouldBeFound("id.equals=" + id);
        defaultProductoCategoriaShouldNotBeFound("id.notEquals=" + id);

        defaultProductoCategoriaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductoCategoriaShouldNotBeFound("id.greaterThan=" + id);

        defaultProductoCategoriaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductoCategoriaShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProductoCategoriasByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        productoCategoriaRepository.saveAndFlush(productoCategoria);

        // Get all the productoCategoriaList where nombre equals to DEFAULT_NOMBRE
        defaultProductoCategoriaShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the productoCategoriaList where nombre equals to UPDATED_NOMBRE
        defaultProductoCategoriaShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllProductoCategoriasByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productoCategoriaRepository.saveAndFlush(productoCategoria);

        // Get all the productoCategoriaList where nombre not equals to DEFAULT_NOMBRE
        defaultProductoCategoriaShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the productoCategoriaList where nombre not equals to UPDATED_NOMBRE
        defaultProductoCategoriaShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllProductoCategoriasByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        productoCategoriaRepository.saveAndFlush(productoCategoria);

        // Get all the productoCategoriaList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultProductoCategoriaShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the productoCategoriaList where nombre equals to UPDATED_NOMBRE
        defaultProductoCategoriaShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllProductoCategoriasByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        productoCategoriaRepository.saveAndFlush(productoCategoria);

        // Get all the productoCategoriaList where nombre is not null
        defaultProductoCategoriaShouldBeFound("nombre.specified=true");

        // Get all the productoCategoriaList where nombre is null
        defaultProductoCategoriaShouldNotBeFound("nombre.specified=false");
    }
                @Test
    @Transactional
    public void getAllProductoCategoriasByNombreContainsSomething() throws Exception {
        // Initialize the database
        productoCategoriaRepository.saveAndFlush(productoCategoria);

        // Get all the productoCategoriaList where nombre contains DEFAULT_NOMBRE
        defaultProductoCategoriaShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the productoCategoriaList where nombre contains UPDATED_NOMBRE
        defaultProductoCategoriaShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllProductoCategoriasByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        productoCategoriaRepository.saveAndFlush(productoCategoria);

        // Get all the productoCategoriaList where nombre does not contain DEFAULT_NOMBRE
        defaultProductoCategoriaShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the productoCategoriaList where nombre does not contain UPDATED_NOMBRE
        defaultProductoCategoriaShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }


    @Test
    @Transactional
    public void getAllProductoCategoriasByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        productoCategoriaRepository.saveAndFlush(productoCategoria);

        // Get all the productoCategoriaList where descripcion equals to DEFAULT_DESCRIPCION
        defaultProductoCategoriaShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the productoCategoriaList where descripcion equals to UPDATED_DESCRIPCION
        defaultProductoCategoriaShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllProductoCategoriasByDescripcionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productoCategoriaRepository.saveAndFlush(productoCategoria);

        // Get all the productoCategoriaList where descripcion not equals to DEFAULT_DESCRIPCION
        defaultProductoCategoriaShouldNotBeFound("descripcion.notEquals=" + DEFAULT_DESCRIPCION);

        // Get all the productoCategoriaList where descripcion not equals to UPDATED_DESCRIPCION
        defaultProductoCategoriaShouldBeFound("descripcion.notEquals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllProductoCategoriasByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        productoCategoriaRepository.saveAndFlush(productoCategoria);

        // Get all the productoCategoriaList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultProductoCategoriaShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the productoCategoriaList where descripcion equals to UPDATED_DESCRIPCION
        defaultProductoCategoriaShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllProductoCategoriasByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productoCategoriaRepository.saveAndFlush(productoCategoria);

        // Get all the productoCategoriaList where descripcion is not null
        defaultProductoCategoriaShouldBeFound("descripcion.specified=true");

        // Get all the productoCategoriaList where descripcion is null
        defaultProductoCategoriaShouldNotBeFound("descripcion.specified=false");
    }
                @Test
    @Transactional
    public void getAllProductoCategoriasByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        productoCategoriaRepository.saveAndFlush(productoCategoria);

        // Get all the productoCategoriaList where descripcion contains DEFAULT_DESCRIPCION
        defaultProductoCategoriaShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the productoCategoriaList where descripcion contains UPDATED_DESCRIPCION
        defaultProductoCategoriaShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllProductoCategoriasByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        productoCategoriaRepository.saveAndFlush(productoCategoria);

        // Get all the productoCategoriaList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultProductoCategoriaShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the productoCategoriaList where descripcion does not contain UPDATED_DESCRIPCION
        defaultProductoCategoriaShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }


    @Test
    @Transactional
    public void getAllProductoCategoriasByProductoIsEqualToSomething() throws Exception {
        // Initialize the database
        productoCategoriaRepository.saveAndFlush(productoCategoria);
        Producto producto = ProductoResourceIT.createEntity(em);
        em.persist(producto);
        em.flush();
        productoCategoria.addProducto(producto);
        productoCategoriaRepository.saveAndFlush(productoCategoria);
        Long productoId = producto.getId();

        // Get all the productoCategoriaList where producto equals to productoId
        defaultProductoCategoriaShouldBeFound("productoId.equals=" + productoId);

        // Get all the productoCategoriaList where producto equals to productoId + 1
        defaultProductoCategoriaShouldNotBeFound("productoId.equals=" + (productoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductoCategoriaShouldBeFound(String filter) throws Exception {
        restProductoCategoriaMockMvc.perform(get("/api/producto-categorias?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productoCategoria.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));

        // Check, that the count call also returns 1
        restProductoCategoriaMockMvc.perform(get("/api/producto-categorias/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductoCategoriaShouldNotBeFound(String filter) throws Exception {
        restProductoCategoriaMockMvc.perform(get("/api/producto-categorias?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductoCategoriaMockMvc.perform(get("/api/producto-categorias/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProductoCategoria() throws Exception {
        // Get the productoCategoria
        restProductoCategoriaMockMvc.perform(get("/api/producto-categorias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductoCategoria() throws Exception {
        // Initialize the database
        productoCategoriaRepository.saveAndFlush(productoCategoria);

        int databaseSizeBeforeUpdate = productoCategoriaRepository.findAll().size();

        // Update the productoCategoria
        ProductoCategoria updatedProductoCategoria = productoCategoriaRepository.findById(productoCategoria.getId()).get();
        // Disconnect from session so that the updates on updatedProductoCategoria are not directly saved in db
        em.detach(updatedProductoCategoria);
        updatedProductoCategoria
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION);
        ProductoCategoriaDTO productoCategoriaDTO = productoCategoriaMapper.toDto(updatedProductoCategoria);

        restProductoCategoriaMockMvc.perform(put("/api/producto-categorias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productoCategoriaDTO)))
            .andExpect(status().isOk());

        // Validate the ProductoCategoria in the database
        List<ProductoCategoria> productoCategoriaList = productoCategoriaRepository.findAll();
        assertThat(productoCategoriaList).hasSize(databaseSizeBeforeUpdate);
        ProductoCategoria testProductoCategoria = productoCategoriaList.get(productoCategoriaList.size() - 1);
        assertThat(testProductoCategoria.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testProductoCategoria.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void updateNonExistingProductoCategoria() throws Exception {
        int databaseSizeBeforeUpdate = productoCategoriaRepository.findAll().size();

        // Create the ProductoCategoria
        ProductoCategoriaDTO productoCategoriaDTO = productoCategoriaMapper.toDto(productoCategoria);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoCategoriaMockMvc.perform(put("/api/producto-categorias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productoCategoriaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductoCategoria in the database
        List<ProductoCategoria> productoCategoriaList = productoCategoriaRepository.findAll();
        assertThat(productoCategoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProductoCategoria() throws Exception {
        // Initialize the database
        productoCategoriaRepository.saveAndFlush(productoCategoria);

        int databaseSizeBeforeDelete = productoCategoriaRepository.findAll().size();

        // Delete the productoCategoria
        restProductoCategoriaMockMvc.perform(delete("/api/producto-categorias/{id}", productoCategoria.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductoCategoria> productoCategoriaList = productoCategoriaRepository.findAll();
        assertThat(productoCategoriaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
