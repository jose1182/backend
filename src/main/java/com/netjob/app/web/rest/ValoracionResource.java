package com.netjob.app.web.rest;

import com.netjob.app.repository.ValoracionRepository;
import com.netjob.app.service.ValoracionQueryService;
import com.netjob.app.service.ValoracionService;
import com.netjob.app.service.criteria.ValoracionCriteria;
import com.netjob.app.service.dto.ValoracionDTO;
import com.netjob.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.netjob.app.domain.Valoracion}.
 */
@RestController
@RequestMapping("/api")
public class ValoracionResource {

    private final Logger log = LoggerFactory.getLogger(ValoracionResource.class);

    private static final String ENTITY_NAME = "valoracion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ValoracionService valoracionService;

    private final ValoracionRepository valoracionRepository;

    private final ValoracionQueryService valoracionQueryService;

    public ValoracionResource(
        ValoracionService valoracionService,
        ValoracionRepository valoracionRepository,
        ValoracionQueryService valoracionQueryService
    ) {
        this.valoracionService = valoracionService;
        this.valoracionRepository = valoracionRepository;
        this.valoracionQueryService = valoracionQueryService;
    }

    /**
     * {@code POST  /valoracions} : Create a new valoracion.
     *
     * @param valoracionDTO the valoracionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new valoracionDTO, or with status {@code 400 (Bad Request)} if the valoracion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/valoracions")
    public ResponseEntity<ValoracionDTO> createValoracion(@Valid @RequestBody ValoracionDTO valoracionDTO) throws URISyntaxException {
        log.debug("REST request to save Valoracion : {}", valoracionDTO);
        if (valoracionDTO.getId() != null) {
            throw new BadRequestAlertException("A new valoracion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ValoracionDTO result = valoracionService.save(valoracionDTO);
        return ResponseEntity
            .created(new URI("/api/valoracions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /valoracions/:id} : Updates an existing valoracion.
     *
     * @param id the id of the valoracionDTO to save.
     * @param valoracionDTO the valoracionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated valoracionDTO,
     * or with status {@code 400 (Bad Request)} if the valoracionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the valoracionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/valoracions/{id}")
    public ResponseEntity<ValoracionDTO> updateValoracion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ValoracionDTO valoracionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Valoracion : {}, {}", id, valoracionDTO);
        if (valoracionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, valoracionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!valoracionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ValoracionDTO result = valoracionService.save(valoracionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, valoracionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /valoracions/:id} : Partial updates given fields of an existing valoracion, field will ignore if it is null
     *
     * @param id the id of the valoracionDTO to save.
     * @param valoracionDTO the valoracionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated valoracionDTO,
     * or with status {@code 400 (Bad Request)} if the valoracionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the valoracionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the valoracionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/valoracions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ValoracionDTO> partialUpdateValoracion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ValoracionDTO valoracionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Valoracion partially : {}, {}", id, valoracionDTO);
        if (valoracionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, valoracionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!valoracionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ValoracionDTO> result = valoracionService.partialUpdate(valoracionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, valoracionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /valoracions} : get all the valoracions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of valoracions in body.
     */
    @GetMapping("/valoracions")
    public ResponseEntity<List<ValoracionDTO>> getAllValoracions(ValoracionCriteria criteria) {
        log.debug("REST request to get Valoracions by criteria: {}", criteria);
        List<ValoracionDTO> entityList = valoracionQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /valoracions/count} : count all the valoracions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/valoracions/count")
    public ResponseEntity<Long> countValoracions(ValoracionCriteria criteria) {
        log.debug("REST request to count Valoracions by criteria: {}", criteria);
        return ResponseEntity.ok().body(valoracionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /valoracions/:id} : get the "id" valoracion.
     *
     * @param id the id of the valoracionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the valoracionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/valoracions/{id}")
    public ResponseEntity<ValoracionDTO> getValoracion(@PathVariable Long id) {
        log.debug("REST request to get Valoracion : {}", id);
        Optional<ValoracionDTO> valoracionDTO = valoracionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(valoracionDTO);
    }

    /**
     * {@code DELETE  /valoracions/:id} : delete the "id" valoracion.
     *
     * @param id the id of the valoracionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/valoracions/{id}")
    public ResponseEntity<Void> deleteValoracion(@PathVariable Long id) {
        log.debug("REST request to delete Valoracion : {}", id);
        valoracionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
