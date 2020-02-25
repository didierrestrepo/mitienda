package com.themakers.mitienda.web.rest;

import com.themakers.mitienda.MitiendaApp;
import com.themakers.mitienda.domain.Sucursal;
import com.themakers.mitienda.repository.SucursalRepository;
import com.themakers.mitienda.service.SucursalService;
import com.themakers.mitienda.service.dto.SucursalDTO;
import com.themakers.mitienda.service.mapper.SucursalMapper;
import com.themakers.mitienda.web.rest.errors.ExceptionTranslator;
import com.themakers.mitienda.service.dto.SucursalCriteria;
import com.themakers.mitienda.service.SucursalQueryService;

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
 * Integration tests for the {@link SucursalResource} REST controller.
 */
@SpringBootTest(classes = MitiendaApp.class)
public class SucursalResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private SucursalMapper sucursalMapper;

    @Autowired
    private SucursalService sucursalService;

    @Autowired
    private SucursalQueryService sucursalQueryService;

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

    private MockMvc restSucursalMockMvc;

    private Sucursal sucursal;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SucursalResource sucursalResource = new SucursalResource(sucursalService, sucursalQueryService);
        this.restSucursalMockMvc = MockMvcBuilders.standaloneSetup(sucursalResource)
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
    public static Sucursal createEntity(EntityManager em) {
        Sucursal sucursal = new Sucursal()
            .nombre(DEFAULT_NOMBRE)
            .direccion(DEFAULT_DIRECCION);
        return sucursal;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sucursal createUpdatedEntity(EntityManager em) {
        Sucursal sucursal = new Sucursal()
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION);
        return sucursal;
    }

    @BeforeEach
    public void initTest() {
        sucursal = createEntity(em);
    }

    @Test
    @Transactional
    public void createSucursal() throws Exception {
        int databaseSizeBeforeCreate = sucursalRepository.findAll().size();

        // Create the Sucursal
        SucursalDTO sucursalDTO = sucursalMapper.toDto(sucursal);
        restSucursalMockMvc.perform(post("/api/sucursals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sucursalDTO)))
            .andExpect(status().isCreated());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeCreate + 1);
        Sucursal testSucursal = sucursalList.get(sucursalList.size() - 1);
        assertThat(testSucursal.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testSucursal.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
    }

    @Test
    @Transactional
    public void createSucursalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sucursalRepository.findAll().size();

        // Create the Sucursal with an existing ID
        sucursal.setId(1L);
        SucursalDTO sucursalDTO = sucursalMapper.toDto(sucursal);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSucursalMockMvc.perform(post("/api/sucursals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sucursalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = sucursalRepository.findAll().size();
        // set the field null
        sucursal.setNombre(null);

        // Create the Sucursal, which fails.
        SucursalDTO sucursalDTO = sucursalMapper.toDto(sucursal);

        restSucursalMockMvc.perform(post("/api/sucursals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sucursalDTO)))
            .andExpect(status().isBadRequest());

        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDireccionIsRequired() throws Exception {
        int databaseSizeBeforeTest = sucursalRepository.findAll().size();
        // set the field null
        sucursal.setDireccion(null);

        // Create the Sucursal, which fails.
        SucursalDTO sucursalDTO = sucursalMapper.toDto(sucursal);

        restSucursalMockMvc.perform(post("/api/sucursals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sucursalDTO)))
            .andExpect(status().isBadRequest());

        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSucursals() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList
        restSucursalMockMvc.perform(get("/api/sucursals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sucursal.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)));
    }
    
    @Test
    @Transactional
    public void getSucursal() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get the sucursal
        restSucursalMockMvc.perform(get("/api/sucursals/{id}", sucursal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sucursal.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION));
    }


    @Test
    @Transactional
    public void getSucursalsByIdFiltering() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        Long id = sucursal.getId();

        defaultSucursalShouldBeFound("id.equals=" + id);
        defaultSucursalShouldNotBeFound("id.notEquals=" + id);

        defaultSucursalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSucursalShouldNotBeFound("id.greaterThan=" + id);

        defaultSucursalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSucursalShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSucursalsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where nombre equals to DEFAULT_NOMBRE
        defaultSucursalShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the sucursalList where nombre equals to UPDATED_NOMBRE
        defaultSucursalShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllSucursalsByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where nombre not equals to DEFAULT_NOMBRE
        defaultSucursalShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the sucursalList where nombre not equals to UPDATED_NOMBRE
        defaultSucursalShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllSucursalsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultSucursalShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the sucursalList where nombre equals to UPDATED_NOMBRE
        defaultSucursalShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllSucursalsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where nombre is not null
        defaultSucursalShouldBeFound("nombre.specified=true");

        // Get all the sucursalList where nombre is null
        defaultSucursalShouldNotBeFound("nombre.specified=false");
    }
                @Test
    @Transactional
    public void getAllSucursalsByNombreContainsSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where nombre contains DEFAULT_NOMBRE
        defaultSucursalShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the sucursalList where nombre contains UPDATED_NOMBRE
        defaultSucursalShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllSucursalsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where nombre does not contain DEFAULT_NOMBRE
        defaultSucursalShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the sucursalList where nombre does not contain UPDATED_NOMBRE
        defaultSucursalShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }


    @Test
    @Transactional
    public void getAllSucursalsByDireccionIsEqualToSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where direccion equals to DEFAULT_DIRECCION
        defaultSucursalShouldBeFound("direccion.equals=" + DEFAULT_DIRECCION);

        // Get all the sucursalList where direccion equals to UPDATED_DIRECCION
        defaultSucursalShouldNotBeFound("direccion.equals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    public void getAllSucursalsByDireccionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where direccion not equals to DEFAULT_DIRECCION
        defaultSucursalShouldNotBeFound("direccion.notEquals=" + DEFAULT_DIRECCION);

        // Get all the sucursalList where direccion not equals to UPDATED_DIRECCION
        defaultSucursalShouldBeFound("direccion.notEquals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    public void getAllSucursalsByDireccionIsInShouldWork() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where direccion in DEFAULT_DIRECCION or UPDATED_DIRECCION
        defaultSucursalShouldBeFound("direccion.in=" + DEFAULT_DIRECCION + "," + UPDATED_DIRECCION);

        // Get all the sucursalList where direccion equals to UPDATED_DIRECCION
        defaultSucursalShouldNotBeFound("direccion.in=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    public void getAllSucursalsByDireccionIsNullOrNotNull() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where direccion is not null
        defaultSucursalShouldBeFound("direccion.specified=true");

        // Get all the sucursalList where direccion is null
        defaultSucursalShouldNotBeFound("direccion.specified=false");
    }
                @Test
    @Transactional
    public void getAllSucursalsByDireccionContainsSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where direccion contains DEFAULT_DIRECCION
        defaultSucursalShouldBeFound("direccion.contains=" + DEFAULT_DIRECCION);

        // Get all the sucursalList where direccion contains UPDATED_DIRECCION
        defaultSucursalShouldNotBeFound("direccion.contains=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    public void getAllSucursalsByDireccionNotContainsSomething() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        // Get all the sucursalList where direccion does not contain DEFAULT_DIRECCION
        defaultSucursalShouldNotBeFound("direccion.doesNotContain=" + DEFAULT_DIRECCION);

        // Get all the sucursalList where direccion does not contain UPDATED_DIRECCION
        defaultSucursalShouldBeFound("direccion.doesNotContain=" + UPDATED_DIRECCION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSucursalShouldBeFound(String filter) throws Exception {
        restSucursalMockMvc.perform(get("/api/sucursals?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sucursal.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)));

        // Check, that the count call also returns 1
        restSucursalMockMvc.perform(get("/api/sucursals/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSucursalShouldNotBeFound(String filter) throws Exception {
        restSucursalMockMvc.perform(get("/api/sucursals?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSucursalMockMvc.perform(get("/api/sucursals/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSucursal() throws Exception {
        // Get the sucursal
        restSucursalMockMvc.perform(get("/api/sucursals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSucursal() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        int databaseSizeBeforeUpdate = sucursalRepository.findAll().size();

        // Update the sucursal
        Sucursal updatedSucursal = sucursalRepository.findById(sucursal.getId()).get();
        // Disconnect from session so that the updates on updatedSucursal are not directly saved in db
        em.detach(updatedSucursal);
        updatedSucursal
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION);
        SucursalDTO sucursalDTO = sucursalMapper.toDto(updatedSucursal);

        restSucursalMockMvc.perform(put("/api/sucursals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sucursalDTO)))
            .andExpect(status().isOk());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeUpdate);
        Sucursal testSucursal = sucursalList.get(sucursalList.size() - 1);
        assertThat(testSucursal.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testSucursal.getDireccion()).isEqualTo(UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    public void updateNonExistingSucursal() throws Exception {
        int databaseSizeBeforeUpdate = sucursalRepository.findAll().size();

        // Create the Sucursal
        SucursalDTO sucursalDTO = sucursalMapper.toDto(sucursal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSucursalMockMvc.perform(put("/api/sucursals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sucursalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sucursal in the database
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSucursal() throws Exception {
        // Initialize the database
        sucursalRepository.saveAndFlush(sucursal);

        int databaseSizeBeforeDelete = sucursalRepository.findAll().size();

        // Delete the sucursal
        restSucursalMockMvc.perform(delete("/api/sucursals/{id}", sucursal.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sucursal> sucursalList = sucursalRepository.findAll();
        assertThat(sucursalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
