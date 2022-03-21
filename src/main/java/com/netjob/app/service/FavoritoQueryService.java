package com.netjob.app.service;

import com.netjob.app.domain.*; // for static metamodels
import com.netjob.app.domain.Favorito;
import com.netjob.app.repository.FavoritoRepository;
import com.netjob.app.service.criteria.FavoritoCriteria;
import com.netjob.app.service.dto.FavoritoDTO;
import com.netjob.app.service.mapper.FavoritoMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Favorito} entities in the database.
 * The main input is a {@link FavoritoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FavoritoDTO} or a {@link Page} of {@link FavoritoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FavoritoQueryService extends QueryService<Favorito> {

    private final Logger log = LoggerFactory.getLogger(FavoritoQueryService.class);

    private final FavoritoRepository favoritoRepository;

    private final FavoritoMapper favoritoMapper;

    public FavoritoQueryService(FavoritoRepository favoritoRepository, FavoritoMapper favoritoMapper) {
        this.favoritoRepository = favoritoRepository;
        this.favoritoMapper = favoritoMapper;
    }

    /**
     * Return a {@link List} of {@link FavoritoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FavoritoDTO> findByCriteria(FavoritoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Favorito> specification = createSpecification(criteria);
        return favoritoMapper.toDto(favoritoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FavoritoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FavoritoDTO> findByCriteria(FavoritoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Favorito> specification = createSpecification(criteria);
        return favoritoRepository.findAll(specification, page).map(favoritoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FavoritoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Favorito> specification = createSpecification(criteria);
        return favoritoRepository.count(specification);
    }

    /**
     * Function to convert {@link FavoritoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Favorito> createSpecification(FavoritoCriteria criteria) {
        Specification<Favorito> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Favorito_.id));
            }
            if (criteria.getUsuarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUsuarioId(), root -> root.join(Favorito_.usuario, JoinType.LEFT).get(Usuario_.id))
                    );
            }
            if (criteria.getServicioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getServicioId(), root -> root.join(Favorito_.servicio, JoinType.LEFT).get(Servicio_.id))
                    );
            }
        }
        return specification;
    }
}
