package com.themakers.mitienda.web.rest;

import com.themakers.mitienda.MitiendaApp;
import com.themakers.mitienda.domain.Usuario;
import com.themakers.mitienda.domain.User;
import com.themakers.mitienda.domain.ElementoLista;
import com.themakers.mitienda.repository.UsuarioRepository;
import com.themakers.mitienda.service.UsuarioService;
import com.themakers.mitienda.service.dto.UsuarioDTO;
import com.themakers.mitienda.service.mapper.UsuarioMapper;
import com.themakers.mitienda.web.rest.errors.ExceptionTranslator;
import com.themakers.mitienda.service.dto.UsuarioCriteria;
import com.themakers.mitienda.service.UsuarioQueryService;

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

import com.themakers.mitienda.domain.enumeration.Genero;
/**
 * Integration tests for the {@link UsuarioResource} REST controller.
 */
@SpringBootTest(classes = MitiendaApp.class)
public class UsuarioResourceIT {

    private static final String DEFAULT_SEGUNDO_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_SEGUNDO_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_SEGUNDO_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_SEGUNDO_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_NUM_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_NUM_DOCUMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_CELULAR = "AAAAAAAAAA";
    private static final String UPDATED_CELULAR = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final Genero DEFAULT_GENERO = Genero.HOMBRE;
    private static final Genero UPDATED_GENERO = Genero.MUJER;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioQueryService usuarioQueryService;

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

    private MockMvc restUsuarioMockMvc;

