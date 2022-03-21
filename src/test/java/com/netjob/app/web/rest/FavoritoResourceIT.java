package com.netjob.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.netjob.app.IntegrationTest;
import com.netjob.app.domain.Favorito;
import com.netjob.app.domain.Servicio;
import com.netjob.app.domain.Usuario;
import com.netjob.app.repository.FavoritoRepository;
import com.netjob.app.service.criteria.FavoritoCriteria;
import com.netjob.app.service.dto.FavoritoDTO;
import com.netjob.app.service.mapper.FavoritoMapper;
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
 * Integration tests for the {@link FavoritoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FavoritoResourceIT {

    private static final String ENTITY_API_URL = "/api/favoritos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private FavoritoMapper favoritoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFavoritoMockMvc;

    private Favorito favorito;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Favorito createEntity(EntityManager em) {
        Favorito favorito = new Favorito();
        return favorito;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Favorito createUpdatedEntity(EntityManager em) {
        Favorito favorito = new Favorito();
        return favorito;
    }

    @BeforeEach
    public void initTest() {
        favorito = createEntity(em);
    }

    @Test
    @Transactional
    void createFavorito() throws Exception {
        int databaseSizeBeforeCreate = favoritoRepository.findAll().size();
        // Create the Favorito
        FavoritoDTO favoritoDTO = favoritoMapper.toDto(favorito);
        restFavoritoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(favoritoDTO)))
            .andExpect(status().isCreated());

        // Validate the Favorito in the database
        List<Favorito> favoritoList = favoritoRepository.findAll();
        assertThat(favoritoList).hasSize(databaseSizeBeforeCreate + 1);
        Favorito testFavorito = favoritoList.get(favoritoList.size() - 1);
    }

    @Test
    @Transactional
    void createFavoritoWithExistingId() throws Exception {
        // Create the Favorito with an existing ID
        favorito.setId(1L);
        FavoritoDTO favoritoDTO = favoritoMapper.toDto(favorito);

        int databaseSizeBeforeCreate = favoritoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFavoritoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(favoritoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Favorito in the database
        List<Favorito> favoritoList = favoritoRepository.findAll();
        assertThat(favoritoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFavoritos() throws Exception {
        // Initialize the database
        favoritoRepository.saveAndFlush(favorito);

        // Get all the favoritoList
        restFavoritoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(favorito.getId().intValue())));
    }

    @Test
    @Transactional
    void getFavorito() throws Exception {
        // Initialize the database
        favoritoRepository.saveAndFlush(favorito);

        // Get the favorito
        restFavoritoMockMvc
            .perform(get(ENTITY_API_URL_ID, favorito.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(favorito.getId().intValue()));
    }

    @Test
    @Transactional
    void getFavoritosByIdFiltering() throws Exception {
        // Initialize the database
        favoritoRepository.saveAndFlush(favorito);

        Long id = favorito.getId();

        defaultFavoritoShouldBeFound("id.equals=" + id);
        defaultFavoritoShouldNotBeFound("id.notEquals=" + id);

        defaultFavoritoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFavoritoShouldNotBeFound("id.greaterThan=" + id);

        defaultFavoritoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFavoritoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFavoritosByUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        favoritoRepository.saveAndFlush(favorito);
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
        favorito.setUsuario(usuario);
        favoritoRepository.saveAndFlush(favorito);
        Long usuarioId = usuario.getId();

        // Get all the favoritoList where usuario equals to usuarioId
        defaultFavoritoShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the favoritoList where usuario equals to (usuarioId + 1)
        defaultFavoritoShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    @Test
    @Transactional
    void getAllFavoritosByServicioIsEqualToSomething() throws Exception {
        // Initialize the database
        favoritoRepository.saveAndFlush(favorito);
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
        favorito.setServicio(servicio);
        favoritoRepository.saveAndFlush(favorito);
        Long servicioId = servicio.getId();

        // Get all the favoritoList where servicio equals to servicioId
        defaultFavoritoShouldBeFound("servicioId.equals=" + servicioId);

        // Get all the favoritoList where servicio equals to (servicioId + 1)
        defaultFavoritoShouldNotBeFound("servicioId.equals=" + (servicioId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFavoritoShouldBeFound(String filter) throws Exception {
        restFavoritoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(favorito.getId().intValue())));

        // Check, that the count call also returns 1
        restFavoritoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFavoritoShouldNotBeFound(String filter) throws Exception {
        restFavoritoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFavoritoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFavorito() throws Exception {
        // Get the favorito
        restFavoritoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFavorito() throws Exception {
        // Initialize the database
        favoritoRepository.saveAndFlush(favorito);

        int databaseSizeBeforeUpdate = favoritoRepository.findAll().size();

        // Update the favorito
        Favorito updatedFavorito = favoritoRepository.findById(favorito.getId()).get();
        // Disconnect from session so that the updates on updatedFavorito are not directly saved in db
        em.detach(updatedFavorito);
        FavoritoDTO favoritoDTO = favoritoMapper.toDto(updatedFavorito);

        restFavoritoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, favoritoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(favoritoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Favorito in the database
        List<Favorito> favoritoList = favoritoRepository.findAll();
        assertThat(favoritoList).hasSize(databaseSizeBeforeUpdate);
        Favorito testFavorito = favoritoList.get(favoritoList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingFavorito() throws Exception {
        int databaseSizeBeforeUpdate = favoritoRepository.findAll().size();
        favorito.setId(count.incrementAndGet());

        // Create the Favorito
        FavoritoDTO favoritoDTO = favoritoMapper.toDto(favorito);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFavoritoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, favoritoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(favoritoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Favorito in the database
        List<Favorito> favoritoList = favoritoRepository.findAll();
        assertThat(favoritoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFavorito() throws Exception {
        int databaseSizeBeforeUpdate = favoritoRepository.findAll().size();
        favorito.setId(count.incrementAndGet());

        // Create the Favorito
        FavoritoDTO favoritoDTO = favoritoMapper.toDto(favorito);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFavoritoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(favoritoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Favorito in the database
        List<Favorito> favoritoList = favoritoRepository.findAll();
        assertThat(favoritoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFavorito() throws Exception {
        int databaseSizeBeforeUpdate = favoritoRepository.findAll().size();
        favorito.setId(count.incrementAndGet());

        // Create the Favorito
        FavoritoDTO favoritoDTO = favoritoMapper.toDto(favorito);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFavoritoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(favoritoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Favorito in the database
        List<Favorito> favoritoList = favoritoRepository.findAll();
        assertThat(favoritoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFavoritoWithPatch() throws Exception {
        // Initialize the database
        favoritoRepository.saveAndFlush(favorito);

        int databaseSizeBeforeUpdate = favoritoRepository.findAll().size();

        // Update the favorito using partial update
        Favorito partialUpdatedFavorito = new Favorito();
        partialUpdatedFavorito.setId(favorito.getId());

        restFavoritoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFavorito.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFavorito))
            )
            .andExpect(status().isOk());

        // Validate the Favorito in the database
        List<Favorito> favoritoList = favoritoRepository.findAll();
        assertThat(favoritoList).hasSize(databaseSizeBeforeUpdate);
        Favorito testFavorito = favoritoList.get(favoritoList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateFavoritoWithPatch() throws Exception {
        // Initialize the database
        favoritoRepository.saveAndFlush(favorito);

        int databaseSizeBeforeUpdate = favoritoRepository.findAll().size();

        // Update the favorito using partial update
        Favorito partialUpdatedFavorito = new Favorito();
        partialUpdatedFavorito.setId(favorito.getId());

        restFavoritoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFavorito.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFavorito))
            )
            .andExpect(status().isOk());

        // Validate the Favorito in the database
        List<Favorito> favoritoList = favoritoRepository.findAll();
        assertThat(favoritoList).hasSize(databaseSizeBeforeUpdate);
        Favorito testFavorito = favoritoList.get(favoritoList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingFavorito() throws Exception {
        int databaseSizeBeforeUpdate = favoritoRepository.findAll().size();
        favorito.setId(count.incrementAndGet());

        // Create the Favorito
        FavoritoDTO favoritoDTO = favoritoMapper.toDto(favorito);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFavoritoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, favoritoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(favoritoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Favorito in the database
        List<Favorito> favoritoList = favoritoRepository.findAll();
        assertThat(favoritoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFavorito() throws Exception {
        int databaseSizeBeforeUpdate = favoritoRepository.findAll().size();
        favorito.setId(count.incrementAndGet());

        // Create the Favorito
        FavoritoDTO favoritoDTO = favoritoMapper.toDto(favorito);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFavoritoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(favoritoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Favorito in the database
        List<Favorito> favoritoList = favoritoRepository.findAll();
        assertThat(favoritoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFavorito() throws Exception {
        int databaseSizeBeforeUpdate = favoritoRepository.findAll().size();
        favorito.setId(count.incrementAndGet());

        // Create the Favorito
        FavoritoDTO favoritoDTO = favoritoMapper.toDto(favorito);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFavoritoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(favoritoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Favorito in the database
        List<Favorito> favoritoList = favoritoRepository.findAll();
        assertThat(favoritoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFavorito() throws Exception {
        // Initialize the database
        favoritoRepository.saveAndFlush(favorito);

        int databaseSizeBeforeDelete = favoritoRepository.findAll().size();

        // Delete the favorito
        restFavoritoMockMvc
            .perform(delete(ENTITY_API_URL_ID, favorito.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Favorito> favoritoList = favoritoRepository.findAll();
        assertThat(favoritoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
