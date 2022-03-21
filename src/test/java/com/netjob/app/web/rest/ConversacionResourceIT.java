package com.netjob.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.netjob.app.IntegrationTest;
import com.netjob.app.domain.Conversacion;
import com.netjob.app.domain.Usuario;
import com.netjob.app.repository.ConversacionRepository;
import com.netjob.app.service.criteria.ConversacionCriteria;
import com.netjob.app.service.dto.ConversacionDTO;
import com.netjob.app.service.mapper.ConversacionMapper;
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
 * Integration tests for the {@link ConversacionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConversacionResourceIT {

    private static final String ENTITY_API_URL = "/api/conversacions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConversacionRepository conversacionRepository;

    @Autowired
    private ConversacionMapper conversacionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConversacionMockMvc;

    private Conversacion conversacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conversacion createEntity(EntityManager em) {
        Conversacion conversacion = new Conversacion();
        return conversacion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conversacion createUpdatedEntity(EntityManager em) {
        Conversacion conversacion = new Conversacion();
        return conversacion;
    }

    @BeforeEach
    public void initTest() {
        conversacion = createEntity(em);
    }

    @Test
    @Transactional
    void createConversacion() throws Exception {
        int databaseSizeBeforeCreate = conversacionRepository.findAll().size();
        // Create the Conversacion
        ConversacionDTO conversacionDTO = conversacionMapper.toDto(conversacion);
        restConversacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conversacionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Conversacion in the database
        List<Conversacion> conversacionList = conversacionRepository.findAll();
        assertThat(conversacionList).hasSize(databaseSizeBeforeCreate + 1);
        Conversacion testConversacion = conversacionList.get(conversacionList.size() - 1);
    }

    @Test
    @Transactional
    void createConversacionWithExistingId() throws Exception {
        // Create the Conversacion with an existing ID
        conversacion.setId(1L);
        ConversacionDTO conversacionDTO = conversacionMapper.toDto(conversacion);

        int databaseSizeBeforeCreate = conversacionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConversacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conversacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conversacion in the database
        List<Conversacion> conversacionList = conversacionRepository.findAll();
        assertThat(conversacionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConversacions() throws Exception {
        // Initialize the database
        conversacionRepository.saveAndFlush(conversacion);

        // Get all the conversacionList
        restConversacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conversacion.getId().intValue())));
    }

    @Test
    @Transactional
    void getConversacion() throws Exception {
        // Initialize the database
        conversacionRepository.saveAndFlush(conversacion);

        // Get the conversacion
        restConversacionMockMvc
            .perform(get(ENTITY_API_URL_ID, conversacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(conversacion.getId().intValue()));
    }

    @Test
    @Transactional
    void getConversacionsByIdFiltering() throws Exception {
        // Initialize the database
        conversacionRepository.saveAndFlush(conversacion);

        Long id = conversacion.getId();

        defaultConversacionShouldBeFound("id.equals=" + id);
        defaultConversacionShouldNotBeFound("id.notEquals=" + id);

        defaultConversacionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultConversacionShouldNotBeFound("id.greaterThan=" + id);

        defaultConversacionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultConversacionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllConversacionsByUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        conversacionRepository.saveAndFlush(conversacion);
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
        conversacion.addUsuario(usuario);
        conversacionRepository.saveAndFlush(conversacion);
        Long usuarioId = usuario.getId();

        // Get all the conversacionList where usuario equals to usuarioId
        defaultConversacionShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the conversacionList where usuario equals to (usuarioId + 1)
        defaultConversacionShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConversacionShouldBeFound(String filter) throws Exception {
        restConversacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conversacion.getId().intValue())));

        // Check, that the count call also returns 1
        restConversacionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConversacionShouldNotBeFound(String filter) throws Exception {
        restConversacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConversacionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingConversacion() throws Exception {
        // Get the conversacion
        restConversacionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConversacion() throws Exception {
        // Initialize the database
        conversacionRepository.saveAndFlush(conversacion);

        int databaseSizeBeforeUpdate = conversacionRepository.findAll().size();

        // Update the conversacion
        Conversacion updatedConversacion = conversacionRepository.findById(conversacion.getId()).get();
        // Disconnect from session so that the updates on updatedConversacion are not directly saved in db
        em.detach(updatedConversacion);
        ConversacionDTO conversacionDTO = conversacionMapper.toDto(updatedConversacion);

        restConversacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, conversacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conversacionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Conversacion in the database
        List<Conversacion> conversacionList = conversacionRepository.findAll();
        assertThat(conversacionList).hasSize(databaseSizeBeforeUpdate);
        Conversacion testConversacion = conversacionList.get(conversacionList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingConversacion() throws Exception {
        int databaseSizeBeforeUpdate = conversacionRepository.findAll().size();
        conversacion.setId(count.incrementAndGet());

        // Create the Conversacion
        ConversacionDTO conversacionDTO = conversacionMapper.toDto(conversacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConversacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, conversacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conversacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conversacion in the database
        List<Conversacion> conversacionList = conversacionRepository.findAll();
        assertThat(conversacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConversacion() throws Exception {
        int databaseSizeBeforeUpdate = conversacionRepository.findAll().size();
        conversacion.setId(count.incrementAndGet());

        // Create the Conversacion
        ConversacionDTO conversacionDTO = conversacionMapper.toDto(conversacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConversacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conversacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conversacion in the database
        List<Conversacion> conversacionList = conversacionRepository.findAll();
        assertThat(conversacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConversacion() throws Exception {
        int databaseSizeBeforeUpdate = conversacionRepository.findAll().size();
        conversacion.setId(count.incrementAndGet());

        // Create the Conversacion
        ConversacionDTO conversacionDTO = conversacionMapper.toDto(conversacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConversacionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conversacionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Conversacion in the database
        List<Conversacion> conversacionList = conversacionRepository.findAll();
        assertThat(conversacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConversacionWithPatch() throws Exception {
        // Initialize the database
        conversacionRepository.saveAndFlush(conversacion);

        int databaseSizeBeforeUpdate = conversacionRepository.findAll().size();

        // Update the conversacion using partial update
        Conversacion partialUpdatedConversacion = new Conversacion();
        partialUpdatedConversacion.setId(conversacion.getId());

        restConversacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConversacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConversacion))
            )
            .andExpect(status().isOk());

        // Validate the Conversacion in the database
        List<Conversacion> conversacionList = conversacionRepository.findAll();
        assertThat(conversacionList).hasSize(databaseSizeBeforeUpdate);
        Conversacion testConversacion = conversacionList.get(conversacionList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateConversacionWithPatch() throws Exception {
        // Initialize the database
        conversacionRepository.saveAndFlush(conversacion);

        int databaseSizeBeforeUpdate = conversacionRepository.findAll().size();

        // Update the conversacion using partial update
        Conversacion partialUpdatedConversacion = new Conversacion();
        partialUpdatedConversacion.setId(conversacion.getId());

        restConversacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConversacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConversacion))
            )
            .andExpect(status().isOk());

        // Validate the Conversacion in the database
        List<Conversacion> conversacionList = conversacionRepository.findAll();
        assertThat(conversacionList).hasSize(databaseSizeBeforeUpdate);
        Conversacion testConversacion = conversacionList.get(conversacionList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingConversacion() throws Exception {
        int databaseSizeBeforeUpdate = conversacionRepository.findAll().size();
        conversacion.setId(count.incrementAndGet());

        // Create the Conversacion
        ConversacionDTO conversacionDTO = conversacionMapper.toDto(conversacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConversacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, conversacionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conversacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conversacion in the database
        List<Conversacion> conversacionList = conversacionRepository.findAll();
        assertThat(conversacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConversacion() throws Exception {
        int databaseSizeBeforeUpdate = conversacionRepository.findAll().size();
        conversacion.setId(count.incrementAndGet());

        // Create the Conversacion
        ConversacionDTO conversacionDTO = conversacionMapper.toDto(conversacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConversacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conversacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conversacion in the database
        List<Conversacion> conversacionList = conversacionRepository.findAll();
        assertThat(conversacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConversacion() throws Exception {
        int databaseSizeBeforeUpdate = conversacionRepository.findAll().size();
        conversacion.setId(count.incrementAndGet());

        // Create the Conversacion
        ConversacionDTO conversacionDTO = conversacionMapper.toDto(conversacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConversacionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conversacionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Conversacion in the database
        List<Conversacion> conversacionList = conversacionRepository.findAll();
        assertThat(conversacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConversacion() throws Exception {
        // Initialize the database
        conversacionRepository.saveAndFlush(conversacion);

        int databaseSizeBeforeDelete = conversacionRepository.findAll().size();

        // Delete the conversacion
        restConversacionMockMvc
            .perform(delete(ENTITY_API_URL_ID, conversacion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Conversacion> conversacionList = conversacionRepository.findAll();
        assertThat(conversacionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
