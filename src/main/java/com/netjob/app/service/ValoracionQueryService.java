package com.netjob.app.service;

import com.netjob.app.domain.*; // for static metamodels
import com.netjob.app.domain.Valoracion;
import com.netjob.app.repository.ValoracionRepository;
import com.netjob.app.service.criteria.ValoracionCriteria;
import com.netjob.app.service.dto.ValoracionDTO;
import com.netjob.app.service.mapper.ValoracionMapper;
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
 * Service for executing complex queries for {@link Valoracion} entities in the database.
 * The main input is a {@link ValoracionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ValoracionDTO} or a {@link Page} of {@link ValoracionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ValoracionQueryService extends QueryService<Valoracion> {

    private final Logger log = LoggerFactory.getLogger(ValoracionQueryService.class);

    private final ValoracionRepository valoracionRepository;

    private final ValoracionMapper valoracionMapper;

    public ValoracionQueryService(ValoracionRepository valoracionRepository, ValoracionMapper valoracionMapper) {
        this.valoracionRepository = valoracionRepository;
        this.valoracionMapper = valoracionMapper;
    }

    /**
     * Return a {@link List} of {@link ValoracionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ValoracionDTO> findByCriteria(ValoracionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Valoracion> specification = createSpecification(criteria);
        return valoracionMapper.toDto(valoracionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ValoracionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ValoracionDTO> findByCriteria(ValoracionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Valoracion> specification = createSpecification(criteria);
        return valoracionRepository.findAll(specification, page).map(valoracionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ValoracionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Valoracion> specification = createSpecification(criteria);
        return valoracionRepository.count(specification);
    }

    /**
     * Function to convert {@link ValoracionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Valoracion> createSpecification(ValoracionCriteria criteria) {
        Specification<Valoracion> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Valoracion_.id));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), Valoracion_.descripcion));
            }
            if (criteria.getFecha() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFecha(), Valoracion_.fecha));
            }
            if (criteria.getId_servicio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId_servicio(), Valoracion_.id_servicio));
            }
        }
        return specification;
    }
}
