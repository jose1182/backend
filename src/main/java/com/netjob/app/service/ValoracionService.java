package com.netjob.app.service;

import com.netjob.app.domain.Valoracion;
import com.netjob.app.repository.ValoracionRepository;
import com.netjob.app.service.dto.ValoracionDTO;
import com.netjob.app.service.mapper.ValoracionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Valoracion}.
 */
@Service
@Transactional
public class ValoracionService {

    private final Logger log = LoggerFactory.getLogger(ValoracionService.class);

    private final ValoracionRepository valoracionRepository;

    private final ValoracionMapper valoracionMapper;

    public ValoracionService(ValoracionRepository valoracionRepository, ValoracionMapper valoracionMapper) {
        this.valoracionRepository = valoracionRepository;
        this.valoracionMapper = valoracionMapper;
    }

    /**
     * Save a valoracion.
     *
     * @param valoracionDTO the entity to save.
     * @return the persisted entity.
     */
    public ValoracionDTO save(ValoracionDTO valoracionDTO) {
        log.debug("Request to save Valoracion : {}", valoracionDTO);
        Valoracion valoracion = valoracionMapper.toEntity(valoracionDTO);
        valoracion = valoracionRepository.save(valoracion);
        return valoracionMapper.toDto(valoracion);
    }

    /**
     * Partially update a valoracion.
     *
     * @param valoracionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ValoracionDTO> partialUpdate(ValoracionDTO valoracionDTO) {
        log.debug("Request to partially update Valoracion : {}", valoracionDTO);

        return valoracionRepository
            .findById(valoracionDTO.getId())
            .map(existingValoracion -> {
                valoracionMapper.partialUpdate(existingValoracion, valoracionDTO);

                return existingValoracion;
            })
            .map(valoracionRepository::save)
            .map(valoracionMapper::toDto);
    }

    /**
     * Get all the valoracions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ValoracionDTO> findAll() {
        log.debug("Request to get all Valoracions");
        return valoracionRepository.findAll().stream().map(valoracionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one valoracion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ValoracionDTO> findOne(Long id) {
        log.debug("Request to get Valoracion : {}", id);
        return valoracionRepository.findById(id).map(valoracionMapper::toDto);
    }

    /**
     * Delete the valoracion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Valoracion : {}", id);
        valoracionRepository.deleteById(id);
    }
}
