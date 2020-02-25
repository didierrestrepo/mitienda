package com.themakers.mitienda.web.rest;

import com.themakers.mitienda.MitiendaApp;
import com.themakers.mitienda.domain.Producto;
import com.themakers.mitienda.domain.ProductoDetalle;
import com.themakers.mitienda.domain.ProductoCategoria;
import com.themakers.mitienda.repository.ProductoRepository;
import com.themakers.mitienda.service.ProductoService;
import com.themakers.mitienda.service.dto.ProductoDTO;
import com.themakers.mitienda.service.mapper.ProductoMapper;
import com.themakers.mitienda.web.rest.errors.ExceptionTranslator;
import com.themakers.mitienda.service.dto.ProductoCriteria;
import com.themakers.mitienda.service.ProductoQueryService;

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
import java.math.BigDecimal;
import java.util.List;

import static com.themakers.mitienda.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.themakers.mitienda.domain.enumeration.EstadoProducto;
/**
 * Integration tests for the {@link ProductoResource} REST controller.
 */
@SpringBootTest(classes = MitiendaApp.class)
public class ProductoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRECIO_COMPRA = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRECIO_COMPRA = new BigDecimal(1);
    private static final BigDecimal SMALLER_PRECIO_COMPRA = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_PRECIO_VENTA = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRECIO_VENTA = new BigDecimal(1);
    private static final BigDecimal SMALLER_PRECIO_VENTA = new BigDecimal(0 - 1);

    private static final EstadoProducto DEFAULT_ESTADO = EstadoProducto.ACTIVO;
    private static final EstadoProducto UPDATED_ESTADO = EstadoProducto.INACTIVO;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoMapper productoMapper;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoQueryService productoQueryService;

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

    private MockMvc restProductoMockMvc;

    private Producto producto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductoResource productoResource = new ProductoResource(productoService, productoQueryService);
        this.restProductoMockMvc = MockMvcBuilders.standaloneSetup(productoResource)
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
    public static Producto createEntity(EntityManager em) {
        Producto producto = new Producto()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .precioCompra(DEFAULT_PRECIO_COMPRA)
            .precioVenta(DEFAULT_PRECIO_VENTA)
            .estado(DEFAULT_ESTADO);
        return producto;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Producto createUpdatedEntity(EntityManager em) {
        Producto producto = new Producto()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precioCompra(UPDATED_PRECIO_COMPRA)
            .precioVenta(UPDATED_PRECIO_VENTA)
            .estado(UPDATED_ESTADO);
        return producto;
    }

    @BeforeEach
    public void initTest() {
        producto = createEntity(em);
    }

    @Test
    @Transactional
    public void createProducto() throws Exception {
        int databaseSizeBeforeCreate = productoRepository.findAll().size();

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);
        restProductoMockMvc.perform(post("/api/productos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productoDTO)))
            .andExpect(status().isCreated());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeCreate + 1);
        Producto testProducto = productoList.get(productoList.size() - 1);
        assertThat(testProducto.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testProducto.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testProducto.getPrecioCompra()).isEqualTo(DEFAULT_PRECIO_COMPRA);
        assertThat(testProducto.getPrecioVenta()).isEqualTo(DEFAULT_PRECIO_VENTA);
        assertThat(testProducto.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    public void createProductoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productoRepository.findAll().size();

        // Create the Producto with an existing ID
        producto.setId(1L);
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductoMockMvc.perform(post("/api/productos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = productoRepository.findAll().size();
        // set the field null
        producto.setNombre(null);

        // Create the Producto, which fails.
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        restProductoMockMvc.perform(post("/api/productos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrecioCompraIsRequired() throws Exception {
        int databaseSizeBeforeTest = productoRepository.findAll().size();
        // set the field null
        producto.setPrecioCompra(null);

        // Create the Producto, which fails.
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        restProductoMockMvc.perform(post("/api/productos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrecioVentaIsRequired() throws Exception {
        int databaseSizeBeforeTest = productoRepository.findAll().size();
        // set the field null
        producto.setPrecioVenta(null);

        // Create the Producto, which fails.
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        restProductoMockMvc.perform(post("/api/productos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductos() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList
        restProductoMockMvc.perform(get("/api/productos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(producto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].precioCompra").value(hasItem(DEFAULT_PRECIO_COMPRA.intValue())))
            .andExpect(jsonPath("$.[*].precioVenta").value(hasItem(DEFAULT_PRECIO_VENTA.intValue())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }
    
    @Test
    @Transactional
    public void getProducto() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get the producto
        restProductoMockMvc.perform(get("/api/productos/{id}", producto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(producto.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.precioCompra").value(DEFAULT_PRECIO_COMPRA.intValue()))
            .andExpect(jsonPath("$.precioVenta").value(DEFAULT_PRECIO_VENTA.intValue()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }


    @Test
    @Transactional
    public void getProductosByIdFiltering() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        Long id = producto.getId();

        defaultProductoShouldBeFound("id.equals=" + id);
        defaultProductoShouldNotBeFound("id.notEquals=" + id);

        defaultProductoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductoShouldNotBeFound("id.greaterThan=" + id);

        defaultProductoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProductosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre equals to DEFAULT_NOMBRE
        defaultProductoShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the productoList where nombre equals to UPDATED_NOMBRE
        defaultProductoShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllProductosByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre not equals to DEFAULT_NOMBRE
        defaultProductoShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the productoList where nombre not equals to UPDATED_NOMBRE
        defaultProductoShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllProductosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultProductoShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the productoList where nombre equals to UPDATED_NOMBRE
        defaultProductoShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllProductosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre is not null
        defaultProductoShouldBeFound("nombre.specified=true");

        // Get all the productoList where nombre is null
        defaultProductoShouldNotBeFound("nombre.specified=false");
    }
                @Test
    @Transactional
    public void getAllProductosByNombreContainsSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre contains DEFAULT_NOMBRE
        defaultProductoShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the productoList where nombre contains UPDATED_NOMBRE
        defaultProductoShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllProductosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre does not contain DEFAULT_NOMBRE
        defaultProductoShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the productoList where nombre does not contain UPDATED_NOMBRE
        defaultProductoShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }


    @Test
    @Transactional
    public void getAllProductosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where descripcion equals to DEFAULT_DESCRIPCION
        defaultProductoShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the productoList where descripcion equals to UPDATED_DESCRIPCION
        defaultProductoShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllProductosByDescripcionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where descripcion not equals to DEFAULT_DESCRIPCION
        defaultProductoShouldNotBeFound("descripcion.notEquals=" + DEFAULT_DESCRIPCION);

        // Get all the productoList where descripcion not equals to UPDATED_DESCRIPCION
        defaultProductoShouldBeFound("descripcion.notEquals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllProductosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultProductoShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the productoList where descripcion equals to UPDATED_DESCRIPCION
        defaultProductoShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllProductosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where descripcion is not null
        defaultProductoShouldBeFound("descripcion.specified=true");

        // Get all the productoList where descripcion is null
        defaultProductoShouldNotBeFound("descripcion.specified=false");
    }
                @Test
    @Transactional
    public void getAllProductosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where descripcion contains DEFAULT_DESCRIPCION
        defaultProductoShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the productoList where descripcion contains UPDATED_DESCRIPCION
        defaultProductoShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllProductosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultProductoShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the productoList where descripcion does not contain UPDATED_DESCRIPCION
        defaultProductoShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }


    @Test
    @Transactional
    public void getAllProductosByPrecioCompraIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioCompra equals to DEFAULT_PRECIO_COMPRA
        defaultProductoShouldBeFound("precioCompra.equals=" + DEFAULT_PRECIO_COMPRA);

        // Get all the productoList where precioCompra equals to UPDATED_PRECIO_COMPRA
        defaultProductoShouldNotBeFound("precioCompra.equals=" + UPDATED_PRECIO_COMPRA);
    }

    @Test
    @Transactional
    public void getAllProductosByPrecioCompraIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioCompra not equals to DEFAULT_PRECIO_COMPRA
        defaultProductoShouldNotBeFound("precioCompra.notEquals=" + DEFAULT_PRECIO_COMPRA);

        // Get all the productoList where precioCompra not equals to UPDATED_PRECIO_COMPRA
        defaultProductoShouldBeFound("precioCompra.notEquals=" + UPDATED_PRECIO_COMPRA);
    }

    @Test
    @Transactional
    public void getAllProductosByPrecioCompraIsInShouldWork() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioCompra in DEFAULT_PRECIO_COMPRA or UPDATED_PRECIO_COMPRA
        defaultProductoShouldBeFound("precioCompra.in=" + DEFAULT_PRECIO_COMPRA + "," + UPDATED_PRECIO_COMPRA);

        // Get all the productoList where precioCompra equals to UPDATED_PRECIO_COMPRA
        defaultProductoShouldNotBeFound("precioCompra.in=" + UPDATED_PRECIO_COMPRA);
    }

    @Test
    @Transactional
    public void getAllProductosByPrecioCompraIsNullOrNotNull() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioCompra is not null
        defaultProductoShouldBeFound("precioCompra.specified=true");

        // Get all the productoList where precioCompra is null
        defaultProductoShouldNotBeFound("precioCompra.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductosByPrecioCompraIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioCompra is greater than or equal to DEFAULT_PRECIO_COMPRA
        defaultProductoShouldBeFound("precioCompra.greaterThanOrEqual=" + DEFAULT_PRECIO_COMPRA);

        // Get all the productoList where precioCompra is greater than or equal to UPDATED_PRECIO_COMPRA
        defaultProductoShouldNotBeFound("precioCompra.greaterThanOrEqual=" + UPDATED_PRECIO_COMPRA);
    }

    @Test
    @Transactional
    public void getAllProductosByPrecioCompraIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioCompra is less than or equal to DEFAULT_PRECIO_COMPRA
        defaultProductoShouldBeFound("precioCompra.lessThanOrEqual=" + DEFAULT_PRECIO_COMPRA);

        // Get all the productoList where precioCompra is less than or equal to SMALLER_PRECIO_COMPRA
        defaultProductoShouldNotBeFound("precioCompra.lessThanOrEqual=" + SMALLER_PRECIO_COMPRA);
    }

    @Test
    @Transactional
    public void getAllProductosByPrecioCompraIsLessThanSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioCompra is less than DEFAULT_PRECIO_COMPRA
        defaultProductoShouldNotBeFound("precioCompra.lessThan=" + DEFAULT_PRECIO_COMPRA);

        // Get all the productoList where precioCompra is less than UPDATED_PRECIO_COMPRA
        defaultProductoShouldBeFound("precioCompra.lessThan=" + UPDATED_PRECIO_COMPRA);
    }

    @Test
    @Transactional
    public void getAllProductosByPrecioCompraIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioCompra is greater than DEFAULT_PRECIO_COMPRA
        defaultProductoShouldNotBeFound("precioCompra.greaterThan=" + DEFAULT_PRECIO_COMPRA);

        // Get all the productoList where precioCompra is greater than SMALLER_PRECIO_COMPRA
        defaultProductoShouldBeFound("precioCompra.greaterThan=" + SMALLER_PRECIO_COMPRA);
    }


    @Test
    @Transactional
    public void getAllProductosByPrecioVentaIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioVenta equals to DEFAULT_PRECIO_VENTA
        defaultProductoShouldBeFound("precioVenta.equals=" + DEFAULT_PRECIO_VENTA);

        // Get all the productoList where precioVenta equals to UPDATED_PRECIO_VENTA
        defaultProductoShouldNotBeFound("precioVenta.equals=" + UPDATED_PRECIO_VENTA);
    }

    @Test
    @Transactional
    public void getAllProductosByPrecioVentaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioVenta not equals to DEFAULT_PRECIO_VENTA
        defaultProductoShouldNotBeFound("precioVenta.notEquals=" + DEFAULT_PRECIO_VENTA);

        // Get all the productoList where precioVenta not equals to UPDATED_PRECIO_VENTA
        defaultProductoShouldBeFound("precioVenta.notEquals=" + UPDATED_PRECIO_VENTA);
    }

    @Test
    @Transactional
    public void getAllProductosByPrecioVentaIsInShouldWork() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioVenta in DEFAULT_PRECIO_VENTA or UPDATED_PRECIO_VENTA
        defaultProductoShouldBeFound("precioVenta.in=" + DEFAULT_PRECIO_VENTA + "," + UPDATED_PRECIO_VENTA);

        // Get all the productoList where precioVenta equals to UPDATED_PRECIO_VENTA
        defaultProductoShouldNotBeFound("precioVenta.in=" + UPDATED_PRECIO_VENTA);
    }

    @Test
    @Transactional
    public void getAllProductosByPrecioVentaIsNullOrNotNull() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioVenta is not null
        defaultProductoShouldBeFound("precioVenta.specified=true");

        // Get all the productoList where precioVenta is null
        defaultProductoShouldNotBeFound("precioVenta.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductosByPrecioVentaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioVenta is greater than or equal to DEFAULT_PRECIO_VENTA
        defaultProductoShouldBeFound("precioVenta.greaterThanOrEqual=" + DEFAULT_PRECIO_VENTA);

        // Get all the productoList where precioVenta is greater than or equal to UPDATED_PRECIO_VENTA
        defaultProductoShouldNotBeFound("precioVenta.greaterThanOrEqual=" + UPDATED_PRECIO_VENTA);
    }

    @Test
    @Transactional
    public void getAllProductosByPrecioVentaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioVenta is less than or equal to DEFAULT_PRECIO_VENTA
        defaultProductoShouldBeFound("precioVenta.lessThanOrEqual=" + DEFAULT_PRECIO_VENTA);

        // Get all the productoList where precioVenta is less than or equal to SMALLER_PRECIO_VENTA
        defaultProductoShouldNotBeFound("precioVenta.lessThanOrEqual=" + SMALLER_PRECIO_VENTA);
    }

    @Test
    @Transactional
    public void getAllProductosByPrecioVentaIsLessThanSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioVenta is less than DEFAULT_PRECIO_VENTA
        defaultProductoShouldNotBeFound("precioVenta.lessThan=" + DEFAULT_PRECIO_VENTA);

        // Get all the productoList where precioVenta is less than UPDATED_PRECIO_VENTA
        defaultProductoShouldBeFound("precioVenta.lessThan=" + UPDATED_PRECIO_VENTA);
    }

    @Test
    @Transactional
    public void getAllProductosByPrecioVentaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioVenta is greater than DEFAULT_PRECIO_VENTA
        defaultProductoShouldNotBeFound("precioVenta.greaterThan=" + DEFAULT_PRECIO_VENTA);

        // Get all the productoList where precioVenta is greater than SMALLER_PRECIO_VENTA
        defaultProductoShouldBeFound("precioVenta.greaterThan=" + SMALLER_PRECIO_VENTA);
    }


    @Test
    @Transactional
    public void getAllProductosByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where estado equals to DEFAULT_ESTADO
        defaultProductoShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the productoList where estado equals to UPDATED_ESTADO
        defaultProductoShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllProductosByEstadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where estado not equals to DEFAULT_ESTADO
        defaultProductoShouldNotBeFound("estado.notEquals=" + DEFAULT_ESTADO);

        // Get all the productoList where estado not equals to UPDATED_ESTADO
        defaultProductoShouldBeFound("estado.notEquals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllProductosByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultProductoShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the productoList where estado equals to UPDATED_ESTADO
        defaultProductoShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void getAllProductosByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        // Get all the productoList where estado is not null
        defaultProductoShouldBeFound("estado.specified=true");

        // Get all the productoList where estado is null
        defaultProductoShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductosByProductoDetalleIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);
        ProductoDetalle productoDetalle = ProductoDetalleResourceIT.createEntity(em);
        em.persist(productoDetalle);
        em.flush();
        producto.addProductoDetalle(productoDetalle);
        productoRepository.saveAndFlush(producto);
        Long productoDetalleId = productoDetalle.getId();

        // Get all the productoList where productoDetalle equals to productoDetalleId
        defaultProductoShouldBeFound("productoDetalleId.equals=" + productoDetalleId);

        // Get all the productoList where productoDetalle equals to productoDetalleId + 1
        defaultProductoShouldNotBeFound("productoDetalleId.equals=" + (productoDetalleId + 1));
    }


    @Test
    @Transactional
    public void getAllProductosByProductCategoriaIsEqualToSomething() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);
        ProductoCategoria productCategoria = ProductoCategoriaResourceIT.createEntity(em);
        em.persist(productCategoria);
        em.flush();
        producto.setProductCategoria(productCategoria);
        productoRepository.saveAndFlush(producto);
        Long productCategoriaId = productCategoria.getId();

        // Get all the productoList where productCategoria equals to productCategoriaId
        defaultProductoShouldBeFound("productCategoriaId.equals=" + productCategoriaId);

        // Get all the productoList where productCategoria equals to productCategoriaId + 1
        defaultProductoShouldNotBeFound("productCategoriaId.equals=" + (productCategoriaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductoShouldBeFound(String filter) throws Exception {
        restProductoMockMvc.perform(get("/api/productos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(producto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].precioCompra").value(hasItem(DEFAULT_PRECIO_COMPRA.intValue())))
            .andExpect(jsonPath("$.[*].precioVenta").value(hasItem(DEFAULT_PRECIO_VENTA.intValue())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));

        // Check, that the count call also returns 1
        restProductoMockMvc.perform(get("/api/productos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductoShouldNotBeFound(String filter) throws Exception {
        restProductoMockMvc.perform(get("/api/productos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductoMockMvc.perform(get("/api/productos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProducto() throws Exception {
        // Get the producto
        restProductoMockMvc.perform(get("/api/productos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProducto() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        int databaseSizeBeforeUpdate = productoRepository.findAll().size();

        // Update the producto
        Producto updatedProducto = productoRepository.findById(producto.getId()).get();
        // Disconnect from session so that the updates on updatedProducto are not directly saved in db
        em.detach(updatedProducto);
        updatedProducto
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .precioCompra(UPDATED_PRECIO_COMPRA)
            .precioVenta(UPDATED_PRECIO_VENTA)
            .estado(UPDATED_ESTADO);
        ProductoDTO productoDTO = productoMapper.toDto(updatedProducto);

        restProductoMockMvc.perform(put("/api/productos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productoDTO)))
            .andExpect(status().isOk());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
        Producto testProducto = productoList.get(productoList.size() - 1);
        assertThat(testProducto.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testProducto.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testProducto.getPrecioCompra()).isEqualTo(UPDATED_PRECIO_COMPRA);
        assertThat(testProducto.getPrecioVenta()).isEqualTo(UPDATED_PRECIO_VENTA);
        assertThat(testProducto.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    public void updateNonExistingProducto() throws Exception {
        int databaseSizeBeforeUpdate = productoRepository.findAll().size();

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoMockMvc.perform(put("/api/productos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProducto() throws Exception {
        // Initialize the database
        productoRepository.saveAndFlush(producto);

        int databaseSizeBeforeDelete = productoRepository.findAll().size();

        // Delete the producto
        restProductoMockMvc.perform(delete("/api/productos/{id}", producto.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Producto> productoList = productoRepository.findAll();
        assertThat(productoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
