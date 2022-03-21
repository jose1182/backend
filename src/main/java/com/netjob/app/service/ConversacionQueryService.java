package com.netjob.app.service;

import com.netjob.app.domain.*; // for static metamodels
import com.netjob.app.domain.Conversacion;
import com.netjob.app.repository.ConversacionRepository;
import com.netjob.app.service.criteria.ConversacionCriteria;
import com.netjob.app.service.dto.ConversacionDTO;
import com.netjob.app.service.mapper.ConversacionMapper;
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
 * Service for executing complex queries for {@link Conversacion} entities in the database.
 * The main input is a {@link ConversacionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ConversacionDTO} or a {@link Page} of {@link ConversacionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConversacionQueryService extends QueryService<Conversacion> {

    private final Logger log = LoggerFactory.getLogger(ConversacionQueryService.class);

    private final ConversacionRepository conversacionRepository;

    private final ConversacionMapper conversacionMapper;

    public ConversacionQueryService(ConversacionRepository conversacionRepository, ConversacionMapper conversacionMapper) {
        this.conversacionRepository = conversacionRepository;
        this.conversacionMapper = conversacionMapper;
    }

    /**
     * Return a {@link List} of {@link ConversacionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ConversacionDTO> findByCriteria(ConversacionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Conversacion> specification = createSpecification(criteria);
        return conversacionMapper.toDto(conversacionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ConversacionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ConversacionDTO> findByCriteria(ConversacionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Conversacion> specification = createSpecification(criteria);
        return conversacionRepository.findAll(specification, page).map(conversacionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConversacionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Conversacion> specification = createSpecification(criteria);
        return conversacionRepository.count(specification);
    }

    /**
     * Function to convert {@link ConversacionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Conversacion> createSpecification(ConversacionCriteria criteria) {
        Specification<Conversacion> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Conversacion_.id));
            }
            if (criteria.getUsuarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUsuarioId(),
                            root -> root.join(Conversacion_.usuarios, JoinType.LEFT).get(Usuario_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
