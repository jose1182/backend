package com.netjob.app.service.impl;

import com.netjob.app.domain.Conversacion;
import com.netjob.app.repository.ConversacionRepository;
import com.netjob.app.service.ConversacionService;
import com.netjob.app.service.dto.ConversacionDTO;
import com.netjob.app.service.mapper.ConversacionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Conversacion}.
 */
@Service
@Transactional
public class ConversacionServiceImpl implements ConversacionService {

    private final Logger log = LoggerFactory.getLogger(ConversacionServiceImpl.class);

    private final ConversacionRepository conversacionRepository;

    private final ConversacionMapper conversacionMapper;

    public ConversacionServiceImpl(ConversacionRepository conversacionRepository, ConversacionMapper conversacionMapper) {
        this.conversacionRepository = conversacionRepository;
        this.conversacionMapper = conversacionMapper;
    }

    @Override
    public ConversacionDTO save(ConversacionDTO conversacionDTO) {
        log.debug("Request to save Conversacion : {}", conversacionDTO);
        Conversacion conversacion = conversacionMapper.toEntity(conversacionDTO);
        conversacion = conversacionRepository.save(conversacion);
        return conversacionMapper.toDto(conversacion);
    }

    @Override
    public Optional<ConversacionDTO> partialUpdate(ConversacionDTO conversacionDTO) {
        log.debug("Request to partially update Conversacion : {}", conversacionDTO);

        return conversacionRepository
            .findById(conversacionDTO.getId())
            .map(existingConversacion -> {
                conversacionMapper.partialUpdate(existingConversacion, conversacionDTO);

                return existingConversacion;
            })
            .map(conversacionRepository::save)
            .map(conversacionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConversacionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Conversacions");
        return conversacionRepository.findAll(pageable).map(conversacionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConversacionDTO> findOne(Long id) {
        log.debug("Request to get Conversacion : {}", id);
        return conversacionRepository.findById(id).map(conversacionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Conversacion : {}", id);
        conversacionRepository.deleteById(id);
    }
}
