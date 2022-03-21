package com.netjob.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.netjob.app.IntegrationTest;
import com.netjob.app.domain.Conversacion;
import com.netjob.app.domain.Mensaje;
import com.netjob.app.domain.Usuario;
import com.netjob.app.repository.MensajeRepository;
import com.netjob.app.service.criteria.MensajeCriteria;
import com.netjob.app.service.dto.MensajeDTO;
import com.netjob.app.service.mapper.MensajeMapper;
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
 * Integration tests for the {@link MensajeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MensajeResourceIT {

    private static final String DEFAULT_TEXTO = "AAAAAAAAAA";
    private static final String UPDATED_TEXTO = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/mensajes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MensajeRepository mensajeRepository;

    @Autowired
    private MensajeMapper mensajeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMensajeMockMvc;

    private Mensaje mensaje;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mensaje createEntity(EntityManager em) {
        Mensaje mensaje = new Mensaje().texto(DEFAULT_TEXTO).fecha(DEFAULT_FECHA);
        return mensaje;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mensaje createUpdatedEntity(EntityManager em) {
        Mensaje mensaje = new Mensaje().texto(UPDATED_TEXTO).fecha(UPDATED_FECHA);
        return mensaje;
    }

    @BeforeEach
    public void initTest() {
        mensaje = createEntity(em);
    }

    @Test
    @Transactional
    void createMensaje() throws Exception {
        int databaseSizeBeforeCreate = mensajeRepository.findAll().size();
        // Create the Mensaje
        MensajeDTO mensajeDTO = mensajeMapper.toDto(mensaje);
        restMensajeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mensajeDTO)))
            .andExpect(status().isCreated());

        // Validate the Mensaje in the database
        List<Mensaje> mensajeList = mensajeRepository.findAll();
        assertThat(mensajeList).hasSize(databaseSizeBeforeCreate + 1);
        Mensaje testMensaje = mensajeList.get(mensajeList.size() - 1);
        assertThat(testMensaje.getTexto()).isEqualTo(DEFAULT_TEXTO);
        assertThat(testMensaje.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    void createMensajeWithExistingId() throws Exception {
        // Create the Mensaje with an existing ID
        mensaje.setId(1L);
        MensajeDTO mensajeDTO = mensajeMapper.toDto(mensaje);

        int databaseSizeBeforeCreate = mensajeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMensajeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mensajeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Mensaje in the database
        List<Mensaje> mensajeList = mensajeRepository.findAll();
        assertThat(mensajeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTextoIsRequired() throws Exception {
        int databaseSizeBeforeTest = mensajeRepository.findAll().size();
        // set the field null
        mensaje.setTexto(null);

        // Create the Mensaje, which fails.
        MensajeDTO mensajeDTO = mensajeMapper.toDto(mensaje);

        restMensajeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mensajeDTO)))
            .andExpect(status().isBadRequest());

        List<Mensaje> mensajeList = mensajeRepository.findAll();
        assertThat(mensajeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = mensajeRepository.findAll().size();
        // set the field null
        mensaje.setFecha(null);

        // Create the Mensaje, which fails.
        MensajeDTO mensajeDTO = mensajeMapper.toDto(mensaje);

        restMensajeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mensajeDTO)))
            .andExpect(status().isBadRequest());

        List<Mensaje> mensajeList = mensajeRepository.findAll();
        assertThat(mensajeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMensajes() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

        // Get all the mensajeList
        restMensajeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mensaje.getId().intValue())))
            .andExpect(jsonPath("$.[*].texto").value(hasItem(DEFAULT_TEXTO)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())));
    }

    @Test
    @Transactional
    void getMensaje() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

        // Get the mensaje
        restMensajeMockMvc
            .perform(get(ENTITY_API_URL_ID, mensaje.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mensaje.getId().intValue()))
            .andExpect(jsonPath("$.texto").value(DEFAULT_TEXTO))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()));
    }

    @Test
    @Transactional
    void getMensajesByIdFiltering() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

        Long id = mensaje.getId();

        defaultMensajeShouldBeFound("id.equals=" + id);
        defaultMensajeShouldNotBeFound("id.notEquals=" + id);

        defaultMensajeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMensajeShouldNotBeFound("id.greaterThan=" + id);

        defaultMensajeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMensajeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMensajesByTextoIsEqualToSomething() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

        // Get all the mensajeList where texto equals to DEFAULT_TEXTO
        defaultMensajeShouldBeFound("texto.equals=" + DEFAULT_TEXTO);

        // Get all the mensajeList where texto equals to UPDATED_TEXTO
        defaultMensajeShouldNotBeFound("texto.equals=" + UPDATED_TEXTO);
    }

    @Test
    @Transactional
    void getAllMensajesByTextoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

        // Get all the mensajeList where texto not equals to DEFAULT_TEXTO
        defaultMensajeShouldNotBeFound("texto.notEquals=" + DEFAULT_TEXTO);

        // Get all the mensajeList where texto not equals to UPDATED_TEXTO
        defaultMensajeShouldBeFound("texto.notEquals=" + UPDATED_TEXTO);
    }

    @Test
    @Transactional
    void getAllMensajesByTextoIsInShouldWork() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

        // Get all the mensajeList where texto in DEFAULT_TEXTO or UPDATED_TEXTO
        defaultMensajeShouldBeFound("texto.in=" + DEFAULT_TEXTO + "," + UPDATED_TEXTO);

        // Get all the mensajeList where texto equals to UPDATED_TEXTO
        defaultMensajeShouldNotBeFound("texto.in=" + UPDATED_TEXTO);
    }

    @Test
    @Transactional
    void getAllMensajesByTextoIsNullOrNotNull() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

        // Get all the mensajeList where texto is not null
        defaultMensajeShouldBeFound("texto.specified=true");

        // Get all the mensajeList where texto is null
        defaultMensajeShouldNotBeFound("texto.specified=false");
    }

    @Test
    @Transactional
    void getAllMensajesByTextoContainsSomething() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

        // Get all the mensajeList where texto contains DEFAULT_TEXTO
        defaultMensajeShouldBeFound("texto.contains=" + DEFAULT_TEXTO);

        // Get all the mensajeList where texto contains UPDATED_TEXTO
        defaultMensajeShouldNotBeFound("texto.contains=" + UPDATED_TEXTO);
    }

    @Test
    @Transactional
    void getAllMensajesByTextoNotContainsSomething() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

        // Get all the mensajeList where texto does not contain DEFAULT_TEXTO
        defaultMensajeShouldNotBeFound("texto.doesNotContain=" + DEFAULT_TEXTO);

        // Get all the mensajeList where texto does not contain UPDATED_TEXTO
        defaultMensajeShouldBeFound("texto.doesNotContain=" + UPDATED_TEXTO);
    }

    @Test
    @Transactional
    void getAllMensajesByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

        // Get all the mensajeList where fecha equals to DEFAULT_FECHA
        defaultMensajeShouldBeFound("fecha.equals=" + DEFAULT_FECHA);

        // Get all the mensajeList where fecha equals to UPDATED_FECHA
        defaultMensajeShouldNotBeFound("fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllMensajesByFechaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

        // Get all the mensajeList where fecha not equals to DEFAULT_FECHA
        defaultMensajeShouldNotBeFound("fecha.notEquals=" + DEFAULT_FECHA);

        // Get all the mensajeList where fecha not equals to UPDATED_FECHA
        defaultMensajeShouldBeFound("fecha.notEquals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllMensajesByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

        // Get all the mensajeList where fecha in DEFAULT_FECHA or UPDATED_FECHA
        defaultMensajeShouldBeFound("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA);

        // Get all the mensajeList where fecha equals to UPDATED_FECHA
        defaultMensajeShouldNotBeFound("fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllMensajesByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

        // Get all the mensajeList where fecha is not null
        defaultMensajeShouldBeFound("fecha.specified=true");

        // Get all the mensajeList where fecha is null
        defaultMensajeShouldNotBeFound("fecha.specified=false");
    }

    @Test
    @Transactional
    void getAllMensajesByEmisorIsEqualToSomething() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);
        Usuario emisor;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            emisor = UsuarioResourceIT.createEntity(em);
            em.persist(emisor);
            em.flush();
        } else {
            emisor = TestUtil.findAll(em, Usuario.class).get(0);
        }
        em.persist(emisor);
        em.flush();
        mensaje.setEmisor(emisor);
        mensajeRepository.saveAndFlush(mensaje);
        Long emisorId = emisor.getId();

        // Get all the mensajeList where emisor equals to emisorId
        defaultMensajeShouldBeFound("emisorId.equals=" + emisorId);

        // Get all the mensajeList where emisor equals to (emisorId + 1)
        defaultMensajeShouldNotBeFound("emisorId.equals=" + (emisorId + 1));
    }

    @Test
    @Transactional
    void getAllMensajesByReceptorIsEqualToSomething() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);
        Usuario receptor;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            receptor = UsuarioResourceIT.createEntity(em);
            em.persist(receptor);
            em.flush();
        } else {
            receptor = TestUtil.findAll(em, Usuario.class).get(0);
        }
        em.persist(receptor);
        em.flush();
        mensaje.setReceptor(receptor);
        mensajeRepository.saveAndFlush(mensaje);
        Long receptorId = receptor.getId();

        // Get all the mensajeList where receptor equals to receptorId
        defaultMensajeShouldBeFound("receptorId.equals=" + receptorId);

        // Get all the mensajeList where receptor equals to (receptorId + 1)
        defaultMensajeShouldNotBeFound("receptorId.equals=" + (receptorId + 1));
    }

    @Test
    @Transactional
    void getAllMensajesByConversacionIsEqualToSomething() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);
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
        mensaje.setConversacion(conversacion);
        mensajeRepository.saveAndFlush(mensaje);
        Long conversacionId = conversacion.getId();

        // Get all the mensajeList where conversacion equals to conversacionId
        defaultMensajeShouldBeFound("conversacionId.equals=" + conversacionId);

        // Get all the mensajeList where conversacion equals to (conversacionId + 1)
        defaultMensajeShouldNotBeFound("conversacionId.equals=" + (conversacionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMensajeShouldBeFound(String filter) throws Exception {
        restMensajeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mensaje.getId().intValue())))
            .andExpect(jsonPath("$.[*].texto").value(hasItem(DEFAULT_TEXTO)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())));

        // Check, that the count call also returns 1
        restMensajeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMensajeShouldNotBeFound(String filter) throws Exception {
        restMensajeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMensajeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMensaje() throws Exception {
        // Get the mensaje
        restMensajeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMensaje() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

        int databaseSizeBeforeUpdate = mensajeRepository.findAll().size();

        // Update the mensaje
        Mensaje updatedMensaje = mensajeRepository.findById(mensaje.getId()).get();
        // Disconnect from session so that the updates on updatedMensaje are not directly saved in db
        em.detach(updatedMensaje);
        updatedMensaje.texto(UPDATED_TEXTO).fecha(UPDATED_FECHA);
        MensajeDTO mensajeDTO = mensajeMapper.toDto(updatedMensaje);

        restMensajeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mensajeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mensajeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Mensaje in the database
        List<Mensaje> mensajeList = mensajeRepository.findAll();
        assertThat(mensajeList).hasSize(databaseSizeBeforeUpdate);
        Mensaje testMensaje = mensajeList.get(mensajeList.size() - 1);
        assertThat(testMensaje.getTexto()).isEqualTo(UPDATED_TEXTO);
        assertThat(testMensaje.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void putNonExistingMensaje() throws Exception {
        int databaseSizeBeforeUpdate = mensajeRepository.findAll().size();
        mensaje.setId(count.incrementAndGet());

        // Create the Mensaje
        MensajeDTO mensajeDTO = mensajeMapper.toDto(mensaje);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMensajeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mensajeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mensajeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mensaje in the database
        List<Mensaje> mensajeList = mensajeRepository.findAll();
        assertThat(mensajeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMensaje() throws Exception {
        int databaseSizeBeforeUpdate = mensajeRepository.findAll().size();
        mensaje.setId(count.incrementAndGet());

        // Create the Mensaje
        MensajeDTO mensajeDTO = mensajeMapper.toDto(mensaje);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMensajeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mensajeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mensaje in the database
        List<Mensaje> mensajeList = mensajeRepository.findAll();
        assertThat(mensajeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMensaje() throws Exception {
        int databaseSizeBeforeUpdate = mensajeRepository.findAll().size();
        mensaje.setId(count.incrementAndGet());

        // Create the Mensaje
        MensajeDTO mensajeDTO = mensajeMapper.toDto(mensaje);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMensajeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mensajeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mensaje in the database
        List<Mensaje> mensajeList = mensajeRepository.findAll();
        assertThat(mensajeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMensajeWithPatch() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

        int databaseSizeBeforeUpdate = mensajeRepository.findAll().size();

        // Update the mensaje using partial update
        Mensaje partialUpdatedMensaje = new Mensaje();
        partialUpdatedMensaje.setId(mensaje.getId());

        restMensajeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMensaje.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMensaje))
            )
            .andExpect(status().isOk());

        // Validate the Mensaje in the database
        List<Mensaje> mensajeList = mensajeRepository.findAll();
        assertThat(mensajeList).hasSize(databaseSizeBeforeUpdate);
        Mensaje testMensaje = mensajeList.get(mensajeList.size() - 1);
        assertThat(testMensaje.getTexto()).isEqualTo(DEFAULT_TEXTO);
        assertThat(testMensaje.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    void fullUpdateMensajeWithPatch() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

        int databaseSizeBeforeUpdate = mensajeRepository.findAll().size();

        // Update the mensaje using partial update
        Mensaje partialUpdatedMensaje = new Mensaje();
        partialUpdatedMensaje.setId(mensaje.getId());

        partialUpdatedMensaje.texto(UPDATED_TEXTO).fecha(UPDATED_FECHA);

        restMensajeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMensaje.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMensaje))
            )
            .andExpect(status().isOk());

        // Validate the Mensaje in the database
        List<Mensaje> mensajeList = mensajeRepository.findAll();
        assertThat(mensajeList).hasSize(databaseSizeBeforeUpdate);
        Mensaje testMensaje = mensajeList.get(mensajeList.size() - 1);
        assertThat(testMensaje.getTexto()).isEqualTo(UPDATED_TEXTO);
        assertThat(testMensaje.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void patchNonExistingMensaje() throws Exception {
        int databaseSizeBeforeUpdate = mensajeRepository.findAll().size();
        mensaje.setId(count.incrementAndGet());

        // Create the Mensaje
        MensajeDTO mensajeDTO = mensajeMapper.toDto(mensaje);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMensajeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mensajeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mensajeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mensaje in the database
        List<Mensaje> mensajeList = mensajeRepository.findAll();
        assertThat(mensajeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMensaje() throws Exception {
        int databaseSizeBeforeUpdate = mensajeRepository.findAll().size();
        mensaje.setId(count.incrementAndGet());

        // Create the Mensaje
        MensajeDTO mensajeDTO = mensajeMapper.toDto(mensaje);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMensajeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mensajeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mensaje in the database
        List<Mensaje> mensajeList = mensajeRepository.findAll();
        assertThat(mensajeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMensaje() throws Exception {
        int databaseSizeBeforeUpdate = mensajeRepository.findAll().size();
        mensaje.setId(count.incrementAndGet());

        // Create the Mensaje
        MensajeDTO mensajeDTO = mensajeMapper.toDto(mensaje);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMensajeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mensajeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mensaje in the database
        List<Mensaje> mensajeList = mensajeRepository.findAll();
        assertThat(mensajeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMensaje() throws Exception {
        // Initialize the database
        mensajeRepository.saveAndFlush(mensaje);

        int databaseSizeBeforeDelete = mensajeRepository.findAll().size();

        // Delete the mensaje
        restMensajeMockMvc
            .perform(delete(ENTITY_API_URL_ID, mensaje.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mensaje> mensajeList = mensajeRepository.findAll();
        assertThat(mensajeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
