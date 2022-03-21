package com.netjob.app.service;

import com.netjob.app.service.dto.ServicioDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.netjob.app.domain.Servicio}.
 */
public interface ServicioService {
    /**
     * Save a servicio.
     *
     * @param servicioDTO the entity to save.
     * @return the persisted entity.
     */
    ServicioDTO save(ServicioDTO servicioDTO);

    /**
     * Partially updates a servicio.
     *
     * @param servicioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ServicioDTO> partialUpdate(ServicioDTO servicioDTO);

    /**
     * Get all the servicios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ServicioDTO> findAll(Pageable pageable);

    /**
     * Get all the servicios with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ServicioDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" servicio.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ServicioDTO> findOne(Long id);

    /**
     * Delete the "id" servicio.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
