package com.netjob.app.service;

import com.netjob.app.service.dto.ContratoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.netjob.app.domain.Contrato}.
 */
public interface ContratoService {
    /**
     * Save a contrato.
     *
     * @param contratoDTO the entity to save.
     * @return the persisted entity.
     */
    ContratoDTO save(ContratoDTO contratoDTO);

    /**
     * Partially updates a contrato.
     *
     * @param contratoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContratoDTO> partialUpdate(ContratoDTO contratoDTO);

    /**
     * Get all the contratoes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContratoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" contrato.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContratoDTO> findOne(Long id);

    /**
     * Delete the "id" contrato.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /*Listado de contratos filtrados por id de usuario*/
    Page<ContratoDTO> findByUsuario_id(Long id, Pageable page);
}
