package com.netjob.app.service;

import com.netjob.app.service.dto.ConversacionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.netjob.app.domain.Conversacion}.
 */
public interface ConversacionService {
    /**
     * Save a conversacion.
     *
     * @param conversacionDTO the entity to save.
     * @return the persisted entity.
     */
    ConversacionDTO save(ConversacionDTO conversacionDTO);

    /**
     * Partially updates a conversacion.
     *
     * @param conversacionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ConversacionDTO> partialUpdate(ConversacionDTO conversacionDTO);

    /**
     * Get all the conversacions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ConversacionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" conversacion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConversacionDTO> findOne(Long id);

    /**
     * Delete the "id" conversacion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
