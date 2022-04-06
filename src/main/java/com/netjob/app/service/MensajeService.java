package com.netjob.app.service;

import com.netjob.app.service.dto.MensajeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.netjob.app.domain.Mensaje}.
 */
public interface MensajeService {
    /**
     * Save a mensaje.
     *
     * @param mensajeDTO the entity to save.
     * @return the persisted entity.
     */
    MensajeDTO save(MensajeDTO mensajeDTO);

    /**
     * Partially updates a mensaje.
     *
     * @param mensajeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MensajeDTO> partialUpdate(MensajeDTO mensajeDTO);

    /**
     * Get all the mensajes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MensajeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" mensaje.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MensajeDTO> findOne(Long id);

    /**
     * Delete the "id" mensaje.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
