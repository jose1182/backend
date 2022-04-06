package com.netjob.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.netjob.app.IntegrationTest;
import com.netjob.app.domain.Conversacion;
import com.netjob.app.domain.User;
import com.netjob.app.domain.Usuario;
import com.netjob.app.repository.UsuarioRepository;
import com.netjob.app.service.UsuarioService;
import com.netjob.app.service.criteria.UsuarioCriteria;
import com.netjob.app.service.dto.UsuarioDTO;
import com.netjob.app.service.mapper.UsuarioMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UsuarioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UsuarioResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDOS = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDOS = "BBBBBBBBBB";

    private static final String DEFAULT_CORREO = "AAAAAAAAAA";
    private static final String UPDATED_CORREO = "BBBBBBBBBB";

    private static final String DEFAULT_DNI = "93193396K";
    private static final String UPDATED_DNI = "96915900A";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_LOCALIDAD = "AAAAAAAAAA";
    private static final String UPDATED_LOCALIDAD = "BBBBBBBBBB";

    private static final String DEFAULT_PROVINCIA = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCIA = "BBBBBBBBBB";

    private static final String DEFAULT_PROFESION = "AAAAAAAAAA";
    private static final String UPDATED_PROFESION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/usuarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioRepository usuarioRepositoryMock;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Mock
    private UsuarioService usuarioServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUsuarioMockMvc;

    private Usuario usuario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createEntity(EntityManager em) {
        Usuario usuario = new Usuario()
            .nombre(DEFAULT_NOMBRE)
            .apellidos(DEFAULT_APELLIDOS)
            .correo(DEFAULT_CORREO)
            .dni(DEFAULT_DNI)
            .direccion(DEFAULT_DIRECCION)
            .localidad(DEFAULT_LOCALIDAD)
            .provincia(DEFAULT_PROVINCIA)
            .profesion(DEFAULT_PROFESION)
            .fn(DEFAULT_FN)
            .fechaRegistro(DEFAULT_FECHA_REGISTRO);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        usuario.setUser(user);
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
            .nombre(UPDATED_NOMBRE)
            .apellidos(UPDATED_APELLIDOS)
            .correo(UPDATED_CORREO)
            .dni(UPDATED_DNI)
            .direccion(UPDATED_DIRECCION)
            .localidad(UPDATED_LOCALIDAD)
            .provincia(UPDATED_PROVINCIA)
            .profesion(UPDATED_PROFESION)
            .fn(UPDATED_FN)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        usuario.setUser(user);
        return usuario;
    }

    @BeforeEach
    public void initTest() {
        usuario = createEntity(em);
    }

    @Test
    @Transactional
    void createUsuario() throws Exception {
        int databaseSizeBeforeCreate = usuarioRepository.findAll().size();
        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);
        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isCreated());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeCreate + 1);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testUsuario.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
        assertThat(testUsuario.getCorreo()).isEqualTo(DEFAULT_CORREO);
        assertThat(testUsuario.getDni()).isEqualTo(DEFAULT_DNI);
        assertThat(testUsuario.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testUsuario.getLocalidad()).isEqualTo(DEFAULT_LOCALIDAD);
        assertThat(testUsuario.getProvincia()).isEqualTo(DEFAULT_PROVINCIA);
        assertThat(testUsuario.getProfesion()).isEqualTo(DEFAULT_PROFESION);
        assertThat(testUsuario.getFn()).isEqualTo(DEFAULT_FN);
        assertThat(testUsuario.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);

        // Validate the id for MapsId, the ids must be same
        assertThat(testUsuario.getId()).isEqualTo(usuarioDTO.getUser().getId());
    }

    @Test
    @Transactional
    void createUsuarioWithExistingId() throws Exception {
        // Create the Usuario with an existing ID
        usuario.setId(1L);
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        int databaseSizeBeforeCreate = usuarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void updateUsuarioMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);
        int databaseSizeBeforeCreate = usuarioRepository.findAll().size();

        // Add a new parent entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();

        // Load the usuario
        Usuario updatedUsuario = usuarioRepository.findById(usuario.getId()).get();
        assertThat(updatedUsuario).isNotNull();
        // Disconnect from session so that the updates on updatedUsuario are not directly saved in db
        em.detach(updatedUsuario);

        // Update the User with new association value
        updatedUsuario.setUser(user);
        UsuarioDTO updatedUsuarioDTO = usuarioMapper.toDto(updatedUsuario);
        assertThat(updatedUsuarioDTO).isNotNull();

        // Update the entity
        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUsuarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUsuarioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeCreate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        // Validate the id for MapsId, the ids must be same
        // Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
        // Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
        // assertThat(testUsuario.getId()).isEqualTo(testUsuario.getUser().getId());
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().size();
        // set the field null
        usuario.setNombre(null);

        // Create the Usuario, which fails.
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isBadRequest());

        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCorreoIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().size();
        // set the field null
        usuario.setCorreo(null);

        // Create the Usuario, which fails.
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isBadRequest());

        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDniIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().size();
        // set the field null
        usuario.setDni(null);

        // Create the Usuario, which fails.
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isBadRequest());

        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDireccionIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().size();
        // set the field null
        usuario.setDireccion(null);

        // Create the Usuario, which fails.
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isBadRequest());

        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLocalidadIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().size();
        // set the field null
        usuario.setLocalidad(null);

        // Create the Usuario, which fails.
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isBadRequest());

        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProvinciaIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().size();
        // set the field null
        usuario.setProvincia(null);

        // Create the Usuario, which fails.
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isBadRequest());

        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFnIsRequired() throws Exception {
        int databaseSizeBeforeTest = usuarioRepository.findAll().size();
        // set the field null
        usuario.setFn(null);

        // Create the Usuario, which fails.
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isBadRequest());

        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUsuarios() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS)))
            .andExpect(jsonPath("$.[*].correo").value(hasItem(DEFAULT_CORREO)))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].localidad").value(hasItem(DEFAULT_LOCALIDAD)))
            .andExpect(jsonPath("$.[*].provincia").value(hasItem(DEFAULT_PROVINCIA)))
            .andExpect(jsonPath("$.[*].profesion").value(hasItem(DEFAULT_PROFESION)))
            .andExpect(jsonPath("$.[*].fn").value(hasItem(DEFAULT_FN.toString())))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUsuariosWithEagerRelationshipsIsEnabled() throws Exception {
        when(usuarioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUsuarioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(usuarioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUsuariosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(usuarioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUsuarioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(usuarioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get the usuario
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL_ID, usuario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(usuario.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellidos").value(DEFAULT_APELLIDOS))
            .andExpect(jsonPath("$.correo").value(DEFAULT_CORREO))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.localidad").value(DEFAULT_LOCALIDAD))
            .andExpect(jsonPath("$.provincia").value(DEFAULT_PROVINCIA))
            .andExpect(jsonPath("$.profesion").value(DEFAULT_PROFESION))
            .andExpect(jsonPath("$.fn").value(DEFAULT_FN.toString()))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()));
    }

    @Test
    @Transactional
    void getUsuariosByIdFiltering() throws Exception {
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
    void getAllUsuariosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nombre equals to DEFAULT_NOMBRE
        defaultUsuarioShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the usuarioList where nombre equals to UPDATED_NOMBRE
        defaultUsuarioShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuariosByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nombre not equals to DEFAULT_NOMBRE
        defaultUsuarioShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the usuarioList where nombre not equals to UPDATED_NOMBRE
        defaultUsuarioShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuariosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultUsuarioShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the usuarioList where nombre equals to UPDATED_NOMBRE
        defaultUsuarioShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuariosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nombre is not null
        defaultUsuarioShouldBeFound("nombre.specified=true");

        // Get all the usuarioList where nombre is null
        defaultUsuarioShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByNombreContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nombre contains DEFAULT_NOMBRE
        defaultUsuarioShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the usuarioList where nombre contains UPDATED_NOMBRE
        defaultUsuarioShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuariosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where nombre does not contain DEFAULT_NOMBRE
        defaultUsuarioShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the usuarioList where nombre does not contain UPDATED_NOMBRE
        defaultUsuarioShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllUsuariosByApellidosIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where apellidos equals to DEFAULT_APELLIDOS
        defaultUsuarioShouldBeFound("apellidos.equals=" + DEFAULT_APELLIDOS);

        // Get all the usuarioList where apellidos equals to UPDATED_APELLIDOS
        defaultUsuarioShouldNotBeFound("apellidos.equals=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllUsuariosByApellidosIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where apellidos not equals to DEFAULT_APELLIDOS
        defaultUsuarioShouldNotBeFound("apellidos.notEquals=" + DEFAULT_APELLIDOS);

        // Get all the usuarioList where apellidos not equals to UPDATED_APELLIDOS
        defaultUsuarioShouldBeFound("apellidos.notEquals=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllUsuariosByApellidosIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where apellidos in DEFAULT_APELLIDOS or UPDATED_APELLIDOS
        defaultUsuarioShouldBeFound("apellidos.in=" + DEFAULT_APELLIDOS + "," + UPDATED_APELLIDOS);

        // Get all the usuarioList where apellidos equals to UPDATED_APELLIDOS
        defaultUsuarioShouldNotBeFound("apellidos.in=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllUsuariosByApellidosIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where apellidos is not null
        defaultUsuarioShouldBeFound("apellidos.specified=true");

        // Get all the usuarioList where apellidos is null
        defaultUsuarioShouldNotBeFound("apellidos.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByApellidosContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where apellidos contains DEFAULT_APELLIDOS
        defaultUsuarioShouldBeFound("apellidos.contains=" + DEFAULT_APELLIDOS);

        // Get all the usuarioList where apellidos contains UPDATED_APELLIDOS
        defaultUsuarioShouldNotBeFound("apellidos.contains=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllUsuariosByApellidosNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where apellidos does not contain DEFAULT_APELLIDOS
        defaultUsuarioShouldNotBeFound("apellidos.doesNotContain=" + DEFAULT_APELLIDOS);

        // Get all the usuarioList where apellidos does not contain UPDATED_APELLIDOS
        defaultUsuarioShouldBeFound("apellidos.doesNotContain=" + UPDATED_APELLIDOS);
    }

    @Test
    @Transactional
    void getAllUsuariosByCorreoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where correo equals to DEFAULT_CORREO
        defaultUsuarioShouldBeFound("correo.equals=" + DEFAULT_CORREO);

        // Get all the usuarioList where correo equals to UPDATED_CORREO
        defaultUsuarioShouldNotBeFound("correo.equals=" + UPDATED_CORREO);
    }

    @Test
    @Transactional
    void getAllUsuariosByCorreoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where correo not equals to DEFAULT_CORREO
        defaultUsuarioShouldNotBeFound("correo.notEquals=" + DEFAULT_CORREO);

        // Get all the usuarioList where correo not equals to UPDATED_CORREO
        defaultUsuarioShouldBeFound("correo.notEquals=" + UPDATED_CORREO);
    }

    @Test
    @Transactional
    void getAllUsuariosByCorreoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where correo in DEFAULT_CORREO or UPDATED_CORREO
        defaultUsuarioShouldBeFound("correo.in=" + DEFAULT_CORREO + "," + UPDATED_CORREO);

        // Get all the usuarioList where correo equals to UPDATED_CORREO
        defaultUsuarioShouldNotBeFound("correo.in=" + UPDATED_CORREO);
    }

    @Test
    @Transactional
    void getAllUsuariosByCorreoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where correo is not null
        defaultUsuarioShouldBeFound("correo.specified=true");

        // Get all the usuarioList where correo is null
        defaultUsuarioShouldNotBeFound("correo.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByCorreoContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where correo contains DEFAULT_CORREO
        defaultUsuarioShouldBeFound("correo.contains=" + DEFAULT_CORREO);

        // Get all the usuarioList where correo contains UPDATED_CORREO
        defaultUsuarioShouldNotBeFound("correo.contains=" + UPDATED_CORREO);
    }

    @Test
    @Transactional
    void getAllUsuariosByCorreoNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where correo does not contain DEFAULT_CORREO
        defaultUsuarioShouldNotBeFound("correo.doesNotContain=" + DEFAULT_CORREO);

        // Get all the usuarioList where correo does not contain UPDATED_CORREO
        defaultUsuarioShouldBeFound("correo.doesNotContain=" + UPDATED_CORREO);
    }

    @Test
    @Transactional
    void getAllUsuariosByDniIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where dni equals to DEFAULT_DNI
        defaultUsuarioShouldBeFound("dni.equals=" + DEFAULT_DNI);

        // Get all the usuarioList where dni equals to UPDATED_DNI
        defaultUsuarioShouldNotBeFound("dni.equals=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllUsuariosByDniIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where dni not equals to DEFAULT_DNI
        defaultUsuarioShouldNotBeFound("dni.notEquals=" + DEFAULT_DNI);

        // Get all the usuarioList where dni not equals to UPDATED_DNI
        defaultUsuarioShouldBeFound("dni.notEquals=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllUsuariosByDniIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where dni in DEFAULT_DNI or UPDATED_DNI
        defaultUsuarioShouldBeFound("dni.in=" + DEFAULT_DNI + "," + UPDATED_DNI);

        // Get all the usuarioList where dni equals to UPDATED_DNI
        defaultUsuarioShouldNotBeFound("dni.in=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllUsuariosByDniIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where dni is not null
        defaultUsuarioShouldBeFound("dni.specified=true");

        // Get all the usuarioList where dni is null
        defaultUsuarioShouldNotBeFound("dni.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByDniContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where dni contains DEFAULT_DNI
        defaultUsuarioShouldBeFound("dni.contains=" + DEFAULT_DNI);

        // Get all the usuarioList where dni contains UPDATED_DNI
        defaultUsuarioShouldNotBeFound("dni.contains=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllUsuariosByDniNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where dni does not contain DEFAULT_DNI
        defaultUsuarioShouldNotBeFound("dni.doesNotContain=" + DEFAULT_DNI);

        // Get all the usuarioList where dni does not contain UPDATED_DNI
        defaultUsuarioShouldBeFound("dni.doesNotContain=" + UPDATED_DNI);
    }

    @Test
    @Transactional
    void getAllUsuariosByDireccionIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where direccion equals to DEFAULT_DIRECCION
        defaultUsuarioShouldBeFound("direccion.equals=" + DEFAULT_DIRECCION);

        // Get all the usuarioList where direccion equals to UPDATED_DIRECCION
        defaultUsuarioShouldNotBeFound("direccion.equals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllUsuariosByDireccionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where direccion not equals to DEFAULT_DIRECCION
        defaultUsuarioShouldNotBeFound("direccion.notEquals=" + DEFAULT_DIRECCION);

        // Get all the usuarioList where direccion not equals to UPDATED_DIRECCION
        defaultUsuarioShouldBeFound("direccion.notEquals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllUsuariosByDireccionIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where direccion in DEFAULT_DIRECCION or UPDATED_DIRECCION
        defaultUsuarioShouldBeFound("direccion.in=" + DEFAULT_DIRECCION + "," + UPDATED_DIRECCION);

        // Get all the usuarioList where direccion equals to UPDATED_DIRECCION
        defaultUsuarioShouldNotBeFound("direccion.in=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllUsuariosByDireccionIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where direccion is not null
        defaultUsuarioShouldBeFound("direccion.specified=true");

        // Get all the usuarioList where direccion is null
        defaultUsuarioShouldNotBeFound("direccion.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByDireccionContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where direccion contains DEFAULT_DIRECCION
        defaultUsuarioShouldBeFound("direccion.contains=" + DEFAULT_DIRECCION);

        // Get all the usuarioList where direccion contains UPDATED_DIRECCION
        defaultUsuarioShouldNotBeFound("direccion.contains=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllUsuariosByDireccionNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where direccion does not contain DEFAULT_DIRECCION
        defaultUsuarioShouldNotBeFound("direccion.doesNotContain=" + DEFAULT_DIRECCION);

        // Get all the usuarioList where direccion does not contain UPDATED_DIRECCION
        defaultUsuarioShouldBeFound("direccion.doesNotContain=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllUsuariosByLocalidadIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where localidad equals to DEFAULT_LOCALIDAD
        defaultUsuarioShouldBeFound("localidad.equals=" + DEFAULT_LOCALIDAD);

        // Get all the usuarioList where localidad equals to UPDATED_LOCALIDAD
        defaultUsuarioShouldNotBeFound("localidad.equals=" + UPDATED_LOCALIDAD);
    }

    @Test
    @Transactional
    void getAllUsuariosByLocalidadIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where localidad not equals to DEFAULT_LOCALIDAD
        defaultUsuarioShouldNotBeFound("localidad.notEquals=" + DEFAULT_LOCALIDAD);

        // Get all the usuarioList where localidad not equals to UPDATED_LOCALIDAD
        defaultUsuarioShouldBeFound("localidad.notEquals=" + UPDATED_LOCALIDAD);
    }

    @Test
    @Transactional
    void getAllUsuariosByLocalidadIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where localidad in DEFAULT_LOCALIDAD or UPDATED_LOCALIDAD
        defaultUsuarioShouldBeFound("localidad.in=" + DEFAULT_LOCALIDAD + "," + UPDATED_LOCALIDAD);

        // Get all the usuarioList where localidad equals to UPDATED_LOCALIDAD
        defaultUsuarioShouldNotBeFound("localidad.in=" + UPDATED_LOCALIDAD);
    }

    @Test
    @Transactional
    void getAllUsuariosByLocalidadIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where localidad is not null
        defaultUsuarioShouldBeFound("localidad.specified=true");

        // Get all the usuarioList where localidad is null
        defaultUsuarioShouldNotBeFound("localidad.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByLocalidadContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where localidad contains DEFAULT_LOCALIDAD
        defaultUsuarioShouldBeFound("localidad.contains=" + DEFAULT_LOCALIDAD);

        // Get all the usuarioList where localidad contains UPDATED_LOCALIDAD
        defaultUsuarioShouldNotBeFound("localidad.contains=" + UPDATED_LOCALIDAD);
    }

    @Test
    @Transactional
    void getAllUsuariosByLocalidadNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where localidad does not contain DEFAULT_LOCALIDAD
        defaultUsuarioShouldNotBeFound("localidad.doesNotContain=" + DEFAULT_LOCALIDAD);

        // Get all the usuarioList where localidad does not contain UPDATED_LOCALIDAD
        defaultUsuarioShouldBeFound("localidad.doesNotContain=" + UPDATED_LOCALIDAD);
    }

    @Test
    @Transactional
    void getAllUsuariosByProvinciaIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where provincia equals to DEFAULT_PROVINCIA
        defaultUsuarioShouldBeFound("provincia.equals=" + DEFAULT_PROVINCIA);

        // Get all the usuarioList where provincia equals to UPDATED_PROVINCIA
        defaultUsuarioShouldNotBeFound("provincia.equals=" + UPDATED_PROVINCIA);
    }

    @Test
    @Transactional
    void getAllUsuariosByProvinciaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where provincia not equals to DEFAULT_PROVINCIA
        defaultUsuarioShouldNotBeFound("provincia.notEquals=" + DEFAULT_PROVINCIA);

        // Get all the usuarioList where provincia not equals to UPDATED_PROVINCIA
        defaultUsuarioShouldBeFound("provincia.notEquals=" + UPDATED_PROVINCIA);
    }

    @Test
    @Transactional
    void getAllUsuariosByProvinciaIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where provincia in DEFAULT_PROVINCIA or UPDATED_PROVINCIA
        defaultUsuarioShouldBeFound("provincia.in=" + DEFAULT_PROVINCIA + "," + UPDATED_PROVINCIA);

        // Get all the usuarioList where provincia equals to UPDATED_PROVINCIA
        defaultUsuarioShouldNotBeFound("provincia.in=" + UPDATED_PROVINCIA);
    }

    @Test
    @Transactional
    void getAllUsuariosByProvinciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where provincia is not null
        defaultUsuarioShouldBeFound("provincia.specified=true");

        // Get all the usuarioList where provincia is null
        defaultUsuarioShouldNotBeFound("provincia.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByProvinciaContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where provincia contains DEFAULT_PROVINCIA
        defaultUsuarioShouldBeFound("provincia.contains=" + DEFAULT_PROVINCIA);

        // Get all the usuarioList where provincia contains UPDATED_PROVINCIA
        defaultUsuarioShouldNotBeFound("provincia.contains=" + UPDATED_PROVINCIA);
    }

    @Test
    @Transactional
    void getAllUsuariosByProvinciaNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where provincia does not contain DEFAULT_PROVINCIA
        defaultUsuarioShouldNotBeFound("provincia.doesNotContain=" + DEFAULT_PROVINCIA);

        // Get all the usuarioList where provincia does not contain UPDATED_PROVINCIA
        defaultUsuarioShouldBeFound("provincia.doesNotContain=" + UPDATED_PROVINCIA);
    }

    @Test
    @Transactional
    void getAllUsuariosByProfesionIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where profesion equals to DEFAULT_PROFESION
        defaultUsuarioShouldBeFound("profesion.equals=" + DEFAULT_PROFESION);

        // Get all the usuarioList where profesion equals to UPDATED_PROFESION
        defaultUsuarioShouldNotBeFound("profesion.equals=" + UPDATED_PROFESION);
    }

    @Test
    @Transactional
    void getAllUsuariosByProfesionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where profesion not equals to DEFAULT_PROFESION
        defaultUsuarioShouldNotBeFound("profesion.notEquals=" + DEFAULT_PROFESION);

        // Get all the usuarioList where profesion not equals to UPDATED_PROFESION
        defaultUsuarioShouldBeFound("profesion.notEquals=" + UPDATED_PROFESION);
    }

    @Test
    @Transactional
    void getAllUsuariosByProfesionIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where profesion in DEFAULT_PROFESION or UPDATED_PROFESION
        defaultUsuarioShouldBeFound("profesion.in=" + DEFAULT_PROFESION + "," + UPDATED_PROFESION);

        // Get all the usuarioList where profesion equals to UPDATED_PROFESION
        defaultUsuarioShouldNotBeFound("profesion.in=" + UPDATED_PROFESION);
    }

    @Test
    @Transactional
    void getAllUsuariosByProfesionIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where profesion is not null
        defaultUsuarioShouldBeFound("profesion.specified=true");

        // Get all the usuarioList where profesion is null
        defaultUsuarioShouldNotBeFound("profesion.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByProfesionContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where profesion contains DEFAULT_PROFESION
        defaultUsuarioShouldBeFound("profesion.contains=" + DEFAULT_PROFESION);

        // Get all the usuarioList where profesion contains UPDATED_PROFESION
        defaultUsuarioShouldNotBeFound("profesion.contains=" + UPDATED_PROFESION);
    }

    @Test
    @Transactional
    void getAllUsuariosByProfesionNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where profesion does not contain DEFAULT_PROFESION
        defaultUsuarioShouldNotBeFound("profesion.doesNotContain=" + DEFAULT_PROFESION);

        // Get all the usuarioList where profesion does not contain UPDATED_PROFESION
        defaultUsuarioShouldBeFound("profesion.doesNotContain=" + UPDATED_PROFESION);
    }

    @Test
    @Transactional
    void getAllUsuariosByFnIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where fn equals to DEFAULT_FN
        defaultUsuarioShouldBeFound("fn.equals=" + DEFAULT_FN);

        // Get all the usuarioList where fn equals to UPDATED_FN
        defaultUsuarioShouldNotBeFound("fn.equals=" + UPDATED_FN);
    }

    @Test
    @Transactional
    void getAllUsuariosByFnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where fn not equals to DEFAULT_FN
        defaultUsuarioShouldNotBeFound("fn.notEquals=" + DEFAULT_FN);

        // Get all the usuarioList where fn not equals to UPDATED_FN
        defaultUsuarioShouldBeFound("fn.notEquals=" + UPDATED_FN);
    }

    @Test
    @Transactional
    void getAllUsuariosByFnIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where fn in DEFAULT_FN or UPDATED_FN
        defaultUsuarioShouldBeFound("fn.in=" + DEFAULT_FN + "," + UPDATED_FN);

        // Get all the usuarioList where fn equals to UPDATED_FN
        defaultUsuarioShouldNotBeFound("fn.in=" + UPDATED_FN);
    }

    @Test
    @Transactional
    void getAllUsuariosByFnIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where fn is not null
        defaultUsuarioShouldBeFound("fn.specified=true");

        // Get all the usuarioList where fn is null
        defaultUsuarioShouldNotBeFound("fn.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByFechaRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where fechaRegistro equals to DEFAULT_FECHA_REGISTRO
        defaultUsuarioShouldBeFound("fechaRegistro.equals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the usuarioList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultUsuarioShouldNotBeFound("fechaRegistro.equals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllUsuariosByFechaRegistroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where fechaRegistro not equals to DEFAULT_FECHA_REGISTRO
        defaultUsuarioShouldNotBeFound("fechaRegistro.notEquals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the usuarioList where fechaRegistro not equals to UPDATED_FECHA_REGISTRO
        defaultUsuarioShouldBeFound("fechaRegistro.notEquals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllUsuariosByFechaRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where fechaRegistro in DEFAULT_FECHA_REGISTRO or UPDATED_FECHA_REGISTRO
        defaultUsuarioShouldBeFound("fechaRegistro.in=" + DEFAULT_FECHA_REGISTRO + "," + UPDATED_FECHA_REGISTRO);

        // Get all the usuarioList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultUsuarioShouldNotBeFound("fechaRegistro.in=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllUsuariosByFechaRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where fechaRegistro is not null
        defaultUsuarioShouldBeFound("fechaRegistro.specified=true");

        // Get all the usuarioList where fechaRegistro is null
        defaultUsuarioShouldNotBeFound("fechaRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = usuario.getUser();
        usuarioRepository.saveAndFlush(usuario);
        Long userId = user.getId();

        // Get all the usuarioList where user equals to userId
        defaultUsuarioShouldBeFound("userId.equals=" + userId);

        // Get all the usuarioList where user equals to (userId + 1)
        defaultUsuarioShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllUsuariosByConversacionIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);
        Conversacion conversacion;
        if (TestUtil.findAll(em, Conversacion.class).isEmpty()) {
            conversacion = ConversacionResourceIT.createEntity(em);
            em.persist(conversacion);
            em.flush();
        } else {
            conversacion = TestUtil.findAll(em, Conversacion.class).get(0);
        }
        em.persist(conversacion);
        em.flush();
        usuario.addConversacion(conversacion);
        usuarioRepository.saveAndFlush(usuario);
        Long conversacionId = conversacion.getId();

        // Get all the usuarioList where conversacion equals to conversacionId
        defaultUsuarioShouldBeFound("conversacionId.equals=" + conversacionId);

        // Get all the usuarioList where conversacion equals to (conversacionId + 1)
        defaultUsuarioShouldNotBeFound("conversacionId.equals=" + (conversacionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUsuarioShouldBeFound(String filter) throws Exception {
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS)))
            .andExpect(jsonPath("$.[*].correo").value(hasItem(DEFAULT_CORREO)))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].localidad").value(hasItem(DEFAULT_LOCALIDAD)))
            .andExpect(jsonPath("$.[*].provincia").value(hasItem(DEFAULT_PROVINCIA)))
            .andExpect(jsonPath("$.[*].profesion").value(hasItem(DEFAULT_PROFESION)))
            .andExpect(jsonPath("$.[*].fn").value(hasItem(DEFAULT_FN.toString())))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));

        // Check, that the count call also returns 1
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUsuarioShouldNotBeFound(String filter) throws Exception {
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUsuario() throws Exception {
        // Get the usuario
        restUsuarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario
        Usuario updatedUsuario = usuarioRepository.findById(usuario.getId()).get();
        // Disconnect from session so that the updates on updatedUsuario are not directly saved in db
        em.detach(updatedUsuario);
        updatedUsuario
            .nombre(UPDATED_NOMBRE)
            .apellidos(UPDATED_APELLIDOS)
            .correo(UPDATED_CORREO)
            .dni(UPDATED_DNI)
            .direccion(UPDATED_DIRECCION)
            .localidad(UPDATED_LOCALIDAD)
            .provincia(UPDATED_PROVINCIA)
            .profesion(UPDATED_PROFESION)
            .fn(UPDATED_FN)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(updatedUsuario);

        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usuarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testUsuario.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testUsuario.getCorreo()).isEqualTo(UPDATED_CORREO);
        assertThat(testUsuario.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testUsuario.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testUsuario.getLocalidad()).isEqualTo(UPDATED_LOCALIDAD);
        assertThat(testUsuario.getProvincia()).isEqualTo(UPDATED_PROVINCIA);
        assertThat(testUsuario.getProfesion()).isEqualTo(UPDATED_PROFESION);
        assertThat(testUsuario.getFn()).isEqualTo(UPDATED_FN);
        assertThat(testUsuario.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void putNonExistingUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, usuarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(usuarioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUsuarioWithPatch() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario using partial update
        Usuario partialUpdatedUsuario = new Usuario();
        partialUpdatedUsuario.setId(usuario.getId());

        partialUpdatedUsuario.nombre(UPDATED_NOMBRE).correo(UPDATED_CORREO).provincia(UPDATED_PROVINCIA);

        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testUsuario.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
        assertThat(testUsuario.getCorreo()).isEqualTo(UPDATED_CORREO);
        assertThat(testUsuario.getDni()).isEqualTo(DEFAULT_DNI);
        assertThat(testUsuario.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testUsuario.getLocalidad()).isEqualTo(DEFAULT_LOCALIDAD);
        assertThat(testUsuario.getProvincia()).isEqualTo(UPDATED_PROVINCIA);
        assertThat(testUsuario.getProfesion()).isEqualTo(DEFAULT_PROFESION);
        assertThat(testUsuario.getFn()).isEqualTo(DEFAULT_FN);
        assertThat(testUsuario.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void fullUpdateUsuarioWithPatch() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();

        // Update the usuario using partial update
        Usuario partialUpdatedUsuario = new Usuario();
        partialUpdatedUsuario.setId(usuario.getId());

        partialUpdatedUsuario
            .nombre(UPDATED_NOMBRE)
            .apellidos(UPDATED_APELLIDOS)
            .correo(UPDATED_CORREO)
            .dni(UPDATED_DNI)
            .direccion(UPDATED_DIRECCION)
            .localidad(UPDATED_LOCALIDAD)
            .provincia(UPDATED_PROVINCIA)
            .profesion(UPDATED_PROFESION)
            .fn(UPDATED_FN)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
        Usuario testUsuario = usuarioList.get(usuarioList.size() - 1);
        assertThat(testUsuario.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testUsuario.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testUsuario.getCorreo()).isEqualTo(UPDATED_CORREO);
        assertThat(testUsuario.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testUsuario.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testUsuario.getLocalidad()).isEqualTo(UPDATED_LOCALIDAD);
        assertThat(testUsuario.getProvincia()).isEqualTo(UPDATED_PROVINCIA);
        assertThat(testUsuario.getProfesion()).isEqualTo(UPDATED_PROFESION);
        assertThat(testUsuario.getFn()).isEqualTo(UPDATED_FN);
        assertThat(testUsuario.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void patchNonExistingUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, usuarioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUsuario() throws Exception {
        int databaseSizeBeforeUpdate = usuarioRepository.findAll().size();
        usuario.setId(count.incrementAndGet());

        // Create the Usuario
        UsuarioDTO usuarioDTO = usuarioMapper.toDto(usuario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(usuarioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Usuario in the database
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        int databaseSizeBeforeDelete = usuarioRepository.findAll().size();

        // Delete the usuario
        restUsuarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, usuario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Usuario> usuarioList = usuarioRepository.findAll();
        assertThat(usuarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
