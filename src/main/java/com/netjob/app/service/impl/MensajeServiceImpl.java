package com.netjob.app.service.impl;

import com.netjob.app.domain.Mensaje;
import com.netjob.app.repository.MensajeRepository;
import com.netjob.app.service.MensajeService;
import com.netjob.app.service.dto.MensajeDTO;
import com.netjob.app.service.mapper.MensajeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Mensaje}.
 */
@Service
@Transactional
public class MensajeServiceImpl implements MensajeService {

    private final Logger log = LoggerFactory.getLogger(MensajeServiceImpl.class);

    private final MensajeRepository mensajeRepository;

    private final MensajeMapper mensajeMapper;

    public MensajeServiceImpl(MensajeRepository mensajeRepository, MensajeMapper mensajeMapper) {
        this.mensajeRepository = mensajeRepository;
        this.mensajeMapper = mensajeMapper;
    }

    @Override
    public MensajeDTO save(MensajeDTO mensajeDTO) {
        log.debug("Request to save Mensaje : {}", mensajeDTO);
        Mensaje mensaje = mensajeMapper.toEntity(mensajeDTO);
        mensaje = mensajeRepository.save(mensaje);
        return mensajeMapper.toDto(mensaje);
    }

    @Override
    public Optional<MensajeDTO> partialUpdate(MensajeDTO mensajeDTO) {
        log.debug("Request to partially update Mensaje : {}", mensajeDTO);

        return mensajeRepository
            .findById(mensajeDTO.getId())
            .map(existingMensaje -> {
                mensajeMapper.partialUpdate(existingMensaje, mensajeDTO);

                return existingMensaje;
            })
            .map(mensajeRepository::save)
            .map(mensajeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MensajeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Mensajes");
        return mensajeRepository.findAll(pageable).map(mensajeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MensajeDTO> findOne(Long id) {
        log.debug("Request to get Mensaje : {}", id);
        return mensajeRepository.findById(id).map(mensajeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Mensaje : {}", id);
        mensajeRepository.deleteById(id);
    }
}
