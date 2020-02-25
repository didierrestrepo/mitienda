package com.themakers.mitienda.web.rest;

import com.themakers.mitienda.MitiendaApp;
import com.themakers.mitienda.domain.ElementoLista;
import com.themakers.mitienda.domain.TipoLista;
import com.themakers.mitienda.domain.ElementoLista;
import com.themakers.mitienda.repository.ElementoListaRepository;
import com.themakers.mitienda.service.ElementoListaService;
import com.themakers.mitienda.service.dto.ElementoListaDTO;
import com.themakers.mitienda.service.mapper.ElementoListaMapper;
import com.themakers.mitienda.web.rest.errors.ExceptionTranslator;
import com.themakers.mitienda.service.dto.ElementoListaCriteria;
import com.themakers.mitienda.service.ElementoListaQueryService;

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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.themakers.mitienda.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ElementoListaResource} REST controller.
 */
@SpringBootTest(classes = MitiendaApp.class)
public class ElementoListaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAA";
    private static final String UPDATED_CODIGO = "BBBBB";

    private static final byte[] DEFAULT_PROPIEDADES = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PROPIEDADES = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PROPIEDADES_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PROPIEDADES_CONTENT_TYPE = "image/png";

    @Autowired
    private ElementoListaRepository elementoListaRepository;

    @Autowired
    private ElementoListaMapper elementoListaMapper;

    @Autowired
    private ElementoListaService elementoListaService;

    @Autowired
    private ElementoListaQueryService elementoListaQueryService;

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

    private MockMvc restElementoListaMockMvc;

    private ElementoLista elementoLista;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ElementoListaResource elementoListaResource = new ElementoListaResource(elementoListaService, elementoListaQueryService);
        this.restElementoListaMockMvc = MockMvcBuilders.standaloneSetup(elementoListaResource)
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
    public static ElementoLista createEntity(EntityManager em) {
        ElementoLista elementoLista = new ElementoLista()
            .nombre(DEFAULT_NOMBRE)
            .codigo(DEFAULT_CODIGO)
            .propiedades(DEFAULT_PROPIEDADES)
            .propiedadesContentType(DEFAULT_PROPIEDADES_CONTENT_TYPE);
        // Add required entity
        TipoLista tipoLista;
        if (TestUtil.findAll(em, TipoLista.class).isEmpty()) {
            tipoLista = TipoListaResourceIT.createEntity(em);
            em.persist(tipoLista);
            em.flush();
        } else {
            tipoLista = TestUtil.findAll(em, TipoLista.class).get(0);
        }
        elementoLista.setTipoLista(tipoLista);
        return elementoLista;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ElementoLista createUpdatedEntity(EntityManager em) {
        ElementoLista elementoLista = new ElementoLista()
            .nombre(UPDATED_NOMBRE)
            .codigo(UPDATED_CODIGO)
            .propiedades(UPDATED_PROPIEDADES)
            .propiedadesContentType(UPDATED_PROPIEDADES_CONTENT_TYPE);
        // Add required entity
        TipoLista tipoLista;
        if (TestUtil.findAll(em, TipoLista.class).isEmpty()) {
            tipoLista = TipoListaResourceIT.createUpdatedEntity(em);
            em.persist(tipoLista);
            em.flush();
        } else {
            tipoLista = TestUtil.findAll(em, TipoLista.class).get(0);
        }
        elementoLista.setTipoLista(tipoLista);
        return elementoLista;
    }

    @BeforeEach
    public void initTest() {
        elementoLista = createEntity(em);
    }

    @Test
    @Transactional
    public void createElementoLista() throws Exception {
        int databaseSizeBeforeCreate = elementoListaRepository.findAll().size();

        // Create the ElementoLista
        ElementoListaDTO elementoListaDTO = elementoListaMapper.toDto(elementoLista);
        restElementoListaMockMvc.perform(post("/api/elemento-listas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(elementoListaDTO)))
            .andExpect(status().isCreated());

        // Validate the ElementoLista in the database
        List<ElementoLista> elementoListaList = elementoListaRepository.findAll();
        assertThat(elementoListaList).hasSize(databaseSizeBeforeCreate + 1);
        ElementoLista testElementoLista = elementoListaList.get(elementoListaList.size() - 1);
        assertThat(testElementoLista.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testElementoLista.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testElementoLista.getPropiedades()).isEqualTo(DEFAULT_PROPIEDADES);
        assertThat(testElementoLista.getPropiedadesContentType()).isEqualTo(DEFAULT_PROPIEDADES_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createElementoListaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = elementoListaRepository.findAll().size();

        // Create the ElementoLista with an existing ID
        elementoLista.setId(1L);
        ElementoListaDTO elementoListaDTO = elementoListaMapper.toDto(elementoLista);

        // An entity with an existing ID cannot be created, so this API call must fail
        restElementoListaMockMvc.perform(post("/api/elemento-listas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(elementoListaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ElementoLista in the database
        List<ElementoLista> elementoListaList = elementoListaRepository.findAll();
        assertThat(elementoListaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = elementoListaRepository.findAll().size();
        // set the field null
        elementoLista.setNombre(null);

        // Create the ElementoLista, which fails.
        ElementoListaDTO elementoListaDTO = elementoListaMapper.toDto(elementoLista);

        restElementoListaMockMvc.perform(post("/api/elemento-listas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(elementoListaDTO)))
            .andExpect(status().isBadRequest());

        List<ElementoLista> elementoListaList = elementoListaRepository.findAll();
        assertThat(elementoListaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = elementoListaRepository.findAll().size();
        // set the field null
        elementoLista.setCodigo(null);

        // Create the ElementoLista, which fails.
        ElementoListaDTO elementoListaDTO = elementoListaMapper.toDto(elementoLista);

        restElementoListaMockMvc.perform(post("/api/elemento-listas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(elementoListaDTO)))
            .andExpect(status().isBadRequest());

        List<ElementoLista> elementoListaList = elementoListaRepository.findAll();
        assertThat(elementoListaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllElementoListas() throws Exception {
        // Initialize the database
        elementoListaRepository.saveAndFlush(elementoLista);

        // Get all the elementoListaList
        restElementoListaMockMvc.perform(get("/api/elemento-listas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(elementoLista.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].propiedadesContentType").value(hasItem(DEFAULT_PROPIEDADES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].propiedades").value(hasItem(Base64Utils.encodeToString(DEFAULT_PROPIEDADES))));
    }
    
    @Test
    @Transactional
    public void getElementoLista() throws Exception {
        // Initialize the database
        elementoListaRepository.saveAndFlush(elementoLista);

        // Get the elementoLista
        restElementoListaMockMvc.perform(get("/api/elemento-listas/{id}", elementoLista.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(elementoLista.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.propiedadesContentType").value(DEFAULT_PROPIEDADES_CONTENT_TYPE))
            .andExpect(jsonPath("$.propiedades").value(Base64Utils.encodeToString(DEFAULT_PROPIEDADES)));
    }


    @Test
    @Transactional
    public void getElementoListasByIdFiltering() throws Exception {
        // Initialize the database
        elementoListaRepository.saveAndFlush(elementoLista);

        Long id = elementoLista.getId();

        defaultElementoListaShouldBeFound("id.equals=" + id);
        defaultElementoListaShouldNotBeFound("id.notEquals=" + id);

        defaultElementoListaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultElementoListaShouldNotBeFound("id.greaterThan=" + id);

        defaultElementoListaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultElementoListaShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllElementoListasByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        elementoListaRepository.saveAndFlush(elementoLista);

        // Get all the elementoListaList where nombre equals to DEFAULT_NOMBRE
        defaultElementoListaShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the elementoListaList where nombre equals to UPDATED_NOMBRE
        defaultElementoListaShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllElementoListasByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        elementoListaRepository.saveAndFlush(elementoLista);

        // Get all the elementoListaList where nombre not equals to DEFAULT_NOMBRE
        defaultElementoListaShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the elementoListaList where nombre not equals to UPDATED_NOMBRE
        defaultElementoListaShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllElementoListasByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        elementoListaRepository.saveAndFlush(elementoLista);

        // Get all the elementoListaList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultElementoListaShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the elementoListaList where nombre equals to UPDATED_NOMBRE
        defaultElementoListaShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllElementoListasByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        elementoListaRepository.saveAndFlush(elementoLista);

        // Get all the elementoListaList where nombre is not null
        defaultElementoListaShouldBeFound("nombre.specified=true");

        // Get all the elementoListaList where nombre is null
        defaultElementoListaShouldNotBeFound("nombre.specified=false");
    }
                @Test
    @Transactional
    public void getAllElementoListasByNombreContainsSomething() throws Exception {
        // Initialize the database
        elementoListaRepository.saveAndFlush(elementoLista);

        // Get all the elementoListaList where nombre contains DEFAULT_NOMBRE
        defaultElementoListaShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the elementoListaList where nombre contains UPDATED_NOMBRE
        defaultElementoListaShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllElementoListasByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        elementoListaRepository.saveAndFlush(elementoLista);

        // Get all the elementoListaList where nombre does not contain DEFAULT_NOMBRE
        defaultElementoListaShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the elementoListaList where nombre does not contain UPDATED_NOMBRE
        defaultElementoListaShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }


    @Test
    @Transactional
    public void getAllElementoListasByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        elementoListaRepository.saveAndFlush(elementoLista);

        // Get all the elementoListaList where codigo equals to DEFAULT_CODIGO
        defaultElementoListaShouldBeFound("codigo.equals=" + DEFAULT_CODIGO);

        // Get all the elementoListaList where codigo equals to UPDATED_CODIGO
        defaultElementoListaShouldNotBeFound("codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllElementoListasByCodigoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        elementoListaRepository.saveAndFlush(elementoLista);

        // Get all the elementoListaList where codigo not equals to DEFAULT_CODIGO
        defaultElementoListaShouldNotBeFound("codigo.notEquals=" + DEFAULT_CODIGO);

        // Get all the elementoListaList where codigo not equals to UPDATED_CODIGO
        defaultElementoListaShouldBeFound("codigo.notEquals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllElementoListasByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        elementoListaRepository.saveAndFlush(elementoLista);

        // Get all the elementoListaList where codigo in DEFAULT_CODIGO or UPDATED_CODIGO
        defaultElementoListaShouldBeFound("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO);

        // Get all the elementoListaList where codigo equals to UPDATED_CODIGO
        defaultElementoListaShouldNotBeFound("codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllElementoListasByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        elementoListaRepository.saveAndFlush(elementoLista);

        // Get all the elementoListaList where codigo is not null
        defaultElementoListaShouldBeFound("codigo.specified=true");

        // Get all the elementoListaList where codigo is null
        defaultElementoListaShouldNotBeFound("codigo.specified=false");
    }
                @Test
    @Transactional
    public void getAllElementoListasByCodigoContainsSomething() throws Exception {
        // Initialize the database
        elementoListaRepository.saveAndFlush(elementoLista);

        // Get all the elementoListaList where codigo contains DEFAULT_CODIGO
        defaultElementoListaShouldBeFound("codigo.contains=" + DEFAULT_CODIGO);

        // Get all the elementoListaList where codigo contains UPDATED_CODIGO
        defaultElementoListaShouldNotBeFound("codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllElementoListasByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        elementoListaRepository.saveAndFlush(elementoLista);

        // Get all the elementoListaList where codigo does not contain DEFAULT_CODIGO
        defaultElementoListaShouldNotBeFound("codigo.doesNotContain=" + DEFAULT_CODIGO);

        // Get all the elementoListaList where codigo does not contain UPDATED_CODIGO
        defaultElementoListaShouldBeFound("codigo.doesNotContain=" + UPDATED_CODIGO);
    }


    @Test
    @Transactional
    public void getAllElementoListasByTipoListaIsEqualToSomething() throws Exception {
        // Get already existing entity
        TipoLista tipoLista = elementoLista.getTipoLista();
        elementoListaRepository.saveAndFlush(elementoLista);
        Long tipoListaId = tipoLista.getId();

        // Get all the elementoListaList where tipoLista equals to tipoListaId
        defaultElementoListaShouldBeFound("tipoListaId.equals=" + tipoListaId);

        // Get all the elementoListaList where tipoLista equals to tipoListaId + 1
        defaultElementoListaShouldNotBeFound("tipoListaId.equals=" + (tipoListaId + 1));
    }


    @Test
    @Transactional
    public void getAllElementoListasByPadreIsEqualToSomething() throws Exception {
        // Initialize the database
        elementoListaRepository.saveAndFlush(elementoLista);
        ElementoLista padre = ElementoListaResourceIT.createEntity(em);
        em.persist(padre);
        em.flush();
        elementoLista.setPadre(padre);
        elementoListaRepository.saveAndFlush(elementoLista);
        Long padreId = padre.getId();

        // Get all the elementoListaList where padre equals to padreId
        defaultElementoListaShouldBeFound("padreId.equals=" + padreId);

        // Get all the elementoListaList where padre equals to padreId + 1
        defaultElementoListaShouldNotBeFound("padreId.equals=" + (padreId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultElementoListaShouldBeFound(String filter) throws Exception {
        restElementoListaMockMvc.perform(get("/api/elemento-listas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(elementoLista.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].propiedadesContentType").value(hasItem(DEFAULT_PROPIEDADES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].propiedades").value(hasItem(Base64Utils.encodeToString(DEFAULT_PROPIEDADES))));

        // Check, that the count call also returns 1
        restElementoListaMockMvc.perform(get("/api/elemento-listas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultElementoListaShouldNotBeFound(String filter) throws Exception {
        restElementoListaMockMvc.perform(get("/api/elemento-listas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restElementoListaMockMvc.perform(get("/api/elemento-listas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingElementoLista() throws Exception {
        // Get the elementoLista
        restElementoListaMockMvc.perform(get("/api/elemento-listas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateElementoLista() throws Exception {
        // Initialize the database
        elementoListaRepository.saveAndFlush(elementoLista);

        int databaseSizeBeforeUpdate = elementoListaRepository.findAll().size();

        // Update the elementoLista
        ElementoLista updatedElementoLista = elementoListaRepository.findById(elementoLista.getId()).get();
        // Disconnect from session so that the updates on updatedElementoLista are not directly saved in db
        em.detach(updatedElementoLista);
        updatedElementoLista
            .nombre(UPDATED_NOMBRE)
            .codigo(UPDATED_CODIGO)
            .propiedades(UPDATED_PROPIEDADES)
            .propiedadesContentType(UPDATED_PROPIEDADES_CONTENT_TYPE);
        ElementoListaDTO elementoListaDTO = elementoListaMapper.toDto(updatedElementoLista);

        restElementoListaMockMvc.perform(put("/api/elemento-listas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(elementoListaDTO)))
            .andExpect(status().isOk());

        // Validate the ElementoLista in the database
        List<ElementoLista> elementoListaList = elementoListaRepository.findAll();
        assertThat(elementoListaList).hasSize(databaseSizeBeforeUpdate);
        ElementoLista testElementoLista = elementoListaList.get(elementoListaList.size() - 1);
        assertThat(testElementoLista.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testElementoLista.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testElementoLista.getPropiedades()).isEqualTo(UPDATED_PROPIEDADES);
        assertThat(testElementoLista.getPropiedadesContentType()).isEqualTo(UPDATED_PROPIEDADES_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingElementoLista() throws Exception {
        int databaseSizeBeforeUpdate = elementoListaRepository.findAll().size();

        // Create the ElementoLista
        ElementoListaDTO elementoListaDTO = elementoListaMapper.toDto(elementoLista);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restElementoListaMockMvc.perform(put("/api/elemento-listas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(elementoListaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ElementoLista in the database
        List<ElementoLista> elementoListaList = elementoListaRepository.findAll();
        assertThat(elementoListaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteElementoLista() throws Exception {
        // Initialize the database
        elementoListaRepository.saveAndFlush(elementoLista);

        int databaseSizeBeforeDelete = elementoListaRepository.findAll().size();

        // Delete the elementoLista
        restElementoListaMockMvc.perform(delete("/api/elemento-listas/{id}", elementoLista.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ElementoLista> elementoListaList = elementoListaRepository.findAll();
        assertThat(elementoListaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
