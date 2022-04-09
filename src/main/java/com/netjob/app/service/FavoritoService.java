package com.netjob.app.service;

import com.netjob.app.service.dto.FavoritoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.netjob.app.domain.Favorito}.
 */
public interface FavoritoService {
    /**
     * Save a favorito.
     *
     * @param favoritoDTO the entity to save.
     * @return the persisted entity.
     */
    FavoritoDTO save(FavoritoDTO favoritoDTO);

    /**
     * Partially updates a favorito.
     *
     * @param favoritoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FavoritoDTO> partialUpdate(FavoritoDTO favoritoDTO);

    /**
     * Get all the favoritos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FavoritoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" favorito.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FavoritoDTO> findOne(Long id);

    /**
     * Delete the "id" favorito.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /* Servicio que devuelve los favoritos de un usuario */
    Page<FavoritoDTO> findAllByUsuario_id(Long id, Pageable pageable);
}
