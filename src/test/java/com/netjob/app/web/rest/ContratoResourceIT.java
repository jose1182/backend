package com.netjob.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.netjob.app.IntegrationTest;
import com.netjob.app.domain.Contrato;
import com.netjob.app.domain.Servicio;
import com.netjob.app.domain.Usuario;
import com.netjob.app.repository.ContratoRepository;
import com.netjob.app.service.criteria.ContratoCriteria;
import com.netjob.app.service.dto.ContratoDTO;
import com.netjob.app.service.mapper.ContratoMapper;
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
 * Integration tests for the {@link ContratoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContratoResourceIT {

    private static final Float DEFAULT_PRECIOFINAL = 0F;
    private static final Float UPDATED_PRECIOFINAL = 1F;
    private static final Float SMALLER_PRECIOFINAL = 0F - 1F;

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/contratoes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private ContratoMapper contratoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContratoMockMvc;

    private Contrato contrato;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contrato createEntity(EntityManager em) {
        Contrato contrato = new Contrato().preciofinal(DEFAULT_PRECIOFINAL).fecha(DEFAULT_FECHA);
        return contrato;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contrato createUpdatedEntity(EntityManager em) {
        Contrato contrato = new Contrato().preciofinal(UPDATED_PRECIOFINAL).fecha(UPDATED_FECHA);
        return contrato;
    }

    @BeforeEach
    public void initTest() {
        contrato = createEntity(em);
    }

    @Test
    @Transactional
    void createContrato() throws Exception {
        int databaseSizeBeforeCreate = contratoRepository.findAll().size();
        // Create the Contrato
        ContratoDTO contratoDTO = contratoMapper.toDto(contrato);
        restContratoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contratoDTO)))
            .andExpect(status().isCreated());

        // Validate the Contrato in the database
        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeCreate + 1);
        Contrato testContrato = contratoList.get(contratoList.size() - 1);
        assertThat(testContrato.getPreciofinal()).isEqualTo(DEFAULT_PRECIOFINAL);
        assertThat(testContrato.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    void createContratoWithExistingId() throws Exception {
        // Create the Contrato with an existing ID
        contrato.setId(1L);
        ContratoDTO contratoDTO = contratoMapper.toDto(contrato);

        int databaseSizeBeforeCreate = contratoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContratoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contratoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contrato in the database
        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPreciofinalIsRequired() throws Exception {
        int databaseSizeBeforeTest = contratoRepository.findAll().size();
        // set the field null
        contrato.setPreciofinal(null);

        // Create the Contrato, which fails.
        ContratoDTO contratoDTO = contratoMapper.toDto(contrato);

        restContratoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contratoDTO)))
            .andExpect(status().isBadRequest());

        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = contratoRepository.findAll().size();
        // set the field null
        contrato.setFecha(null);

        // Create the Contrato, which fails.
        ContratoDTO contratoDTO = contratoMapper.toDto(contrato);

        restContratoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contratoDTO)))
            .andExpect(status().isBadRequest());

        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContratoes() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        // Get all the contratoList
        restContratoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contrato.getId().intValue())))
            .andExpect(jsonPath("$.[*].preciofinal").value(hasItem(DEFAULT_PRECIOFINAL.doubleValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())));
    }

    @Test
    @Transactional
    void getContrato() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        // Get the contrato
        restContratoMockMvc
            .perform(get(ENTITY_API_URL_ID, contrato.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contrato.getId().intValue()))
            .andExpect(jsonPath("$.preciofinal").value(DEFAULT_PRECIOFINAL.doubleValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()));
    }

    @Test
    @Transactional
    void getContratoesByIdFiltering() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        Long id = contrato.getId();

        defaultContratoShouldBeFound("id.equals=" + id);
        defaultContratoShouldNotBeFound("id.notEquals=" + id);

        defaultContratoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContratoShouldNotBeFound("id.greaterThan=" + id);

        defaultContratoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContratoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContratoesByPreciofinalIsEqualToSomething() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        // Get all the contratoList where preciofinal equals to DEFAULT_PRECIOFINAL
        defaultContratoShouldBeFound("preciofinal.equals=" + DEFAULT_PRECIOFINAL);

        // Get all the contratoList where preciofinal equals to UPDATED_PRECIOFINAL
        defaultContratoShouldNotBeFound("preciofinal.equals=" + UPDATED_PRECIOFINAL);
    }

    @Test
    @Transactional
    void getAllContratoesByPreciofinalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        // Get all the contratoList where preciofinal not equals to DEFAULT_PRECIOFINAL
        defaultContratoShouldNotBeFound("preciofinal.notEquals=" + DEFAULT_PRECIOFINAL);

        // Get all the contratoList where preciofinal not equals to UPDATED_PRECIOFINAL
        defaultContratoShouldBeFound("preciofinal.notEquals=" + UPDATED_PRECIOFINAL);
    }

    @Test
    @Transactional
    void getAllContratoesByPreciofinalIsInShouldWork() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        // Get all the contratoList where preciofinal in DEFAULT_PRECIOFINAL or UPDATED_PRECIOFINAL
        defaultContratoShouldBeFound("preciofinal.in=" + DEFAULT_PRECIOFINAL + "," + UPDATED_PRECIOFINAL);

        // Get all the contratoList where preciofinal equals to UPDATED_PRECIOFINAL
        defaultContratoShouldNotBeFound("preciofinal.in=" + UPDATED_PRECIOFINAL);
    }

    @Test
    @Transactional
    void getAllContratoesByPreciofinalIsNullOrNotNull() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        // Get all the contratoList where preciofinal is not null
        defaultContratoShouldBeFound("preciofinal.specified=true");

        // Get all the contratoList where preciofinal is null
        defaultContratoShouldNotBeFound("preciofinal.specified=false");
    }

    @Test
    @Transactional
    void getAllContratoesByPreciofinalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        // Get all the contratoList where preciofinal is greater than or equal to DEFAULT_PRECIOFINAL
        defaultContratoShouldBeFound("preciofinal.greaterThanOrEqual=" + DEFAULT_PRECIOFINAL);

        // Get all the contratoList where preciofinal is greater than or equal to UPDATED_PRECIOFINAL
        defaultContratoShouldNotBeFound("preciofinal.greaterThanOrEqual=" + UPDATED_PRECIOFINAL);
    }

    @Test
    @Transactional
    void getAllContratoesByPreciofinalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        // Get all the contratoList where preciofinal is less than or equal to DEFAULT_PRECIOFINAL
        defaultContratoShouldBeFound("preciofinal.lessThanOrEqual=" + DEFAULT_PRECIOFINAL);

        // Get all the contratoList where preciofinal is less than or equal to SMALLER_PRECIOFINAL
        defaultContratoShouldNotBeFound("preciofinal.lessThanOrEqual=" + SMALLER_PRECIOFINAL);
    }

    @Test
    @Transactional
    void getAllContratoesByPreciofinalIsLessThanSomething() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        // Get all the contratoList where preciofinal is less than DEFAULT_PRECIOFINAL
        defaultContratoShouldNotBeFound("preciofinal.lessThan=" + DEFAULT_PRECIOFINAL);

        // Get all the contratoList where preciofinal is less than UPDATED_PRECIOFINAL
        defaultContratoShouldBeFound("preciofinal.lessThan=" + UPDATED_PRECIOFINAL);
    }

    @Test
    @Transactional
    void getAllContratoesByPreciofinalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        // Get all the contratoList where preciofinal is greater than DEFAULT_PRECIOFINAL
        defaultContratoShouldNotBeFound("preciofinal.greaterThan=" + DEFAULT_PRECIOFINAL);

        // Get all the contratoList where preciofinal is greater than SMALLER_PRECIOFINAL
        defaultContratoShouldBeFound("preciofinal.greaterThan=" + SMALLER_PRECIOFINAL);
    }

    @Test
    @Transactional
    void getAllContratoesByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        // Get all the contratoList where fecha equals to DEFAULT_FECHA
        defaultContratoShouldBeFound("fecha.equals=" + DEFAULT_FECHA);

        // Get all the contratoList where fecha equals to UPDATED_FECHA
        defaultContratoShouldNotBeFound("fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllContratoesByFechaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        // Get all the contratoList where fecha not equals to DEFAULT_FECHA
        defaultContratoShouldNotBeFound("fecha.notEquals=" + DEFAULT_FECHA);

        // Get all the contratoList where fecha not equals to UPDATED_FECHA
        defaultContratoShouldBeFound("fecha.notEquals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllContratoesByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        // Get all the contratoList where fecha in DEFAULT_FECHA or UPDATED_FECHA
        defaultContratoShouldBeFound("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA);

        // Get all the contratoList where fecha equals to UPDATED_FECHA
        defaultContratoShouldNotBeFound("fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllContratoesByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        // Get all the contratoList where fecha is not null
        defaultContratoShouldBeFound("fecha.specified=true");

        // Get all the contratoList where fecha is null
        defaultContratoShouldNotBeFound("fecha.specified=false");
    }

    @Test
    @Transactional
    void getAllContratoesByUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);
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
        contrato.setUsuario(usuario);
        contratoRepository.saveAndFlush(contrato);
        Long usuarioId = usuario.getId();

        // Get all the contratoList where usuario equals to usuarioId
        defaultContratoShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the contratoList where usuario equals to (usuarioId + 1)
        defaultContratoShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    @Test
    @Transactional
    void getAllContratoesByServicioIsEqualToSomething() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);
        Servicio servicio;
        if (TestUtil.findAll(em, Servicio.class).isEmpty()) {
            servicio = ServicioResourceIT.createEntity(em);
            em.persist(servicio);
            em.flush();
        } else {
            servicio = TestUtil.findAll(em, Servicio.class).get(0);
        }
        em.persist(servicio);
        em.flush();
        contrato.setServicio(servicio);
        contratoRepository.saveAndFlush(contrato);
        Long servicioId = servicio.getId();

        // Get all the contratoList where servicio equals to servicioId
        defaultContratoShouldBeFound("servicioId.equals=" + servicioId);

        // Get all the contratoList where servicio equals to (servicioId + 1)
        defaultContratoShouldNotBeFound("servicioId.equals=" + (servicioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContratoShouldBeFound(String filter) throws Exception {
        restContratoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contrato.getId().intValue())))
            .andExpect(jsonPath("$.[*].preciofinal").value(hasItem(DEFAULT_PRECIOFINAL.doubleValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())));

        // Check, that the count call also returns 1
        restContratoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContratoShouldNotBeFound(String filter) throws Exception {
        restContratoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContratoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContrato() throws Exception {
        // Get the contrato
        restContratoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContrato() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        int databaseSizeBeforeUpdate = contratoRepository.findAll().size();

        // Update the contrato
        Contrato updatedContrato = contratoRepository.findById(contrato.getId()).get();
        // Disconnect from session so that the updates on updatedContrato are not directly saved in db
        em.detach(updatedContrato);
        updatedContrato.preciofinal(UPDATED_PRECIOFINAL).fecha(UPDATED_FECHA);
        ContratoDTO contratoDTO = contratoMapper.toDto(updatedContrato);

        restContratoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contratoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contratoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Contrato in the database
        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeUpdate);
        Contrato testContrato = contratoList.get(contratoList.size() - 1);
        assertThat(testContrato.getPreciofinal()).isEqualTo(UPDATED_PRECIOFINAL);
        assertThat(testContrato.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void putNonExistingContrato() throws Exception {
        int databaseSizeBeforeUpdate = contratoRepository.findAll().size();
        contrato.setId(count.incrementAndGet());

        // Create the Contrato
        ContratoDTO contratoDTO = contratoMapper.toDto(contrato);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContratoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contratoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contratoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contrato in the database
        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContrato() throws Exception {
        int databaseSizeBeforeUpdate = contratoRepository.findAll().size();
        contrato.setId(count.incrementAndGet());

        // Create the Contrato
        ContratoDTO contratoDTO = contratoMapper.toDto(contrato);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contratoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contrato in the database
        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContrato() throws Exception {
        int databaseSizeBeforeUpdate = contratoRepository.findAll().size();
        contrato.setId(count.incrementAndGet());

        // Create the Contrato
        ContratoDTO contratoDTO = contratoMapper.toDto(contrato);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contratoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contrato in the database
        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContratoWithPatch() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        int databaseSizeBeforeUpdate = contratoRepository.findAll().size();

        // Update the contrato using partial update
        Contrato partialUpdatedContrato = new Contrato();
        partialUpdatedContrato.setId(contrato.getId());

        restContratoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContrato.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContrato))
            )
            .andExpect(status().isOk());

        // Validate the Contrato in the database
        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeUpdate);
        Contrato testContrato = contratoList.get(contratoList.size() - 1);
        assertThat(testContrato.getPreciofinal()).isEqualTo(DEFAULT_PRECIOFINAL);
        assertThat(testContrato.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    void fullUpdateContratoWithPatch() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        int databaseSizeBeforeUpdate = contratoRepository.findAll().size();

        // Update the contrato using partial update
        Contrato partialUpdatedContrato = new Contrato();
        partialUpdatedContrato.setId(contrato.getId());

        partialUpdatedContrato.preciofinal(UPDATED_PRECIOFINAL).fecha(UPDATED_FECHA);

        restContratoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContrato.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContrato))
            )
            .andExpect(status().isOk());

        // Validate the Contrato in the database
        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeUpdate);
        Contrato testContrato = contratoList.get(contratoList.size() - 1);
        assertThat(testContrato.getPreciofinal()).isEqualTo(UPDATED_PRECIOFINAL);
        assertThat(testContrato.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void patchNonExistingContrato() throws Exception {
        int databaseSizeBeforeUpdate = contratoRepository.findAll().size();
        contrato.setId(count.incrementAndGet());

        // Create the Contrato
        ContratoDTO contratoDTO = contratoMapper.toDto(contrato);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContratoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contratoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contratoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contrato in the database
        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContrato() throws Exception {
        int databaseSizeBeforeUpdate = contratoRepository.findAll().size();
        contrato.setId(count.incrementAndGet());

        // Create the Contrato
        ContratoDTO contratoDTO = contratoMapper.toDto(contrato);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contratoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contrato in the database
        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContrato() throws Exception {
        int databaseSizeBeforeUpdate = contratoRepository.findAll().size();
        contrato.setId(count.incrementAndGet());

        // Create the Contrato
        ContratoDTO contratoDTO = contratoMapper.toDto(contrato);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContratoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contratoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contrato in the database
        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContrato() throws Exception {
        // Initialize the database
        contratoRepository.saveAndFlush(contrato);

        int databaseSizeBeforeDelete = contratoRepository.findAll().size();

        // Delete the contrato
        restContratoMockMvc
            .perform(delete(ENTITY_API_URL_ID, contrato.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contrato> contratoList = contratoRepository.findAll();
        assertThat(contratoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