    private Usuario usuario;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UsuarioResource usuarioResource = new UsuarioResource(usuarioService, usuarioQueryService);
        this.restUsuarioMockMvc = MockMvcBuilders.standaloneSetup(usuarioResource)
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
    public static Usuario createEntity(EntityManager em) {
        Usuario usuario = new Usuario()
            .segundoNombre(DEFAULT_SEGUNDO_NOMBRE)
            .segundoApellido(DEFAULT_SEGUNDO_APELLIDO)
            .numDocumento(DEFAULT_NUM_DOCUMENTO)
            .celular(DEFAULT_CELULAR)
            .direccion(DEFAULT_DIRECCION)
            .genero(DEFAULT_GENERO);
        // Add required entity
        ElementoLista elementoLista;
        if (TestUtil.findAll(em, ElementoLista.class).isEmpty()) {
            elementoLista = ElementoListaResourceIT.createEntity(em);
            em.persist(elementoLista);
            em.flush();
        } else {
            elementoLista = TestUtil.findAll(em, ElementoLista.class).get(0);
        }
        usuario.setCuidad(elementoLista);
        return usuario;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createUpdatedEntity(EntityManager em) {
        Usuario usuario = new Usuario()
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .numDocumento(UPDATED_NUM_DOCUMENTO)
            .celular(UPDATED_CELULAR)
            .direccion(UPDATED_DIRECCION)
            .genero(UPDATED_GENERO);
        // Add required entity
        ElementoLista elementoLista;
        if (TestUtil.findAll(em, ElementoLista.class).isEmpty()) {
            elementoLista = ElementoListaResourceIT.createUpdatedEntity(em);
            em.persist(elementoLista);
            em.flush();
        } else {
            elementoLista = TestUtil.findAll(em, ElementoLista.class).get(0);
        }
        usuario.setCuidad(elementoLista);
        return usuario;
    }

    @BeforeEach
    public void initTest() {
        usuario = createEntity(em);
    }

    @Test
    @Transactional
    public void createUsuario() throws Exception {
        int databaseSizeBeforeCreate = usuarioRepository.findAll().size();

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);
        restUsuarioMockMvc.perform(post("/api/usuarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isCreated());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeCreate + 1);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getSegundoNombre()).isEqualTo(DEFAULT_SEGUNDO_NOMBRE);
        assertThat(testUsuario.getSegundoApellido()).isEqualTo(DEFAULT_SEGUNDO_APELLIDO);
        assertThat(testUsuario.getNumDocumento()).isEqualTo(DEFAULT_NUM_DOCUMENTO);
        assertThat(testUsuario.getCelular()).isEqualTo(DEFAULT_CELULAR);
        assertThat(testUsuario.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testUsuario.getGenero()).isEqualTo(DEFAULT_GENERO);
    }

    @Test
    @Transactional
    public void createUsuarioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = usuarioRepository.findAll().size();

        // Create the Usuario with an existing ID
        usuario.setId(1L);
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsuarioMockMvc.perform(post("/api/usuarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNumDocumentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().size();
        // set the field null
        usuario.setNumDocumento(null);

        // Create the Usuario, which fails.
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        restUsuarioMockMvc.perform(post("/api/usuarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isBadRequest());

        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCelularIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().size();
        // set the field null
        usuario.setCelular(null);

        // Create the Usuario, which fails.
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        restUsuarioMockMvc.perform(post("/api/usuarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isBadRequest());

        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUsuarios() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList
        restUsuarioMockMvc.perform(get("/api/usuarios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].segundoNombre").value(hasItem(DEFAULT_SEGUNDO_NOMBRE)))
            .andExpect(jsonPath("$.[*].segundoApellido").value(hasItem(DEFAULT_SEGUNDO_APELLIDO)))
            .andExpect(jsonPath("$.[*].numDocumento").value(hasItem(DEFAULT_NUM_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].genero").value(hasItem(DEFAULT_GENERO.toString())));
    }
    
    @Test
    @Transactional
    public void getUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get the usuario
        restUsuarioMockMvc.perform(get("/api/usuarios/{id}", usuario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(usuario.getId().intValue()))
            .andExpect(jsonPath("$.segundoNombre").value(DEFAULT_SEGUNDO_NOMBRE))
            .andExpect(jsonPath("$.segundoApellido").value(DEFAULT_SEGUNDO_APELLIDO))
            .andExpect(jsonPath("$.numDocumento").value(DEFAULT_NUM_DOCUMENTO))
            .andExpect(jsonPath("$.celular").value(DEFAULT_CELULAR))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.genero").value(DEFAULT_GENERO.toString()));
    }


    @Test
    @Transactional
    public void getUsuariosByIdFiltering() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        Long id = usuario.getId();

        defaultUsuarioShouldBeFound("id.equals=" + id);
        defaultUsuarioShouldNotBeFound("id.notEquals=" + id);

        defaultUsuarioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUsuarioShouldNotBeFound("id.greaterThan=" + id);

        defaultUsuarioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUsuarioShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllUsuariosBySegundoNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where segundoNombre equals to DEFAULT_SEGUNDO_NOMBRE
        defaultUsuarioShouldBeFound("segundoNombre.equals=" + DEFAULT_SEGUNDO_NOMBRE);

        // Get all the usuarioList where segundoNombre equals to UPDATED_SEGUNDO_NOMBRE
        defaultUsuarioShouldNotBeFound("segundoNombre.equals=" + UPDATED_SEGUNDO_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllUsuariosBySegundoNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where segundoNombre not equals to DEFAULT_SEGUNDO_NOMBRE
        defaultUsuarioShouldNotBeFound("segundoNombre.notEquals=" + DEFAULT_SEGUNDO_NOMBRE);

        // Get all the usuarioList where segundoNombre not equals to UPDATED_SEGUNDO_NOMBRE
        defaultUsuarioShouldBeFound("segundoNombre.notEquals=" + UPDATED_SEGUNDO_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllUsuariosBySegundoNombreIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where segundoNombre in DEFAULT_SEGUNDO_NOMBRE or UPDATED_SEGUNDO_NOMBRE
        defaultUsuarioShouldBeFound("segundoNombre.in=" + DEFAULT_SEGUNDO_NOMBRE + "," + UPDATED_SEGUNDO_NOMBRE);

        // Get all the usuarioList where segundoNombre equals to UPDATED_SEGUNDO_NOMBRE
        defaultUsuarioShouldNotBeFound("segundoNombre.in=" + UPDATED_SEGUNDO_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllUsuariosBySegundoNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where segundoNombre is not null
        defaultUsuarioShouldBeFound("segundoNombre.specified=true");

        // Get all the usuarioList where segundoNombre is null
        defaultUsuarioShouldNotBeFound("segundoNombre.specified=false");
    }
                @Test
    @Transactional
    public void getAllUsuariosBySegundoNombreContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where segundoNombre contains DEFAULT_SEGUNDO_NOMBRE
        defaultUsuarioShouldBeFound("segundoNombre.contains=" + DEFAULT_SEGUNDO_NOMBRE);

        // Get all the usuarioList where segundoNombre contains UPDATED_SEGUNDO_NOMBRE
        defaultUsuarioShouldNotBeFound("segundoNombre.contains=" + UPDATED_SEGUNDO_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllUsuariosBySegundoNombreNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where segundoNombre does not contain DEFAULT_SEGUNDO_NOMBRE
        defaultUsuarioShouldNotBeFound("segundoNombre.doesNotContain=" + DEFAULT_SEGUNDO_NOMBRE);

        // Get all the usuarioList where segundoNombre does not contain UPDATED_SEGUNDO_NOMBRE
        defaultUsuarioShouldBeFound("segundoNombre.doesNotContain=" + UPDATED_SEGUNDO_NOMBRE);
    }


    @Test
    @Transactional
    public void getAllUsuariosBySegundoApellidoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where segundoApellido equals to DEFAULT_SEGUNDO_APELLIDO
        defaultUsuarioShouldBeFound("segundoApellido.equals=" + DEFAULT_SEGUNDO_APELLIDO);

        // Get all the usuarioList where segundoApellido equals to UPDATED_SEGUNDO_APELLIDO
        defaultUsuarioShouldNotBeFound("segundoApellido.equals=" + UPDATED_SEGUNDO_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllUsuariosBySegundoApellidoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where segundoApellido not equals to DEFAULT_SEGUNDO_APELLIDO
        defaultUsuarioShouldNotBeFound("segundoApellido.notEquals=" + DEFAULT_SEGUNDO_APELLIDO);

        // Get all the usuarioList where segundoApellido not equals to UPDATED_SEGUNDO_APELLIDO
        defaultUsuarioShouldBeFound("segundoApellido.notEquals=" + UPDATED_SEGUNDO_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllUsuariosBySegundoApellidoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where segundoApellido in DEFAULT_SEGUNDO_APELLIDO or UPDATED_SEGUNDO_APELLIDO
        defaultUsuarioShouldBeFound("segundoApellido.in=" + DEFAULT_SEGUNDO_APELLIDO + "," + UPDATED_SEGUNDO_APELLIDO);

        // Get all the usuarioList where segundoApellido equals to UPDATED_SEGUNDO_APELLIDO
        defaultUsuarioShouldNotBeFound("segundoApellido.in=" + UPDATED_SEGUNDO_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllUsuariosBySegundoApellidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where segundoApellido is not null
        defaultUsuarioShouldBeFound("segundoApellido.specified=true");

        // Get all the usuarioList where segundoApellido is null
        defaultUsuarioShouldNotBeFound("segundoApellido.specified=false");
    }
                @Test
    @Transactional
    public void getAllUsuariosBySegundoApellidoContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where segundoApellido contains DEFAULT_SEGUNDO_APELLIDO
        defaultUsuarioShouldBeFound("segundoApellido.contains=" + DEFAULT_SEGUNDO_APELLIDO);

        // Get all the usuarioList where segundoApellido contains UPDATED_SEGUNDO_APELLIDO
        defaultUsuarioShouldNotBeFound("segundoApellido.contains=" + UPDATED_SEGUNDO_APELLIDO);
    }

    @Test
    @Transactional
    public void getAllUsuariosBySegundoApellidoNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where segundoApellido does not contain DEFAULT_SEGUNDO_APELLIDO
        defaultUsuarioShouldNotBeFound("segundoApellido.doesNotContain=" + DEFAULT_SEGUNDO_APELLIDO);

        // Get all the usuarioList where segundoApellido does not contain UPDATED_SEGUNDO_APELLIDO
        defaultUsuarioShouldBeFound("segundoApellido.doesNotContain=" + UPDATED_SEGUNDO_APELLIDO);
    }


    @Test
    @Transactional
    public void getAllUsuariosByNumDocumentoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where numDocumento equals to DEFAULT_NUM_DOCUMENTO
        defaultUsuarioShouldBeFound("numDocumento.equals=" + DEFAULT_NUM_DOCUMENTO);

        // Get all the usuarioList where numDocumento equals to UPDATED_NUM_DOCUMENTO
        defaultUsuarioShouldNotBeFound("numDocumento.equals=" + UPDATED_NUM_DOCUMENTO);
    }

    @Test
    @Transactional
    public void getAllUsuariosByNumDocumentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where numDocumento not equals to DEFAULT_NUM_DOCUMENTO
        defaultUsuarioShouldNotBeFound("numDocumento.notEquals=" + DEFAULT_NUM_DOCUMENTO);

        // Get all the usuarioList where numDocumento not equals to UPDATED_NUM_DOCUMENTO
        defaultUsuarioShouldBeFound("numDocumento.notEquals=" + UPDATED_NUM_DOCUMENTO);
    }

    @Test
    @Transactional
    public void getAllUsuariosByNumDocumentoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where numDocumento in DEFAULT_NUM_DOCUMENTO or UPDATED_NUM_DOCUMENTO
        defaultUsuarioShouldBeFound("numDocumento.in=" + DEFAULT_NUM_DOCUMENTO + "," + UPDATED_NUM_DOCUMENTO);

        // Get all the usuarioList where numDocumento equals to UPDATED_NUM_DOCUMENTO
        defaultUsuarioShouldNotBeFound("numDocumento.in=" + UPDATED_NUM_DOCUMENTO);
    }

    @Test
    @Transactional
    public void getAllUsuariosByNumDocumentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where numDocumento is not null
        defaultUsuarioShouldBeFound("numDocumento.specified=true");

        // Get all the usuarioList where numDocumento is null
        defaultUsuarioShouldNotBeFound("numDocumento.specified=false");
    }
                @Test
    @Transactional
    public void getAllUsuariosByNumDocumentoContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where numDocumento contains DEFAULT_NUM_DOCUMENTO
        defaultUsuarioShouldBeFound("numDocumento.contains=" + DEFAULT_NUM_DOCUMENTO);

        // Get all the usuarioList where numDocumento contains UPDATED_NUM_DOCUMENTO
        defaultUsuarioShouldNotBeFound("numDocumento.contains=" + UPDATED_NUM_DOCUMENTO);
    }

    @Test
    @Transactional
    public void getAllUsuariosByNumDocumentoNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where numDocumento does not contain DEFAULT_NUM_DOCUMENTO
        defaultUsuarioShouldNotBeFound("numDocumento.doesNotContain=" + DEFAULT_NUM_DOCUMENTO);

        // Get all the usuarioList where numDocumento does not contain UPDATED_NUM_DOCUMENTO
        defaultUsuarioShouldBeFound("numDocumento.doesNotContain=" + UPDATED_NUM_DOCUMENTO);
    }


    @Test
    @Transactional
    public void getAllUsuariosByCelularIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where celular equals to DEFAULT_CELULAR
        defaultUsuarioShouldBeFound("celular.equals=" + DEFAULT_CELULAR);

        // Get all the usuarioList where celular equals to UPDATED_CELULAR
        defaultUsuarioShouldNotBeFound("celular.equals=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    public void getAllUsuariosByCelularIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where celular not equals to DEFAULT_CELULAR
        defaultUsuarioShouldNotBeFound("celular.notEquals=" + DEFAULT_CELULAR);

        // Get all the usuarioList where celular not equals to UPDATED_CELULAR
        defaultUsuarioShouldBeFound("celular.notEquals=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    public void getAllUsuariosByCelularIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where celular in DEFAULT_CELULAR or UPDATED_CELULAR
        defaultUsuarioShouldBeFound("celular.in=" + DEFAULT_CELULAR + "," + UPDATED_CELULAR);

        // Get all the usuarioList where celular equals to UPDATED_CELULAR
        defaultUsuarioShouldNotBeFound("celular.in=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    public void getAllUsuariosByCelularIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where celular is not null
        defaultUsuarioShouldBeFound("celular.specified=true");

        // Get all the usuarioList where celular is null
        defaultUsuarioShouldNotBeFound("celular.specified=false");
    }
                @Test
    @Transactional
    public void getAllUsuariosByCelularContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where celular contains DEFAULT_CELULAR
        defaultUsuarioShouldBeFound("celular.contains=" + DEFAULT_CELULAR);

        // Get all the usuarioList where celular contains UPDATED_CELULAR
        defaultUsuarioShouldNotBeFound("celular.contains=" + UPDATED_CELULAR);
    }

    @Test
    @Transactional
    public void getAllUsuariosByCelularNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where celular does not contain DEFAULT_CELULAR
        defaultUsuarioShouldNotBeFound("celular.doesNotContain=" + DEFAULT_CELULAR);

        // Get all the usuarioList where celular does not contain UPDATED_CELULAR
        defaultUsuarioShouldBeFound("celular.doesNotContain=" + UPDATED_CELULAR);
    }


    @Test
    @Transactional
    public void getAllUsuariosByDireccionIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where direccion equals to DEFAULT_DIRECCION
        defaultUsuarioShouldBeFound("direccion.equals=" + DEFAULT_DIRECCION);

        // Get all the usuarioList where direccion equals to UPDATED_DIRECCION
        defaultUsuarioShouldNotBeFound("direccion.equals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    public void getAllUsuariosByDireccionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where direccion not equals to DEFAULT_DIRECCION
        defaultUsuarioShouldNotBeFound("direccion.notEquals=" + DEFAULT_DIRECCION);

        // Get all the usuarioList where direccion not equals to UPDATED_DIRECCION
        defaultUsuarioShouldBeFound("direccion.notEquals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    public void getAllUsuariosByDireccionIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where direccion in DEFAULT_DIRECCION or UPDATED_DIRECCION
        defaultUsuarioShouldBeFound("direccion.in=" + DEFAULT_DIRECCION + "," + UPDATED_DIRECCION);

        // Get all the usuarioList where direccion equals to UPDATED_DIRECCION
        defaultUsuarioShouldNotBeFound("direccion.in=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    public void getAllUsuariosByDireccionIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where direccion is not null
        defaultUsuarioShouldBeFound("direccion.specified=true");

        // Get all the usuarioList where direccion is null
        defaultUsuarioShouldNotBeFound("direccion.specified=false");
    }
                @Test
    @Transactional
    public void getAllUsuariosByDireccionContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where direccion contains DEFAULT_DIRECCION
        defaultUsuarioShouldBeFound("direccion.contains=" + DEFAULT_DIRECCION);

        // Get all the usuarioList where direccion contains UPDATED_DIRECCION
        defaultUsuarioShouldNotBeFound("direccion.contains=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    public void getAllUsuariosByDireccionNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where direccion does not contain DEFAULT_DIRECCION
        defaultUsuarioShouldNotBeFound("direccion.doesNotContain=" + DEFAULT_DIRECCION);

        // Get all the usuarioList where direccion does not contain UPDATED_DIRECCION
        defaultUsuarioShouldBeFound("direccion.doesNotContain=" + UPDATED_DIRECCION);
    }


    @Test
    @Transactional
    public void getAllUsuariosByGeneroIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where genero equals to DEFAULT_GENERO
        defaultUsuarioShouldBeFound("genero.equals=" + DEFAULT_GENERO);

        // Get all the usuarioList where genero equals to UPDATED_GENERO
        defaultUsuarioShouldNotBeFound("genero.equals=" + UPDATED_GENERO);
    }

    @Test
    @Transactional
    public void getAllUsuariosByGeneroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where genero not equals to DEFAULT_GENERO
        defaultUsuarioShouldNotBeFound("genero.notEquals=" + DEFAULT_GENERO);

        // Get all the usuarioList where genero not equals to UPDATED_GENERO
        defaultUsuarioShouldBeFound("genero.notEquals=" + UPDATED_GENERO);
    }

    @Test
    @Transactional
    public void getAllUsuariosByGeneroIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where genero in DEFAULT_GENERO or UPDATED_GENERO
        defaultUsuarioShouldBeFound("genero.in=" + DEFAULT_GENERO + "," + UPDATED_GENERO);

        // Get all the usuarioList where genero equals to UPDATED_GENERO
        defaultUsuarioShouldNotBeFound("genero.in=" + UPDATED_GENERO);
    }

    @Test
    @Transactional
    public void getAllUsuariosByGeneroIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where genero is not null
        defaultUsuarioShouldBeFound("genero.specified=true");

        // Get all the usuarioList where genero is null
        defaultUsuarioShouldNotBeFound("genero.specified=false");
    }

    @Test
    @Transactional
    public void getAllUsuariosByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        usuario.setUser(user);
        usuarioRepository.saveAndFlush(usuario);
        Long userId = user.getId();

        // Get all the usuarioList where user equals to userId
        defaultUsuarioShouldBeFound("userId.equals=" + userId);

        // Get all the usuarioList where user equals to userId + 1
        defaultUsuarioShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllUsuariosByCuidadIsEqualToSomething() throws Exception {
        // Get already existing entity
        ElementoLista cuidad = usuario.getCuidad();
        usuarioRepository.saveAndFlush(usuario);
        Long cuidadId = cuidad.getId();

        // Get all the usuarioList where cuidad equals to cuidadId
        defaultUsuarioShouldBeFound("cuidadId.equals=" + cuidadId);

        // Get all the usuarioList where cuidad equals to cuidadId + 1
        defaultUsuarioShouldNotBeFound("cuidadId.equals=" + (cuidadId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUsuarioShouldBeFound(String filter) throws Exception {
        restUsuarioMockMvc.perform(get("/api/usuarios?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].segundoNombre").value(hasItem(DEFAULT_SEGUNDO_NOMBRE)))
            .andExpect(jsonPath("$.[*].segundoApellido").value(hasItem(DEFAULT_SEGUNDO_APELLIDO)))
            .andExpect(jsonPath("$.[*].numDocumento").value(hasItem(DEFAULT_NUM_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].genero").value(hasItem(DEFAULT_GENERO.toString())));

        // Check, that the count call also returns 1
        restUsuarioMockMvc.perform(get("/api/usuarios/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUsuarioShouldNotBeFound(String filter) throws Exception {
        restUsuarioMockMvc.perform(get("/api/usuarios?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUsuarioMockMvc.perform(get("/api/usuarios/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingUsuario() throws Exception {
        // Get the usuario
        restUsuarioMockMvc.perform(get("/api/usuarios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario
        Usuario updatedUsuario = usuarioRepository.findById(usuario.getId()).get();
        // Disconnect from session so that the updates on updatedUsuario are not directly saved in db
        em.detach(updatedUsuario);
        updatedUsuario
            .segundoNombre(UPDATED_SEGUNDO_NOMBRE)
            .segundoApellido(UPDATED_SEGUNDO_APELLIDO)
            .numDocumento(UPDATED_NUM_DOCUMENTO)
            .celular(UPDATED_CELULAR)
            .direccion(UPDATED_DIRECCION)
            .genero(UPDATED_GENERO);
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(updatedUsuario);

        restUsuarioMockMvc.perform(put("/api/usuarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getSegundoNombre()).isEqualTo(UPDATED_SEGUNDO_NOMBRE);
        assertThat(testUsuario.getSegundoApellido()).isEqualTo(UPDATED_SEGUNDO_APELLIDO);
        assertThat(testUsuario.getNumDocumento()).isEqualTo(UPDATED_NUM_DOCUMENTO);
        assertThat(testUsuario.getCelular()).isEqualTo(UPDATED_CELULAR);
        assertThat(testUsuario.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testUsuario.getGenero()).isEqualTo(UPDATED_GENERO);
    }

    @Test
    @Transactional
    public void updateNonExistingUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioMockMvc.perform(put("/api/usuarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeDelete = usuarioRepository.findAll().size();

        // Delete the usuario
        restUsuarioMockMvc.perform(delete("/api/usuarios/{id}", usuario.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
