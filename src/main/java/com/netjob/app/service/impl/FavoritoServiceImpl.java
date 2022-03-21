package com.netjob.app.service.impl;

import com.netjob.app.domain.Favorito;
import com.netjob.app.repository.FavoritoRepository;
import com.netjob.app.service.FavoritoService;
import com.netjob.app.service.dto.FavoritoDTO;
import com.netjob.app.service.mapper.FavoritoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Favorito}.
 */
@Service
@Transactional
public class FavoritoServiceImpl implements FavoritoService {

    private final Logger log = LoggerFactory.getLogger(FavoritoServiceImpl.class);

    private final FavoritoRepository favoritoRepository;

    private final FavoritoMapper favoritoMapper;

    public FavoritoServiceImpl(FavoritoRepository favoritoRepository, FavoritoMapper favoritoMapper) {
        this.favoritoRepository = favoritoRepository;
        this.favoritoMapper = favoritoMapper;
    }

    @Override
    public FavoritoDTO save(FavoritoDTO favoritoDTO) {
        log.debug("Request to save Favorito : {}", favoritoDTO);
        Favorito favorito = favoritoMapper.toEntity(favoritoDTO);
        favorito = favoritoRepository.save(favorito);
        return favoritoMapper.toDto(favorito);
    }

    @Override
    public Optional<FavoritoDTO> partialUpdate(FavoritoDTO favoritoDTO) {
        log.debug("Request to partially update Favorito : {}", favoritoDTO);

        return favoritoRepository
            .findById(favoritoDTO.getId())
            .map(existingFavorito -> {
                favoritoMapper.partialUpdate(existingFavorito, favoritoDTO);

                return existingFavorito;
            })
            .map(favoritoRepository::save)
            .map(favoritoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FavoritoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Favoritos");
        return favoritoRepository.findAll(pageable).map(favoritoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FavoritoDTO> findOne(Long id) {
        log.debug("Request to get Favorito : {}", id);
        return favoritoRepository.findById(id).map(favoritoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Favorito : {}", id);
        favoritoRepository.deleteById(id);
    }
}
