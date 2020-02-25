package com.themakers.mitienda.web.rest;

import com.themakers.mitienda.MitiendaApp;
import com.themakers.mitienda.domain.TipoLista;
import com.themakers.mitienda.domain.TipoLista;
import com.themakers.mitienda.repository.TipoListaRepository;
import com.themakers.mitienda.service.TipoListaService;
import com.themakers.mitienda.service.dto.TipoListaDTO;
import com.themakers.mitienda.service.mapper.TipoListaMapper;
import com.themakers.mitienda.web.rest.errors.ExceptionTranslator;
import com.themakers.mitienda.service.dto.TipoListaCriteria;
import com.themakers.mitienda.service.TipoListaQueryService;

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
 * Integration tests for the {@link TipoListaResource} REST controller.
 */
@SpringBootTest(classes = MitiendaApp.class)
public class TipoListaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private TipoListaRepository tipoListaRepository;

    @Autowired
    private TipoListaMapper tipoListaMapper;

    @Autowired
    private TipoListaService tipoListaService;

    @Autowired
    private TipoListaQueryService tipoListaQueryService;

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

    private MockMvc restTipoListaMockMvc;

    private TipoLista tipoLista;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TipoListaResource tipoListaResource = new TipoListaResource(tipoListaService, tipoListaQueryService);
        this.restTipoListaMockMvc = MockMvcBuilders.standaloneSetup(tipoListaResource)
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
    public static TipoLista createEntity(EntityManager em) {
        TipoLista tipoLista = new TipoLista()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION);
        return tipoLista;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoLista createUpdatedEntity(EntityManager em) {
        TipoLista tipoLista = new TipoLista()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION);
        return tipoLista;
    }

    @BeforeEach
    public void initTest() {
        tipoLista = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipoLista() throws Exception {
        int databaseSizeBeforeCreate = tipoListaRepository.findAll().size();

        // Create the TipoLista
        TipoListaDTO tipoListaDTO = tipoListaMapper.toDto(tipoLista);
        restTipoListaMockMvc.perform(post("/api/tipo-listas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoListaDTO)))
            .andExpect(status().isCreated());

        // Validate the TipoLista in the database
        List<TipoLista> tipoListaList = tipoListaRepository.findAll();
        assertThat(tipoListaList).hasSize(databaseSizeBeforeCreate + 1);
        TipoLista testTipoLista = tipoListaList.get(tipoListaList.size() - 1);
        assertThat(testTipoLista.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testTipoLista.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createTipoListaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipoListaRepository.findAll().size();

        // Create the TipoLista with an existing ID
        tipoLista.setId(1L);
        TipoListaDTO tipoListaDTO = tipoListaMapper.toDto(tipoLista);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoListaMockMvc.perform(post("/api/tipo-listas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoListaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoLista in the database
        List<TipoLista> tipoListaList = tipoListaRepository.findAll();
        assertThat(tipoListaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoListaRepository.findAll().size();
        // set the field null
        tipoLista.setNombre(null);

        // Create the TipoLista, which fails.
        TipoListaDTO tipoListaDTO = tipoListaMapper.toDto(tipoLista);

        restTipoListaMockMvc.perform(post("/api/tipo-listas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoListaDTO)))
            .andExpect(status().isBadRequest());

        List<TipoLista> tipoListaList = tipoListaRepository.findAll();
        assertThat(tipoListaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipoListas() throws Exception {
        // Initialize the database
        tipoListaRepository.saveAndFlush(tipoLista);

        // Get all the tipoListaList
        restTipoListaMockMvc.perform(get("/api/tipo-listas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoLista.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }
    
    @Test
    @Transactional
    public void getTipoLista() throws Exception {
        // Initialize the database
        tipoListaRepository.saveAndFlush(tipoLista);

        // Get the tipoLista
        restTipoListaMockMvc.perform(get("/api/tipo-listas/{id}", tipoLista.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipoLista.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }


    @Test
    @Transactional
    public void getTipoListasByIdFiltering() throws Exception {
        // Initialize the database
        tipoListaRepository.saveAndFlush(tipoLista);

        Long id = tipoLista.getId();

        defaultTipoListaShouldBeFound("id.equals=" + id);
        defaultTipoListaShouldNotBeFound("id.notEquals=" + id);

        defaultTipoListaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTipoListaShouldNotBeFound("id.greaterThan=" + id);

        defaultTipoListaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTipoListaShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTipoListasByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        tipoListaRepository.saveAndFlush(tipoLista);

        // Get all the tipoListaList where nombre equals to DEFAULT_NOMBRE
        defaultTipoListaShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the tipoListaList where nombre equals to UPDATED_NOMBRE
        defaultTipoListaShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllTipoListasByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tipoListaRepository.saveAndFlush(tipoLista);

        // Get all the tipoListaList where nombre not equals to DEFAULT_NOMBRE
        defaultTipoListaShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the tipoListaList where nombre not equals to UPDATED_NOMBRE
        defaultTipoListaShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllTipoListasByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        tipoListaRepository.saveAndFlush(tipoLista);

        // Get all the tipoListaList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultTipoListaShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the tipoListaList where nombre equals to UPDATED_NOMBRE
        defaultTipoListaShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllTipoListasByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        tipoListaRepository.saveAndFlush(tipoLista);

        // Get all the tipoListaList where nombre is not null
        defaultTipoListaShouldBeFound("nombre.specified=true");

        // Get all the tipoListaList where nombre is null
        defaultTipoListaShouldNotBeFound("nombre.specified=false");
    }
                @Test
    @Transactional
    public void getAllTipoListasByNombreContainsSomething() throws Exception {
        // Initialize the database
        tipoListaRepository.saveAndFlush(tipoLista);

        // Get all the tipoListaList where nombre contains DEFAULT_NOMBRE
        defaultTipoListaShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the tipoListaList where nombre contains UPDATED_NOMBRE
        defaultTipoListaShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllTipoListasByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        tipoListaRepository.saveAndFlush(tipoLista);

        // Get all the tipoListaList where nombre does not contain DEFAULT_NOMBRE
        defaultTipoListaShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the tipoListaList where nombre does not contain UPDATED_NOMBRE
        defaultTipoListaShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }


    @Test
    @Transactional
    public void getAllTipoListasByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        tipoListaRepository.saveAndFlush(tipoLista);

        // Get all the tipoListaList where descripcion equals to DEFAULT_DESCRIPCION
        defaultTipoListaShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the tipoListaList where descripcion equals to UPDATED_DESCRIPCION
        defaultTipoListaShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllTipoListasByDescripcionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tipoListaRepository.saveAndFlush(tipoLista);

        // Get all the tipoListaList where descripcion not equals to DEFAULT_DESCRIPCION
        defaultTipoListaShouldNotBeFound("descripcion.notEquals=" + DEFAULT_DESCRIPCION);

        // Get all the tipoListaList where descripcion not equals to UPDATED_DESCRIPCION
        defaultTipoListaShouldBeFound("descripcion.notEquals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllTipoListasByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        tipoListaRepository.saveAndFlush(tipoLista);

        // Get all the tipoListaList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultTipoListaShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the tipoListaList where descripcion equals to UPDATED_DESCRIPCION
        defaultTipoListaShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllTipoListasByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        tipoListaRepository.saveAndFlush(tipoLista);

        // Get all the tipoListaList where descripcion is not null
        defaultTipoListaShouldBeFound("descripcion.specified=true");

        // Get all the tipoListaList where descripcion is null
        defaultTipoListaShouldNotBeFound("descripcion.specified=false");
    }
                @Test
    @Transactional
    public void getAllTipoListasByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        tipoListaRepository.saveAndFlush(tipoLista);

        // Get all the tipoListaList where descripcion contains DEFAULT_DESCRIPCION
        defaultTipoListaShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the tipoListaList where descripcion contains UPDATED_DESCRIPCION
        defaultTipoListaShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllTipoListasByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        tipoListaRepository.saveAndFlush(tipoLista);

        // Get all the tipoListaList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultTipoListaShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the tipoListaList where descripcion does not contain UPDATED_DESCRIPCION
        defaultTipoListaShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }


    @Test
    @Transactional
    public void getAllTipoListasByPadreIsEqualToSomething() throws Exception {
        // Initialize the database
        tipoListaRepository.saveAndFlush(tipoLista);
        TipoLista padre = TipoListaResourceIT.createEntity(em);
        em.persist(padre);
        em.flush();
        tipoLista.setPadre(padre);
        tipoListaRepository.saveAndFlush(tipoLista);
        Long padreId = padre.getId();

        // Get all the tipoListaList where padre equals to padreId
        defaultTipoListaShouldBeFound("padreId.equals=" + padreId);

        // Get all the tipoListaList where padre equals to padreId + 1
        defaultTipoListaShouldNotBeFound("padreId.equals=" + (padreId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTipoListaShouldBeFound(String filter) throws Exception {
        restTipoListaMockMvc.perform(get("/api/tipo-listas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoLista.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));

        // Check, that the count call also returns 1
        restTipoListaMockMvc.perform(get("/api/tipo-listas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTipoListaShouldNotBeFound(String filter) throws Exception {
        restTipoListaMockMvc.perform(get("/api/tipo-listas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTipoListaMockMvc.perform(get("/api/tipo-listas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTipoLista() throws Exception {
        // Get the tipoLista
        restTipoListaMockMvc.perform(get("/api/tipo-listas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipoLista() throws Exception {
        // Initialize the database
        tipoListaRepository.saveAndFlush(tipoLista);

        int databaseSizeBeforeUpdate = tipoListaRepository.findAll().size();

        // Update the tipoLista
        TipoLista updatedTipoLista = tipoListaRepository.findById(tipoLista.getId()).get();
        // Disconnect from session so that the updates on updatedTipoLista are not directly saved in db
        em.detach(updatedTipoLista);
        updatedTipoLista
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION);
        TipoListaDTO tipoListaDTO = tipoListaMapper.toDto(updatedTipoLista);

        restTipoListaMockMvc.perform(put("/api/tipo-listas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoListaDTO)))
            .andExpect(status().isOk());

        // Validate the TipoLista in the database
        List<TipoLista> tipoListaList = tipoListaRepository.findAll();
        assertThat(tipoListaList).hasSize(databaseSizeBeforeUpdate);
        TipoLista testTipoLista = tipoListaList.get(tipoListaList.size() - 1);
        assertThat(testTipoLista.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testTipoLista.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void updateNonExistingTipoLista() throws Exception {
        int databaseSizeBeforeUpdate = tipoListaRepository.findAll().size();

        // Create the TipoLista
        TipoListaDTO tipoListaDTO = tipoListaMapper.toDto(tipoLista);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoListaMockMvc.perform(put("/api/tipo-listas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoListaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoLista in the database
        List<TipoLista> tipoListaList = tipoListaRepository.findAll();
        assertThat(tipoListaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTipoLista() throws Exception {
        // Initialize the database
        tipoListaRepository.saveAndFlush(tipoLista);

        int databaseSizeBeforeDelete = tipoListaRepository.findAll().size();

        // Delete the tipoLista
        restTipoListaMockMvc.perform(delete("/api/tipo-listas/{id}", tipoLista.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TipoLista> tipoListaList = tipoListaRepository.findAll();
        assertThat(tipoListaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
