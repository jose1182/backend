package com.netjob.app.service.impl;

import com.netjob.app.domain.Contrato;
import com.netjob.app.repository.ContratoRepository;
import com.netjob.app.service.ContratoService;
import com.netjob.app.service.dto.ContratoDTO;
import com.netjob.app.service.mapper.ContratoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Contrato}.
 */
@Service
@Transactional
public class ContratoServiceImpl implements ContratoService {

    private final Logger log = LoggerFactory.getLogger(ContratoServiceImpl.class);

    private final ContratoRepository contratoRepository;

    private final ContratoMapper contratoMapper;

    public ContratoServiceImpl(ContratoRepository contratoRepository, ContratoMapper contratoMapper) {
        this.contratoRepository = contratoRepository;
        this.contratoMapper = contratoMapper;
    }

    @Override
    public ContratoDTO save(ContratoDTO contratoDTO) {
        log.debug("Request to save Contrato : {}", contratoDTO);
        Contrato contrato = contratoMapper.toEntity(contratoDTO);
        contrato = contratoRepository.save(contrato);
        return contratoMapper.toDto(contrato);
    }

    @Override
    public Optional<ContratoDTO> partialUpdate(ContratoDTO contratoDTO) {
        log.debug("Request to partially update Contrato : {}", contratoDTO);

        return contratoRepository
            .findById(contratoDTO.getId())
            .map(existingContrato -> {
                contratoMapper.partialUpdate(existingContrato, contratoDTO);

                return existingContrato;
            })
            .map(contratoRepository::save)
            .map(contratoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContratoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contratoes");
        return contratoRepository.findAll(pageable).map(contratoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContratoDTO> findOne(Long id) {
        log.debug("Request to get Contrato : {}", id);
        return contratoRepository.findById(id).map(contratoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Contrato : {}", id);
        contratoRepository.deleteById(id);
    }

    @Override
    public Page<ContratoDTO> findByUsuario_id(Long id, Pageable page) {
        return contratoRepository.findByUsuario_id(id, page).map(contratoMapper::toDto);
    }
}
