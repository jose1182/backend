package com.netjob.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.netjob.app.IntegrationTest;
import com.netjob.app.domain.Categoria;
import com.netjob.app.domain.Servicio;
import com.netjob.app.domain.Usuario;
import com.netjob.app.domain.enumeration.Disponibilidad;
import com.netjob.app.repository.ServicioRepository;
import com.netjob.app.service.ServicioService;
import com.netjob.app.service.criteria.ServicioCriteria;
import com.netjob.app.service.dto.ServicioDTO;
import com.netjob.app.service.mapper.ServicioMapper;
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
 * Integration tests for the {@link ServicioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ServicioResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Disponibilidad DEFAULT_DISPONIBILIDAD = Disponibilidad.MANANA;
    private static final Disponibilidad UPDATED_DISPONIBILIDAD = Disponibilidad.TARDE;

    private static final Float DEFAULT_PRECIOHORA = 0F;
    private static final Float UPDATED_PRECIOHORA = 1F;
    private static final Float SMALLER_PRECIOHORA = 0F - 1F;

    private static final Float DEFAULT_PRECIOTRASLADO = 0F;
    private static final Float UPDATED_PRECIOTRASLADO = 1F;
    private static final Float SMALLER_PRECIOTRASLADO = 0F - 1F;

    private static final Instant DEFAULT_FECHACREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHACREACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHAACTUALIZACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHAACTUALIZACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_DESTACADO = false;
    private static final Boolean UPDATED_DESTACADO = true;

    private static final String ENTITY_API_URL = "/api/servicios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ServicioRepository servicioRepository;

    @Mock
    private ServicioRepository servicioRepositoryMock;

    @Autowired
    private ServicioMapper servicioMapper;

    @Mock
    private ServicioService servicioServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServicioMockMvc;

    private Servicio servicio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Servicio createEntity(EntityManager em) {
        Servicio servicio = new Servicio()
            .titulo(DEFAULT_TITULO)
            .descripcion(DEFAULT_DESCRIPCION)
            .disponibilidad(DEFAULT_DISPONIBILIDAD)
            .preciohora(DEFAULT_PRECIOHORA)
            .preciotraslado(DEFAULT_PRECIOTRASLADO)
            .fechacreacion(DEFAULT_FECHACREACION)
            .fechaactualizacion(DEFAULT_FECHAACTUALIZACION)
            .destacado(DEFAULT_DESTACADO);
        return servicio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Servicio createUpdatedEntity(EntityManager em) {
        Servicio servicio = new Servicio()
            .titulo(UPDATED_TITULO)
            .descripcion(UPDATED_DESCRIPCION)
            .disponibilidad(UPDATED_DISPONIBILIDAD)
            .preciohora(UPDATED_PRECIOHORA)
            .preciotraslado(UPDATED_PRECIOTRASLADO)
            .fechacreacion(UPDATED_FECHACREACION)
            .fechaactualizacion(UPDATED_FECHAACTUALIZACION)
            .destacado(UPDATED_DESTACADO);
        return servicio;
    }

    @BeforeEach
    public void initTest() {
        servicio = createEntity(em);
    }

    @Test
    @Transactional
    void createServicio() throws Exception {
        int databaseSizeBeforeCreate = servicioRepository.findAll().size();
        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);
        restServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(servicioDTO)))
            .andExpect(status().isCreated());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeCreate + 1);
        Servicio testServicio = servicioList.get(servicioList.size() - 1);
        assertThat(testServicio.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testServicio.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testServicio.getDisponibilidad()).isEqualTo(DEFAULT_DISPONIBILIDAD);
        assertThat(testServicio.getPreciohora()).isEqualTo(DEFAULT_PRECIOHORA);
        assertThat(testServicio.getPreciotraslado()).isEqualTo(DEFAULT_PRECIOTRASLADO);
        assertThat(testServicio.getFechacreacion()).isEqualTo(DEFAULT_FECHACREACION);
        assertThat(testServicio.getFechaactualizacion()).isEqualTo(DEFAULT_FECHAACTUALIZACION);
        assertThat(testServicio.getDestacado()).isEqualTo(DEFAULT_DESTACADO);
    }

    @Test
    @Transactional
    void createServicioWithExistingId() throws Exception {
        // Create the Servicio with an existing ID
        servicio.setId(1L);
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        int databaseSizeBeforeCreate = servicioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(servicioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTituloIsRequired() throws Exception {
        int databaseSizeBeforeTest = servicioRepository.findAll().size();
        // set the field null
        servicio.setTitulo(null);

        // Create the Servicio, which fails.
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        restServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(servicioDTO)))
            .andExpect(status().isBadRequest());

        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = servicioRepository.findAll().size();
        // set the field null
        servicio.setDescripcion(null);

        // Create the Servicio, which fails.
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        restServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(servicioDTO)))
            .andExpect(status().isBadRequest());

        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDisponibilidadIsRequired() throws Exception {
        int databaseSizeBeforeTest = servicioRepository.findAll().size();
        // set the field null
        servicio.setDisponibilidad(null);

        // Create the Servicio, which fails.
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        restServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(servicioDTO)))
            .andExpect(status().isBadRequest());

        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPreciohoraIsRequired() throws Exception {
        int databaseSizeBeforeTest = servicioRepository.findAll().size();
        // set the field null
        servicio.setPreciohora(null);

        // Create the Servicio, which fails.
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        restServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(servicioDTO)))
            .andExpect(status().isBadRequest());

        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPreciotrasladoIsRequired() throws Exception {
        int databaseSizeBeforeTest = servicioRepository.findAll().size();
        // set the field null
        servicio.setPreciotraslado(null);

        // Create the Servicio, which fails.
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        restServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(servicioDTO)))
            .andExpect(status().isBadRequest());

        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechacreacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = servicioRepository.findAll().size();
        // set the field null
        servicio.setFechacreacion(null);

        // Create the Servicio, which fails.
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        restServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(servicioDTO)))
            .andExpect(status().isBadRequest());

        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaactualizacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = servicioRepository.findAll().size();
        // set the field null
        servicio.setFechaactualizacion(null);

        // Create the Servicio, which fails.
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        restServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(servicioDTO)))
            .andExpect(status().isBadRequest());

        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDestacadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = servicioRepository.findAll().size();
        // set the field null
        servicio.setDestacado(null);

        // Create the Servicio, which fails.
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        restServicioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(servicioDTO)))
            .andExpect(status().isBadRequest());

        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServicios() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList
        restServicioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servicio.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].disponibilidad").value(hasItem(DEFAULT_DISPONIBILIDAD.toString())))
            .andExpect(jsonPath("$.[*].preciohora").value(hasItem(DEFAULT_PRECIOHORA.doubleValue())))
            .andExpect(jsonPath("$.[*].preciotraslado").value(hasItem(DEFAULT_PRECIOTRASLADO.doubleValue())))
            .andExpect(jsonPath("$.[*].fechacreacion").value(hasItem(DEFAULT_FECHACREACION.toString())))
            .andExpect(jsonPath("$.[*].fechaactualizacion").value(hasItem(DEFAULT_FECHAACTUALIZACION.toString())))
            .andExpect(jsonPath("$.[*].destacado").value(hasItem(DEFAULT_DESTACADO.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServiciosWithEagerRelationshipsIsEnabled() throws Exception {
        when(servicioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServicioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(servicioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllServiciosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(servicioServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restServicioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(servicioServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getServicio() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get the servicio
        restServicioMockMvc
            .perform(get(ENTITY_API_URL_ID, servicio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(servicio.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.disponibilidad").value(DEFAULT_DISPONIBILIDAD.toString()))
            .andExpect(jsonPath("$.preciohora").value(DEFAULT_PRECIOHORA.doubleValue()))
            .andExpect(jsonPath("$.preciotraslado").value(DEFAULT_PRECIOTRASLADO.doubleValue()))
            .andExpect(jsonPath("$.fechacreacion").value(DEFAULT_FECHACREACION.toString()))
            .andExpect(jsonPath("$.fechaactualizacion").value(DEFAULT_FECHAACTUALIZACION.toString()))
            .andExpect(jsonPath("$.destacado").value(DEFAULT_DESTACADO.booleanValue()));
    }

    @Test
    @Transactional
    void getServiciosByIdFiltering() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        Long id = servicio.getId();

        defaultServicioShouldBeFound("id.equals=" + id);
        defaultServicioShouldNotBeFound("id.notEquals=" + id);

        defaultServicioShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultServicioShouldNotBeFound("id.greaterThan=" + id);

        defaultServicioShouldBeFound("id.lessThanOrEqual=" + id);
        defaultServicioShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllServiciosByTituloIsEqualToSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where titulo equals to DEFAULT_TITULO
        defaultServicioShouldBeFound("titulo.equals=" + DEFAULT_TITULO);

        // Get all the servicioList where titulo equals to UPDATED_TITULO
        defaultServicioShouldNotBeFound("titulo.equals=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllServiciosByTituloIsNotEqualToSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where titulo not equals to DEFAULT_TITULO
        defaultServicioShouldNotBeFound("titulo.notEquals=" + DEFAULT_TITULO);

        // Get all the servicioList where titulo not equals to UPDATED_TITULO
        defaultServicioShouldBeFound("titulo.notEquals=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllServiciosByTituloIsInShouldWork() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where titulo in DEFAULT_TITULO or UPDATED_TITULO
        defaultServicioShouldBeFound("titulo.in=" + DEFAULT_TITULO + "," + UPDATED_TITULO);

        // Get all the servicioList where titulo equals to UPDATED_TITULO
        defaultServicioShouldNotBeFound("titulo.in=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllServiciosByTituloIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where titulo is not null
        defaultServicioShouldBeFound("titulo.specified=true");

        // Get all the servicioList where titulo is null
        defaultServicioShouldNotBeFound("titulo.specified=false");
    }

    @Test
    @Transactional
    void getAllServiciosByTituloContainsSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where titulo contains DEFAULT_TITULO
        defaultServicioShouldBeFound("titulo.contains=" + DEFAULT_TITULO);

        // Get all the servicioList where titulo contains UPDATED_TITULO
        defaultServicioShouldNotBeFound("titulo.contains=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllServiciosByTituloNotContainsSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where titulo does not contain DEFAULT_TITULO
        defaultServicioShouldNotBeFound("titulo.doesNotContain=" + DEFAULT_TITULO);

        // Get all the servicioList where titulo does not contain UPDATED_TITULO
        defaultServicioShouldBeFound("titulo.doesNotContain=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllServiciosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where descripcion equals to DEFAULT_DESCRIPCION
        defaultServicioShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the servicioList where descripcion equals to UPDATED_DESCRIPCION
        defaultServicioShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllServiciosByDescripcionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where descripcion not equals to DEFAULT_DESCRIPCION
        defaultServicioShouldNotBeFound("descripcion.notEquals=" + DEFAULT_DESCRIPCION);

        // Get all the servicioList where descripcion not equals to UPDATED_DESCRIPCION
        defaultServicioShouldBeFound("descripcion.notEquals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllServiciosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultServicioShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the servicioList where descripcion equals to UPDATED_DESCRIPCION
        defaultServicioShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllServiciosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where descripcion is not null
        defaultServicioShouldBeFound("descripcion.specified=true");

        // Get all the servicioList where descripcion is null
        defaultServicioShouldNotBeFound("descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllServiciosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where descripcion contains DEFAULT_DESCRIPCION
        defaultServicioShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the servicioList where descripcion contains UPDATED_DESCRIPCION
        defaultServicioShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllServiciosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultServicioShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the servicioList where descripcion does not contain UPDATED_DESCRIPCION
        defaultServicioShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllServiciosByDisponibilidadIsEqualToSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where disponibilidad equals to DEFAULT_DISPONIBILIDAD
        defaultServicioShouldBeFound("disponibilidad.equals=" + DEFAULT_DISPONIBILIDAD);

        // Get all the servicioList where disponibilidad equals to UPDATED_DISPONIBILIDAD
        defaultServicioShouldNotBeFound("disponibilidad.equals=" + UPDATED_DISPONIBILIDAD);
    }

    @Test
    @Transactional
    void getAllServiciosByDisponibilidadIsNotEqualToSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where disponibilidad not equals to DEFAULT_DISPONIBILIDAD
        defaultServicioShouldNotBeFound("disponibilidad.notEquals=" + DEFAULT_DISPONIBILIDAD);

        // Get all the servicioList where disponibilidad not equals to UPDATED_DISPONIBILIDAD
        defaultServicioShouldBeFound("disponibilidad.notEquals=" + UPDATED_DISPONIBILIDAD);
    }

    @Test
    @Transactional
    void getAllServiciosByDisponibilidadIsInShouldWork() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where disponibilidad in DEFAULT_DISPONIBILIDAD or UPDATED_DISPONIBILIDAD
        defaultServicioShouldBeFound("disponibilidad.in=" + DEFAULT_DISPONIBILIDAD + "," + UPDATED_DISPONIBILIDAD);

        // Get all the servicioList where disponibilidad equals to UPDATED_DISPONIBILIDAD
        defaultServicioShouldNotBeFound("disponibilidad.in=" + UPDATED_DISPONIBILIDAD);
    }

    @Test
    @Transactional
    void getAllServiciosByDisponibilidadIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where disponibilidad is not null
        defaultServicioShouldBeFound("disponibilidad.specified=true");

        // Get all the servicioList where disponibilidad is null
        defaultServicioShouldNotBeFound("disponibilidad.specified=false");
    }

    @Test
    @Transactional
    void getAllServiciosByPreciohoraIsEqualToSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where preciohora equals to DEFAULT_PRECIOHORA
        defaultServicioShouldBeFound("preciohora.equals=" + DEFAULT_PRECIOHORA);

        // Get all the servicioList where preciohora equals to UPDATED_PRECIOHORA
        defaultServicioShouldNotBeFound("preciohora.equals=" + UPDATED_PRECIOHORA);
    }

    @Test
    @Transactional
    void getAllServiciosByPreciohoraIsNotEqualToSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where preciohora not equals to DEFAULT_PRECIOHORA
        defaultServicioShouldNotBeFound("preciohora.notEquals=" + DEFAULT_PRECIOHORA);

        // Get all the servicioList where preciohora not equals to UPDATED_PRECIOHORA
        defaultServicioShouldBeFound("preciohora.notEquals=" + UPDATED_PRECIOHORA);
    }

    @Test
    @Transactional
    void getAllServiciosByPreciohoraIsInShouldWork() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where preciohora in DEFAULT_PRECIOHORA or UPDATED_PRECIOHORA
        defaultServicioShouldBeFound("preciohora.in=" + DEFAULT_PRECIOHORA + "," + UPDATED_PRECIOHORA);

        // Get all the servicioList where preciohora equals to UPDATED_PRECIOHORA
        defaultServicioShouldNotBeFound("preciohora.in=" + UPDATED_PRECIOHORA);
    }

    @Test
    @Transactional
    void getAllServiciosByPreciohoraIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where preciohora is not null
        defaultServicioShouldBeFound("preciohora.specified=true");

        // Get all the servicioList where preciohora is null
        defaultServicioShouldNotBeFound("preciohora.specified=false");
    }

    @Test
    @Transactional
    void getAllServiciosByPreciohoraIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where preciohora is greater than or equal to DEFAULT_PRECIOHORA
        defaultServicioShouldBeFound("preciohora.greaterThanOrEqual=" + DEFAULT_PRECIOHORA);

        // Get all the servicioList where preciohora is greater than or equal to UPDATED_PRECIOHORA
        defaultServicioShouldNotBeFound("preciohora.greaterThanOrEqual=" + UPDATED_PRECIOHORA);
    }

    @Test
    @Transactional
    void getAllServiciosByPreciohoraIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where preciohora is less than or equal to DEFAULT_PRECIOHORA
        defaultServicioShouldBeFound("preciohora.lessThanOrEqual=" + DEFAULT_PRECIOHORA);

        // Get all the servicioList where preciohora is less than or equal to SMALLER_PRECIOHORA
        defaultServicioShouldNotBeFound("preciohora.lessThanOrEqual=" + SMALLER_PRECIOHORA);
    }

    @Test
    @Transactional
    void getAllServiciosByPreciohoraIsLessThanSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where preciohora is less than DEFAULT_PRECIOHORA
        defaultServicioShouldNotBeFound("preciohora.lessThan=" + DEFAULT_PRECIOHORA);

        // Get all the servicioList where preciohora is less than UPDATED_PRECIOHORA
        defaultServicioShouldBeFound("preciohora.lessThan=" + UPDATED_PRECIOHORA);
    }

    @Test
    @Transactional
    void getAllServiciosByPreciohoraIsGreaterThanSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where preciohora is greater than DEFAULT_PRECIOHORA
        defaultServicioShouldNotBeFound("preciohora.greaterThan=" + DEFAULT_PRECIOHORA);

        // Get all the servicioList where preciohora is greater than SMALLER_PRECIOHORA
        defaultServicioShouldBeFound("preciohora.greaterThan=" + SMALLER_PRECIOHORA);
    }

    @Test
    @Transactional
    void getAllServiciosByPreciotrasladoIsEqualToSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where preciotraslado equals to DEFAULT_PRECIOTRASLADO
        defaultServicioShouldBeFound("preciotraslado.equals=" + DEFAULT_PRECIOTRASLADO);

        // Get all the servicioList where preciotraslado equals to UPDATED_PRECIOTRASLADO
        defaultServicioShouldNotBeFound("preciotraslado.equals=" + UPDATED_PRECIOTRASLADO);
    }

    @Test
    @Transactional
    void getAllServiciosByPreciotrasladoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where preciotraslado not equals to DEFAULT_PRECIOTRASLADO
        defaultServicioShouldNotBeFound("preciotraslado.notEquals=" + DEFAULT_PRECIOTRASLADO);

        // Get all the servicioList where preciotraslado not equals to UPDATED_PRECIOTRASLADO
        defaultServicioShouldBeFound("preciotraslado.notEquals=" + UPDATED_PRECIOTRASLADO);
    }

    @Test
    @Transactional
    void getAllServiciosByPreciotrasladoIsInShouldWork() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where preciotraslado in DEFAULT_PRECIOTRASLADO or UPDATED_PRECIOTRASLADO
        defaultServicioShouldBeFound("preciotraslado.in=" + DEFAULT_PRECIOTRASLADO + "," + UPDATED_PRECIOTRASLADO);

        // Get all the servicioList where preciotraslado equals to UPDATED_PRECIOTRASLADO
        defaultServicioShouldNotBeFound("preciotraslado.in=" + UPDATED_PRECIOTRASLADO);
    }

    @Test
    @Transactional
    void getAllServiciosByPreciotrasladoIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where preciotraslado is not null
        defaultServicioShouldBeFound("preciotraslado.specified=true");

        // Get all the servicioList where preciotraslado is null
        defaultServicioShouldNotBeFound("preciotraslado.specified=false");
    }

    @Test
    @Transactional
    void getAllServiciosByPreciotrasladoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where preciotraslado is greater than or equal to DEFAULT_PRECIOTRASLADO
        defaultServicioShouldBeFound("preciotraslado.greaterThanOrEqual=" + DEFAULT_PRECIOTRASLADO);

        // Get all the servicioList where preciotraslado is greater than or equal to UPDATED_PRECIOTRASLADO
        defaultServicioShouldNotBeFound("preciotraslado.greaterThanOrEqual=" + UPDATED_PRECIOTRASLADO);
    }

    @Test
    @Transactional
    void getAllServiciosByPreciotrasladoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where preciotraslado is less than or equal to DEFAULT_PRECIOTRASLADO
        defaultServicioShouldBeFound("preciotraslado.lessThanOrEqual=" + DEFAULT_PRECIOTRASLADO);

        // Get all the servicioList where preciotraslado is less than or equal to SMALLER_PRECIOTRASLADO
        defaultServicioShouldNotBeFound("preciotraslado.lessThanOrEqual=" + SMALLER_PRECIOTRASLADO);
    }

    @Test
    @Transactional
    void getAllServiciosByPreciotrasladoIsLessThanSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where preciotraslado is less than DEFAULT_PRECIOTRASLADO
        defaultServicioShouldNotBeFound("preciotraslado.lessThan=" + DEFAULT_PRECIOTRASLADO);

        // Get all the servicioList where preciotraslado is less than UPDATED_PRECIOTRASLADO
        defaultServicioShouldBeFound("preciotraslado.lessThan=" + UPDATED_PRECIOTRASLADO);
    }

    @Test
    @Transactional
    void getAllServiciosByPreciotrasladoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where preciotraslado is greater than DEFAULT_PRECIOTRASLADO
        defaultServicioShouldNotBeFound("preciotraslado.greaterThan=" + DEFAULT_PRECIOTRASLADO);

        // Get all the servicioList where preciotraslado is greater than SMALLER_PRECIOTRASLADO
        defaultServicioShouldBeFound("preciotraslado.greaterThan=" + SMALLER_PRECIOTRASLADO);
    }

    @Test
    @Transactional
    void getAllServiciosByFechacreacionIsEqualToSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where fechacreacion equals to DEFAULT_FECHACREACION
        defaultServicioShouldBeFound("fechacreacion.equals=" + DEFAULT_FECHACREACION);

        // Get all the servicioList where fechacreacion equals to UPDATED_FECHACREACION
        defaultServicioShouldNotBeFound("fechacreacion.equals=" + UPDATED_FECHACREACION);
    }

    @Test
    @Transactional
    void getAllServiciosByFechacreacionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where fechacreacion not equals to DEFAULT_FECHACREACION
        defaultServicioShouldNotBeFound("fechacreacion.notEquals=" + DEFAULT_FECHACREACION);

        // Get all the servicioList where fechacreacion not equals to UPDATED_FECHACREACION
        defaultServicioShouldBeFound("fechacreacion.notEquals=" + UPDATED_FECHACREACION);
    }

    @Test
    @Transactional
    void getAllServiciosByFechacreacionIsInShouldWork() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where fechacreacion in DEFAULT_FECHACREACION or UPDATED_FECHACREACION
        defaultServicioShouldBeFound("fechacreacion.in=" + DEFAULT_FECHACREACION + "," + UPDATED_FECHACREACION);

        // Get all the servicioList where fechacreacion equals to UPDATED_FECHACREACION
        defaultServicioShouldNotBeFound("fechacreacion.in=" + UPDATED_FECHACREACION);
    }

    @Test
    @Transactional
    void getAllServiciosByFechacreacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where fechacreacion is not null
        defaultServicioShouldBeFound("fechacreacion.specified=true");

        // Get all the servicioList where fechacreacion is null
        defaultServicioShouldNotBeFound("fechacreacion.specified=false");
    }

    @Test
    @Transactional
    void getAllServiciosByFechaactualizacionIsEqualToSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where fechaactualizacion equals to DEFAULT_FECHAACTUALIZACION
        defaultServicioShouldBeFound("fechaactualizacion.equals=" + DEFAULT_FECHAACTUALIZACION);

        // Get all the servicioList where fechaactualizacion equals to UPDATED_FECHAACTUALIZACION
        defaultServicioShouldNotBeFound("fechaactualizacion.equals=" + UPDATED_FECHAACTUALIZACION);
    }

    @Test
    @Transactional
    void getAllServiciosByFechaactualizacionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where fechaactualizacion not equals to DEFAULT_FECHAACTUALIZACION
        defaultServicioShouldNotBeFound("fechaactualizacion.notEquals=" + DEFAULT_FECHAACTUALIZACION);

        // Get all the servicioList where fechaactualizacion not equals to UPDATED_FECHAACTUALIZACION
        defaultServicioShouldBeFound("fechaactualizacion.notEquals=" + UPDATED_FECHAACTUALIZACION);
    }

    @Test
    @Transactional
    void getAllServiciosByFechaactualizacionIsInShouldWork() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where fechaactualizacion in DEFAULT_FECHAACTUALIZACION or UPDATED_FECHAACTUALIZACION
        defaultServicioShouldBeFound("fechaactualizacion.in=" + DEFAULT_FECHAACTUALIZACION + "," + UPDATED_FECHAACTUALIZACION);

        // Get all the servicioList where fechaactualizacion equals to UPDATED_FECHAACTUALIZACION
        defaultServicioShouldNotBeFound("fechaactualizacion.in=" + UPDATED_FECHAACTUALIZACION);
    }

    @Test
    @Transactional
    void getAllServiciosByFechaactualizacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where fechaactualizacion is not null
        defaultServicioShouldBeFound("fechaactualizacion.specified=true");

        // Get all the servicioList where fechaactualizacion is null
        defaultServicioShouldNotBeFound("fechaactualizacion.specified=false");
    }

    @Test
    @Transactional
    void getAllServiciosByDestacadoIsEqualToSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where destacado equals to DEFAULT_DESTACADO
        defaultServicioShouldBeFound("destacado.equals=" + DEFAULT_DESTACADO);

        // Get all the servicioList where destacado equals to UPDATED_DESTACADO
        defaultServicioShouldNotBeFound("destacado.equals=" + UPDATED_DESTACADO);
    }

    @Test
    @Transactional
    void getAllServiciosByDestacadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where destacado not equals to DEFAULT_DESTACADO
        defaultServicioShouldNotBeFound("destacado.notEquals=" + DEFAULT_DESTACADO);

        // Get all the servicioList where destacado not equals to UPDATED_DESTACADO
        defaultServicioShouldBeFound("destacado.notEquals=" + UPDATED_DESTACADO);
    }

    @Test
    @Transactional
    void getAllServiciosByDestacadoIsInShouldWork() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where destacado in DEFAULT_DESTACADO or UPDATED_DESTACADO
        defaultServicioShouldBeFound("destacado.in=" + DEFAULT_DESTACADO + "," + UPDATED_DESTACADO);

        // Get all the servicioList where destacado equals to UPDATED_DESTACADO
        defaultServicioShouldNotBeFound("destacado.in=" + UPDATED_DESTACADO);
    }

    @Test
    @Transactional
    void getAllServiciosByDestacadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        // Get all the servicioList where destacado is not null
        defaultServicioShouldBeFound("destacado.specified=true");

        // Get all the servicioList where destacado is null
        defaultServicioShouldNotBeFound("destacado.specified=false");
    }

    @Test
    @Transactional
    void getAllServiciosByUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            usuario = UsuarioResourceIT.createEntity(em);
            em.persist(usuario);
            em.flush();
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).get(0);
        }
        em.persist(usuario);
        em.flush();
        servicio.setUsuario(usuario);
        servicioRepository.saveAndFlush(servicio);
        Long usuarioId = usuario.getId();

        // Get all the servicioList where usuario equals to usuarioId
        defaultServicioShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the servicioList where usuario equals to (usuarioId + 1)
        defaultServicioShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    @Test
    @Transactional
    void getAllServiciosByCategoriaIsEqualToSomething() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);
        Categoria categoria;
        if (TestUtil.findAll(em, Categoria.class).isEmpty()) {
            categoria = CategoriaResourceIT.createEntity(em);
            em.persist(categoria);
            em.flush();
        } else {
            categoria = TestUtil.findAll(em, Categoria.class).get(0);
        }
        em.persist(categoria);
        em.flush();
        servicio.addCategoria(categoria);
        servicioRepository.saveAndFlush(servicio);
        Long categoriaId = categoria.getId();

        // Get all the servicioList where categoria equals to categoriaId
        defaultServicioShouldBeFound("categoriaId.equals=" + categoriaId);

        // Get all the servicioList where categoria equals to (categoriaId + 1)
        defaultServicioShouldNotBeFound("categoriaId.equals=" + (categoriaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultServicioShouldBeFound(String filter) throws Exception {
        restServicioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servicio.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].disponibilidad").value(hasItem(DEFAULT_DISPONIBILIDAD.toString())))
            .andExpect(jsonPath("$.[*].preciohora").value(hasItem(DEFAULT_PRECIOHORA.doubleValue())))
            .andExpect(jsonPath("$.[*].preciotraslado").value(hasItem(DEFAULT_PRECIOTRASLADO.doubleValue())))
            .andExpect(jsonPath("$.[*].fechacreacion").value(hasItem(DEFAULT_FECHACREACION.toString())))
            .andExpect(jsonPath("$.[*].fechaactualizacion").value(hasItem(DEFAULT_FECHAACTUALIZACION.toString())))
            .andExpect(jsonPath("$.[*].destacado").value(hasItem(DEFAULT_DESTACADO.booleanValue())));

        // Check, that the count call also returns 1
        restServicioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultServicioShouldNotBeFound(String filter) throws Exception {
        restServicioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restServicioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingServicio() throws Exception {
        // Get the servicio
        restServicioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewServicio() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        int databaseSizeBeforeUpdate = servicioRepository.findAll().size();

        // Update the servicio
        Servicio updatedServicio = servicioRepository.findById(servicio.getId()).get();
        // Disconnect from session so that the updates on updatedServicio are not directly saved in db
        em.detach(updatedServicio);
        updatedServicio
            .titulo(UPDATED_TITULO)
            .descripcion(UPDATED_DESCRIPCION)
            .disponibilidad(UPDATED_DISPONIBILIDAD)
            .preciohora(UPDATED_PRECIOHORA)
            .preciotraslado(UPDATED_PRECIOTRASLADO)
            .fechacreacion(UPDATED_FECHACREACION)
            .fechaactualizacion(UPDATED_FECHAACTUALIZACION)
            .destacado(UPDATED_DESTACADO);
        ServicioDTO servicioDTO = servicioMapper.toDto(updatedServicio);

        restServicioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, servicioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(servicioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeUpdate);
        Servicio testServicio = servicioList.get(servicioList.size() - 1);
        assertThat(testServicio.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testServicio.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testServicio.getDisponibilidad()).isEqualTo(UPDATED_DISPONIBILIDAD);
        assertThat(testServicio.getPreciohora()).isEqualTo(UPDATED_PRECIOHORA);
        assertThat(testServicio.getPreciotraslado()).isEqualTo(UPDATED_PRECIOTRASLADO);
        assertThat(testServicio.getFechacreacion()).isEqualTo(UPDATED_FECHACREACION);
        assertThat(testServicio.getFechaactualizacion()).isEqualTo(UPDATED_FECHAACTUALIZACION);
        assertThat(testServicio.getDestacado()).isEqualTo(UPDATED_DESTACADO);
    }

    @Test
    @Transactional
    void putNonExistingServicio() throws Exception {
        int databaseSizeBeforeUpdate = servicioRepository.findAll().size();
        servicio.setId(count.incrementAndGet());

        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, servicioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(servicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServicio() throws Exception {
        int databaseSizeBeforeUpdate = servicioRepository.findAll().size();
        servicio.setId(count.incrementAndGet());

        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(servicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServicio() throws Exception {
        int databaseSizeBeforeUpdate = servicioRepository.findAll().size();
        servicio.setId(count.incrementAndGet());

        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(servicioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServicioWithPatch() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        int databaseSizeBeforeUpdate = servicioRepository.findAll().size();

        // Update the servicio using partial update
        Servicio partialUpdatedServicio = new Servicio();
        partialUpdatedServicio.setId(servicio.getId());

        partialUpdatedServicio.titulo(UPDATED_TITULO).preciotraslado(UPDATED_PRECIOTRASLADO);

        restServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServicio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServicio))
            )
            .andExpect(status().isOk());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeUpdate);
        Servicio testServicio = servicioList.get(servicioList.size() - 1);
        assertThat(testServicio.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testServicio.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testServicio.getDisponibilidad()).isEqualTo(DEFAULT_DISPONIBILIDAD);
        assertThat(testServicio.getPreciohora()).isEqualTo(DEFAULT_PRECIOHORA);
        assertThat(testServicio.getPreciotraslado()).isEqualTo(UPDATED_PRECIOTRASLADO);
        assertThat(testServicio.getFechacreacion()).isEqualTo(DEFAULT_FECHACREACION);
        assertThat(testServicio.getFechaactualizacion()).isEqualTo(DEFAULT_FECHAACTUALIZACION);
        assertThat(testServicio.getDestacado()).isEqualTo(DEFAULT_DESTACADO);
    }

    @Test
    @Transactional
    void fullUpdateServicioWithPatch() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        int databaseSizeBeforeUpdate = servicioRepository.findAll().size();

        // Update the servicio using partial update
        Servicio partialUpdatedServicio = new Servicio();
        partialUpdatedServicio.setId(servicio.getId());

        partialUpdatedServicio
            .titulo(UPDATED_TITULO)
            .descripcion(UPDATED_DESCRIPCION)
            .disponibilidad(UPDATED_DISPONIBILIDAD)
            .preciohora(UPDATED_PRECIOHORA)
            .preciotraslado(UPDATED_PRECIOTRASLADO)
            .fechacreacion(UPDATED_FECHACREACION)
            .fechaactualizacion(UPDATED_FECHAACTUALIZACION)
            .destacado(UPDATED_DESTACADO);

        restServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServicio.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedServicio))
            )
            .andExpect(status().isOk());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeUpdate);
        Servicio testServicio = servicioList.get(servicioList.size() - 1);
        assertThat(testServicio.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testServicio.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testServicio.getDisponibilidad()).isEqualTo(UPDATED_DISPONIBILIDAD);
        assertThat(testServicio.getPreciohora()).isEqualTo(UPDATED_PRECIOHORA);
        assertThat(testServicio.getPreciotraslado()).isEqualTo(UPDATED_PRECIOTRASLADO);
        assertThat(testServicio.getFechacreacion()).isEqualTo(UPDATED_FECHACREACION);
        assertThat(testServicio.getFechaactualizacion()).isEqualTo(UPDATED_FECHAACTUALIZACION);
        assertThat(testServicio.getDestacado()).isEqualTo(UPDATED_DESTACADO);
    }

    @Test
    @Transactional
    void patchNonExistingServicio() throws Exception {
        int databaseSizeBeforeUpdate = servicioRepository.findAll().size();
        servicio.setId(count.incrementAndGet());

        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, servicioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(servicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServicio() throws Exception {
        int databaseSizeBeforeUpdate = servicioRepository.findAll().size();
        servicio.setId(count.incrementAndGet());

        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(servicioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServicio() throws Exception {
        int databaseSizeBeforeUpdate = servicioRepository.findAll().size();
        servicio.setId(count.incrementAndGet());

        // Create the Servicio
        ServicioDTO servicioDTO = servicioMapper.toDto(servicio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServicioMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(servicioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Servicio in the database
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServicio() throws Exception {
        // Initialize the database
        servicioRepository.saveAndFlush(servicio);

        int databaseSizeBeforeDelete = servicioRepository.findAll().size();

        // Delete the servicio
        restServicioMockMvc
            .perform(delete(ENTITY_API_URL_ID, servicio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Servicio> servicioList = servicioRepository.findAll();
        assertThat(servicioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
