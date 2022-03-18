package com.netjob.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.netjob.app.IntegrationTest;
import com.netjob.app.domain.Valoracion;
import com.netjob.app.repository.ValoracionRepository;
import com.netjob.app.service.criteria.ValoracionCriteria;
import com.netjob.app.service.dto.ValoracionDTO;
import com.netjob.app.service.mapper.ValoracionMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ValoracionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ValoracionResourceIT {

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_ID_SERVICIO = 1;
    private static final Integer UPDATED_ID_SERVICIO = 2;
    private static final Integer SMALLER_ID_SERVICIO = 1 - 1;

    private static final String ENTITY_API_URL = "/api/valoracions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ValoracionRepository valoracionRepository;

    @Autowired
    private ValoracionMapper valoracionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restValoracionMockMvc;

    private Valoracion valoracion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Valoracion createEntity(EntityManager em) {
        Valoracion valoracion = new Valoracion().descripcion(DEFAULT_DESCRIPCION).fecha(DEFAULT_FECHA).id_servicio(DEFAULT_ID_SERVICIO);
        return valoracion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Valoracion createUpdatedEntity(EntityManager em) {
        Valoracion valoracion = new Valoracion().descripcion(UPDATED_DESCRIPCION).fecha(UPDATED_FECHA).id_servicio(UPDATED_ID_SERVICIO);
        return valoracion;
    }

    @BeforeEach
    public void initTest() {
        valoracion = createEntity(em);
    }

    @Test
    @Transactional
    void createValoracion() throws Exception {
        int databaseSizeBeforeCreate = valoracionRepository.findAll().size();
        // Create the Valoracion
        ValoracionDTO valoracionDTO = valoracionMapper.toDto(valoracion);
        restValoracionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(valoracionDTO)))
            .andExpect(status().isCreated());

        // Validate the Valoracion in the database
        List<Valoracion> valoracionList = valoracionRepository.findAll();
        assertThat(valoracionList).hasSize(databaseSizeBeforeCreate + 1);
        Valoracion testValoracion = valoracionList.get(valoracionList.size() - 1);
        assertThat(testValoracion.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testValoracion.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testValoracion.getId_servicio()).isEqualTo(DEFAULT_ID_SERVICIO);
    }

    @Test
    @Transactional
    void createValoracionWithExistingId() throws Exception {
        // Create the Valoracion with an existing ID
        valoracion.setId(1L);
        ValoracionDTO valoracionDTO = valoracionMapper.toDto(valoracion);

        int databaseSizeBeforeCreate = valoracionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restValoracionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(valoracionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Valoracion in the database
        List<Valoracion> valoracionList = valoracionRepository.findAll();
        assertThat(valoracionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = valoracionRepository.findAll().size();
        // set the field null
        valoracion.setDescripcion(null);

        // Create the Valoracion, which fails.
        ValoracionDTO valoracionDTO = valoracionMapper.toDto(valoracion);

        restValoracionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(valoracionDTO)))
            .andExpect(status().isBadRequest());

        List<Valoracion> valoracionList = valoracionRepository.findAll();
        assertThat(valoracionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = valoracionRepository.findAll().size();
        // set the field null
        valoracion.setFecha(null);

        // Create the Valoracion, which fails.
        ValoracionDTO valoracionDTO = valoracionMapper.toDto(valoracion);

        restValoracionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(valoracionDTO)))
            .andExpect(status().isBadRequest());

        List<Valoracion> valoracionList = valoracionRepository.findAll();
        assertThat(valoracionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllValoracions() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        // Get all the valoracionList
        restValoracionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(valoracion.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].id_servicio").value(hasItem(DEFAULT_ID_SERVICIO)));
    }

    @Test
    @Transactional
    void getValoracion() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        // Get the valoracion
        restValoracionMockMvc
            .perform(get(ENTITY_API_URL_ID, valoracion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(valoracion.getId().intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.id_servicio").value(DEFAULT_ID_SERVICIO));
    }

    @Test
    @Transactional
    void getValoracionsByIdFiltering() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        Long id = valoracion.getId();

        defaultValoracionShouldBeFound("id.equals=" + id);
        defaultValoracionShouldNotBeFound("id.notEquals=" + id);

        defaultValoracionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultValoracionShouldNotBeFound("id.greaterThan=" + id);

        defaultValoracionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultValoracionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllValoracionsByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        // Get all the valoracionList where descripcion equals to DEFAULT_DESCRIPCION
        defaultValoracionShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the valoracionList where descripcion equals to UPDATED_DESCRIPCION
        defaultValoracionShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllValoracionsByDescripcionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        // Get all the valoracionList where descripcion not equals to DEFAULT_DESCRIPCION
        defaultValoracionShouldNotBeFound("descripcion.notEquals=" + DEFAULT_DESCRIPCION);

        // Get all the valoracionList where descripcion not equals to UPDATED_DESCRIPCION
        defaultValoracionShouldBeFound("descripcion.notEquals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllValoracionsByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        // Get all the valoracionList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultValoracionShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the valoracionList where descripcion equals to UPDATED_DESCRIPCION
        defaultValoracionShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllValoracionsByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        // Get all the valoracionList where descripcion is not null
        defaultValoracionShouldBeFound("descripcion.specified=true");

        // Get all the valoracionList where descripcion is null
        defaultValoracionShouldNotBeFound("descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllValoracionsByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        // Get all the valoracionList where descripcion contains DEFAULT_DESCRIPCION
        defaultValoracionShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the valoracionList where descripcion contains UPDATED_DESCRIPCION
        defaultValoracionShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllValoracionsByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        // Get all the valoracionList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultValoracionShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the valoracionList where descripcion does not contain UPDATED_DESCRIPCION
        defaultValoracionShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllValoracionsByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        // Get all the valoracionList where fecha equals to DEFAULT_FECHA
        defaultValoracionShouldBeFound("fecha.equals=" + DEFAULT_FECHA);

        // Get all the valoracionList where fecha equals to UPDATED_FECHA
        defaultValoracionShouldNotBeFound("fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllValoracionsByFechaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        // Get all the valoracionList where fecha not equals to DEFAULT_FECHA
        defaultValoracionShouldNotBeFound("fecha.notEquals=" + DEFAULT_FECHA);

        // Get all the valoracionList where fecha not equals to UPDATED_FECHA
        defaultValoracionShouldBeFound("fecha.notEquals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllValoracionsByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        // Get all the valoracionList where fecha in DEFAULT_FECHA or UPDATED_FECHA
        defaultValoracionShouldBeFound("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA);

        // Get all the valoracionList where fecha equals to UPDATED_FECHA
        defaultValoracionShouldNotBeFound("fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllValoracionsByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        // Get all the valoracionList where fecha is not null
        defaultValoracionShouldBeFound("fecha.specified=true");

        // Get all the valoracionList where fecha is null
        defaultValoracionShouldNotBeFound("fecha.specified=false");
    }

    @Test
    @Transactional
    void getAllValoracionsById_servicioIsEqualToSomething() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        // Get all the valoracionList where id_servicio equals to DEFAULT_ID_SERVICIO
        defaultValoracionShouldBeFound("id_servicio.equals=" + DEFAULT_ID_SERVICIO);

        // Get all the valoracionList where id_servicio equals to UPDATED_ID_SERVICIO
        defaultValoracionShouldNotBeFound("id_servicio.equals=" + UPDATED_ID_SERVICIO);
    }

    @Test
    @Transactional
    void getAllValoracionsById_servicioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        // Get all the valoracionList where id_servicio not equals to DEFAULT_ID_SERVICIO
        defaultValoracionShouldNotBeFound("id_servicio.notEquals=" + DEFAULT_ID_SERVICIO);

        // Get all the valoracionList where id_servicio not equals to UPDATED_ID_SERVICIO
        defaultValoracionShouldBeFound("id_servicio.notEquals=" + UPDATED_ID_SERVICIO);
    }

    @Test
    @Transactional
    void getAllValoracionsById_servicioIsInShouldWork() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        // Get all the valoracionList where id_servicio in DEFAULT_ID_SERVICIO or UPDATED_ID_SERVICIO
        defaultValoracionShouldBeFound("id_servicio.in=" + DEFAULT_ID_SERVICIO + "," + UPDATED_ID_SERVICIO);

        // Get all the valoracionList where id_servicio equals to UPDATED_ID_SERVICIO
        defaultValoracionShouldNotBeFound("id_servicio.in=" + UPDATED_ID_SERVICIO);
    }

    @Test
    @Transactional
    void getAllValoracionsById_servicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        // Get all the valoracionList where id_servicio is not null
        defaultValoracionShouldBeFound("id_servicio.specified=true");

        // Get all the valoracionList where id_servicio is null
        defaultValoracionShouldNotBeFound("id_servicio.specified=false");
    }

    @Test
    @Transactional
    void getAllValoracionsById_servicioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        // Get all the valoracionList where id_servicio is greater than or equal to DEFAULT_ID_SERVICIO
        defaultValoracionShouldBeFound("id_servicio.greaterThanOrEqual=" + DEFAULT_ID_SERVICIO);

        // Get all the valoracionList where id_servicio is greater than or equal to UPDATED_ID_SERVICIO
        defaultValoracionShouldNotBeFound("id_servicio.greaterThanOrEqual=" + UPDATED_ID_SERVICIO);
    }

    @Test
    @Transactional
    void getAllValoracionsById_servicioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        // Get all the valoracionList where id_servicio is less than or equal to DEFAULT_ID_SERVICIO
        defaultValoracionShouldBeFound("id_servicio.lessThanOrEqual=" + DEFAULT_ID_SERVICIO);

        // Get all the valoracionList where id_servicio is less than or equal to SMALLER_ID_SERVICIO
        defaultValoracionShouldNotBeFound("id_servicio.lessThanOrEqual=" + SMALLER_ID_SERVICIO);
    }

    @Test
    @Transactional
    void getAllValoracionsById_servicioIsLessThanSomething() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        // Get all the valoracionList where id_servicio is less than DEFAULT_ID_SERVICIO
        defaultValoracionShouldNotBeFound("id_servicio.lessThan=" + DEFAULT_ID_SERVICIO);

        // Get all the valoracionList where id_servicio is less than UPDATED_ID_SERVICIO
        defaultValoracionShouldBeFound("id_servicio.lessThan=" + UPDATED_ID_SERVICIO);
    }

    @Test
    @Transactional
    void getAllValoracionsById_servicioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        // Get all the valoracionList where id_servicio is greater than DEFAULT_ID_SERVICIO
        defaultValoracionShouldNotBeFound("id_servicio.greaterThan=" + DEFAULT_ID_SERVICIO);

        // Get all the valoracionList where id_servicio is greater than SMALLER_ID_SERVICIO
        defaultValoracionShouldBeFound("id_servicio.greaterThan=" + SMALLER_ID_SERVICIO);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultValoracionShouldBeFound(String filter) throws Exception {
        restValoracionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(valoracion.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].id_servicio").value(hasItem(DEFAULT_ID_SERVICIO)));

        // Check, that the count call also returns 1
        restValoracionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultValoracionShouldNotBeFound(String filter) throws Exception {
        restValoracionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restValoracionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingValoracion() throws Exception {
        // Get the valoracion
        restValoracionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewValoracion() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        int databaseSizeBeforeUpdate = valoracionRepository.findAll().size();

        // Update the valoracion
        Valoracion updatedValoracion = valoracionRepository.findById(valoracion.getId()).get();
        // Disconnect from session so that the updates on updatedValoracion are not directly saved in db
        em.detach(updatedValoracion);
        updatedValoracion.descripcion(UPDATED_DESCRIPCION).fecha(UPDATED_FECHA).id_servicio(UPDATED_ID_SERVICIO);
        ValoracionDTO valoracionDTO = valoracionMapper.toDto(updatedValoracion);

        restValoracionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, valoracionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(valoracionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Valoracion in the database
        List<Valoracion> valoracionList = valoracionRepository.findAll();
        assertThat(valoracionList).hasSize(databaseSizeBeforeUpdate);
        Valoracion testValoracion = valoracionList.get(valoracionList.size() - 1);
        assertThat(testValoracion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testValoracion.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testValoracion.getId_servicio()).isEqualTo(UPDATED_ID_SERVICIO);
    }

    @Test
    @Transactional
    void putNonExistingValoracion() throws Exception {
        int databaseSizeBeforeUpdate = valoracionRepository.findAll().size();
        valoracion.setId(count.incrementAndGet());

        // Create the Valoracion
        ValoracionDTO valoracionDTO = valoracionMapper.toDto(valoracion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValoracionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, valoracionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(valoracionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Valoracion in the database
        List<Valoracion> valoracionList = valoracionRepository.findAll();
        assertThat(valoracionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchValoracion() throws Exception {
        int databaseSizeBeforeUpdate = valoracionRepository.findAll().size();
        valoracion.setId(count.incrementAndGet());

        // Create the Valoracion
        ValoracionDTO valoracionDTO = valoracionMapper.toDto(valoracion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValoracionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(valoracionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Valoracion in the database
        List<Valoracion> valoracionList = valoracionRepository.findAll();
        assertThat(valoracionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamValoracion() throws Exception {
        int databaseSizeBeforeUpdate = valoracionRepository.findAll().size();
        valoracion.setId(count.incrementAndGet());

        // Create the Valoracion
        ValoracionDTO valoracionDTO = valoracionMapper.toDto(valoracion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValoracionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(valoracionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Valoracion in the database
        List<Valoracion> valoracionList = valoracionRepository.findAll();
        assertThat(valoracionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateValoracionWithPatch() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        int databaseSizeBeforeUpdate = valoracionRepository.findAll().size();

        // Update the valoracion using partial update
        Valoracion partialUpdatedValoracion = new Valoracion();
        partialUpdatedValoracion.setId(valoracion.getId());

        restValoracionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedValoracion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedValoracion))
            )
            .andExpect(status().isOk());

        // Validate the Valoracion in the database
        List<Valoracion> valoracionList = valoracionRepository.findAll();
        assertThat(valoracionList).hasSize(databaseSizeBeforeUpdate);
        Valoracion testValoracion = valoracionList.get(valoracionList.size() - 1);
        assertThat(testValoracion.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testValoracion.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testValoracion.getId_servicio()).isEqualTo(DEFAULT_ID_SERVICIO);
    }

    @Test
    @Transactional
    void fullUpdateValoracionWithPatch() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        int databaseSizeBeforeUpdate = valoracionRepository.findAll().size();

        // Update the valoracion using partial update
        Valoracion partialUpdatedValoracion = new Valoracion();
        partialUpdatedValoracion.setId(valoracion.getId());

        partialUpdatedValoracion.descripcion(UPDATED_DESCRIPCION).fecha(UPDATED_FECHA).id_servicio(UPDATED_ID_SERVICIO);

        restValoracionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedValoracion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedValoracion))
            )
            .andExpect(status().isOk());

        // Validate the Valoracion in the database
        List<Valoracion> valoracionList = valoracionRepository.findAll();
        assertThat(valoracionList).hasSize(databaseSizeBeforeUpdate);
        Valoracion testValoracion = valoracionList.get(valoracionList.size() - 1);
        assertThat(testValoracion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testValoracion.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testValoracion.getId_servicio()).isEqualTo(UPDATED_ID_SERVICIO);
    }

    @Test
    @Transactional
    void patchNonExistingValoracion() throws Exception {
        int databaseSizeBeforeUpdate = valoracionRepository.findAll().size();
        valoracion.setId(count.incrementAndGet());

        // Create the Valoracion
        ValoracionDTO valoracionDTO = valoracionMapper.toDto(valoracion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValoracionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, valoracionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(valoracionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Valoracion in the database
        List<Valoracion> valoracionList = valoracionRepository.findAll();
        assertThat(valoracionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchValoracion() throws Exception {
        int databaseSizeBeforeUpdate = valoracionRepository.findAll().size();
        valoracion.setId(count.incrementAndGet());

        // Create the Valoracion
        ValoracionDTO valoracionDTO = valoracionMapper.toDto(valoracion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValoracionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(valoracionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Valoracion in the database
        List<Valoracion> valoracionList = valoracionRepository.findAll();
        assertThat(valoracionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamValoracion() throws Exception {
        int databaseSizeBeforeUpdate = valoracionRepository.findAll().size();
        valoracion.setId(count.incrementAndGet());

        // Create the Valoracion
        ValoracionDTO valoracionDTO = valoracionMapper.toDto(valoracion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValoracionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(valoracionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Valoracion in the database
        List<Valoracion> valoracionList = valoracionRepository.findAll();
        assertThat(valoracionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteValoracion() throws Exception {
        // Initialize the database
        valoracionRepository.saveAndFlush(valoracion);

        int databaseSizeBeforeDelete = valoracionRepository.findAll().size();

        // Delete the valoracion
        restValoracionMockMvc
            .perform(delete(ENTITY_API_URL_ID, valoracion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Valoracion> valoracionList = valoracionRepository.findAll();
        assertThat(valoracionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
