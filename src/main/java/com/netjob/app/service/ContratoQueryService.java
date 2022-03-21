package com.netjob.app.service;

import com.netjob.app.domain.*; // for static metamodels
import com.netjob.app.domain.Contrato;
import com.netjob.app.repository.ContratoRepository;
import com.netjob.app.service.criteria.ContratoCriteria;
import com.netjob.app.service.dto.ContratoDTO;
import com.netjob.app.service.mapper.ContratoMapper;
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
 * Service for executing complex queries for {@link Contrato} entities in the database.
 * The main input is a {@link ContratoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContratoDTO} or a {@link Page} of {@link ContratoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContratoQueryService extends QueryService<Contrato> {

    private final Logger log = LoggerFactory.getLogger(ContratoQueryService.class);

    private final ContratoRepository contratoRepository;

    private final ContratoMapper contratoMapper;

    public ContratoQueryService(ContratoRepository contratoRepository, ContratoMapper contratoMapper) {
        this.contratoRepository = contratoRepository;
        this.contratoMapper = contratoMapper;
    }

    /**
     * Return a {@link List} of {@link ContratoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContratoDTO> findByCriteria(ContratoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Contrato> specification = createSpecification(criteria);
        return contratoMapper.toDto(contratoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContratoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContratoDTO> findByCriteria(ContratoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Contrato> specification = createSpecification(criteria);
        return contratoRepository.findAll(specification, page).map(contratoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContratoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Contrato> specification = createSpecification(criteria);
        return contratoRepository.count(specification);
    }

    /**
     * Function to convert {@link ContratoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Contrato> createSpecification(ContratoCriteria criteria) {
        Specification<Contrato> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Contrato_.id));
            }
            if (criteria.getPreciofinal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPreciofinal(), Contrato_.preciofinal));
            }
            if (criteria.getFecha() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFecha(), Contrato_.fecha));
            }
            if (criteria.getUsuarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUsuarioId(), root -> root.join(Contrato_.usuario, JoinType.LEFT).get(Usuario_.id))
                    );
            }
            if (criteria.getServicioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getServicioId(), root -> root.join(Contrato_.servicio, JoinType.LEFT).get(Servicio_.id))
                    );
            }
        }
        return specification;
    }
}
