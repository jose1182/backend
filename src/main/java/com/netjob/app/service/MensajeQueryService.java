package com.netjob.app.service;

import com.netjob.app.domain.*; // for static metamodels
import com.netjob.app.domain.Mensaje;
import com.netjob.app.repository.MensajeRepository;
import com.netjob.app.service.criteria.MensajeCriteria;
import com.netjob.app.service.dto.MensajeDTO;
import com.netjob.app.service.mapper.MensajeMapper;
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
 * Service for executing complex queries for {@link Mensaje} entities in the database.
 * The main input is a {@link MensajeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MensajeDTO} or a {@link Page} of {@link MensajeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MensajeQueryService extends QueryService<Mensaje> {

    private final Logger log = LoggerFactory.getLogger(MensajeQueryService.class);

    private final MensajeRepository mensajeRepository;

    private final MensajeMapper mensajeMapper;

    public MensajeQueryService(MensajeRepository mensajeRepository, MensajeMapper mensajeMapper) {
        this.mensajeRepository = mensajeRepository;
        this.mensajeMapper = mensajeMapper;
    }

    /**
     * Return a {@link List} of {@link MensajeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MensajeDTO> findByCriteria(MensajeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Mensaje> specification = createSpecification(criteria);
        return mensajeMapper.toDto(mensajeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MensajeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MensajeDTO> findByCriteria(MensajeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Mensaje> specification = createSpecification(criteria);
        return mensajeRepository.findAll(specification, page).map(mensajeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MensajeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Mensaje> specification = createSpecification(criteria);
        return mensajeRepository.count(specification);
    }

    /**
     * Function to convert {@link MensajeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Mensaje> createSpecification(MensajeCriteria criteria) {
        Specification<Mensaje> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Mensaje_.id));
            }
            if (criteria.getTexto() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTexto(), Mensaje_.texto));
            }
            if (criteria.getFecha() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFecha(), Mensaje_.fecha));
            }
            if (criteria.getEmisorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEmisorId(), root -> root.join(Mensaje_.emisor, JoinType.LEFT).get(Usuario_.id))
                    );
            }
            if (criteria.getReceptorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getReceptorId(), root -> root.join(Mensaje_.receptor, JoinType.LEFT).get(Usuario_.id))
                    );
            }
            if (criteria.getConversacionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getConversacionId(),
                            root -> root.join(Mensaje_.conversacion, JoinType.LEFT).get(Conversacion_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
