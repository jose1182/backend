package com.netjob.app.service;

import com.netjob.app.domain.*; // for static metamodels
import com.netjob.app.domain.Servicio;
import com.netjob.app.repository.ServicioRepository;
import com.netjob.app.service.criteria.ServicioCriteria;
import com.netjob.app.service.dto.ServicioDTO;
import com.netjob.app.service.mapper.ServicioMapper;
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
 * Service for executing complex queries for {@link Servicio} entities in the database.
 * The main input is a {@link ServicioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ServicioDTO} or a {@link Page} of {@link ServicioDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ServicioQueryService extends QueryService<Servicio> {

    private final Logger log = LoggerFactory.getLogger(ServicioQueryService.class);

    private final ServicioRepository servicioRepository;

    private final ServicioMapper servicioMapper;

    public ServicioQueryService(ServicioRepository servicioRepository, ServicioMapper servicioMapper) {
        this.servicioRepository = servicioRepository;
        this.servicioMapper = servicioMapper;
    }

    /**
     * Return a {@link List} of {@link ServicioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ServicioDTO> findByCriteria(ServicioCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Servicio> specification = createSpecification(criteria);
        return servicioMapper.toDto(servicioRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ServicioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ServicioDTO> findByCriteria(ServicioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Servicio> specification = createSpecification(criteria);
        return servicioRepository.findAll(specification, page).map(servicioMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ServicioCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Servicio> specification = createSpecification(criteria);
        return servicioRepository.count(specification);
    }

    /**
     * Function to convert {@link ServicioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Servicio> createSpecification(ServicioCriteria criteria) {
        Specification<Servicio> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Servicio_.id));
            }
            if (criteria.getTitulo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitulo(), Servicio_.titulo));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), Servicio_.descripcion));
            }
            if (criteria.getDisponibilidad() != null) {
                specification = specification.and(buildSpecification(criteria.getDisponibilidad(), Servicio_.disponibilidad));
            }
            if (criteria.getPreciohora() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPreciohora(), Servicio_.preciohora));
            }
            if (criteria.getPreciotraslado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPreciotraslado(), Servicio_.preciotraslado));
            }
            if (criteria.getFechacreacion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechacreacion(), Servicio_.fechacreacion));
            }
            if (criteria.getFechaactualizacion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaactualizacion(), Servicio_.fechaactualizacion));
            }
            if (criteria.getUsuarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUsuarioId(), root -> root.join(Servicio_.usuario, JoinType.LEFT).get(Usuario_.id))
                    );
            }
            if (criteria.getCategoriaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoriaId(),
                            root -> root.join(Servicio_.categorias, JoinType.LEFT).get(Categoria_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
