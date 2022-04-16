package com.netjob.app.web.rest;

import com.netjob.app.repository.ServicioRepository;
import com.netjob.app.service.ServicioQueryService;
import com.netjob.app.service.ServicioService;
import com.netjob.app.service.criteria.ServicioCriteria;
import com.netjob.app.service.dto.ServicioDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.netjob.app.domain.Servicio}.
 */
@RestController
@RequestMapping("/api")
public class ServicioResource {

    private final Logger log = LoggerFactory.getLogger(ServicioResource.class);

    private static final String ENTITY_NAME = "servicio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServicioService servicioService;

    private final ServicioRepository servicioRepository;

    private final ServicioQueryService servicioQueryService;

    public ServicioResource(
        ServicioService servicioService,
        ServicioRepository servicioRepository,
        ServicioQueryService servicioQueryService
    ) {
        this.servicioService = servicioService;
        this.servicioRepository = servicioRepository;
        this.servicioQueryService = servicioQueryService;
    }

    /**
     * {@code POST  /servicios} : Create a new servicio.
     *
     * @param servicioDTO the servicioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new servicioDTO, or with status {@code 400 (Bad Request)} if the servicio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/servicios")
    public ResponseEntity<ServicioDTO> createServicio(@Valid @RequestBody ServicioDTO servicioDTO) throws URISyntaxException {
        log.debug("REST request to save Servicio : {}", servicioDTO);
        if (servicioDTO.getId() != null) {
            throw new BadRequestAlertException("A new servicio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServicioDTO result = servicioService.save(servicioDTO);
        return ResponseEntity
            .created(new URI("/api/servicios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /servicios/:id} : Updates an existing servicio.
     *
     * @param id the id of the servicioDTO to save.
     * @param servicioDTO the servicioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated servicioDTO,
     * or with status {@code 400 (Bad Request)} if the servicioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the servicioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/servicios/{id}")
    public ResponseEntity<ServicioDTO> updateServicio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServicioDTO servicioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Servicio : {}, {}", id, servicioDTO);
        if (servicioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, servicioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!servicioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ServicioDTO result = servicioService.save(servicioDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, servicioDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /servicios/:id} : Partial updates given fields of an existing servicio, field will ignore if it is null
     *
     * @param id the id of the servicioDTO to save.
     * @param servicioDTO the servicioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated servicioDTO,
     * or with status {@code 400 (Bad Request)} if the servicioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the servicioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the servicioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/servicios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServicioDTO> partialUpdateServicio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServicioDTO servicioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Servicio partially : {}, {}", id, servicioDTO);
        if (servicioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, servicioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!servicioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServicioDTO> result = servicioService.partialUpdate(servicioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, servicioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /servicios} : get all the servicios.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of servicios in body.
     */
    @GetMapping("/servicios")
    public ResponseEntity<List<ServicioDTO>> getAllServicios(
        ServicioCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Servicios by criteria: {}", criteria);
        Page<ServicioDTO> page = servicioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /servicios/count} : count all the servicios.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/servicios/count")
    public ResponseEntity<Long> countServicios(ServicioCriteria criteria) {
        log.debug("REST request to count Servicios by criteria: {}", criteria);
        return ResponseEntity.ok().body(servicioQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /servicios/:id} : get the "id" servicio.
     *
     * @param id the id of the servicioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the servicioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/servicios/{id}")
    public ResponseEntity<ServicioDTO> getServicio(@PathVariable Long id) {
        log.debug("REST request to get Servicio : {}", id);
        Optional<ServicioDTO> servicioDTO = servicioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(servicioDTO);
    }

    /**
     * {@code DELETE  /servicios/:id} : delete the "id" servicio.
     *
     * @param id the id of the servicioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/servicios/{id}")
    public ResponseEntity<Void> deleteServicio(@PathVariable Long id) {
        log.debug("REST request to delete Servicio : {}", id);
        servicioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /*Servicios vinculados a un usuario concreto*/
    @GetMapping("/servicios/usuario")
    public ResponseEntity<List<ServicioDTO>> getServicioByUsuario(
        @RequestParam(value = "id") Long userId,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        Page<ServicioDTO> page = servicioService.findAllByUsuario_id(userId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /*Servicios vinculados a una categoría concreta*/
    @GetMapping("/servicios/categoria")
    public ResponseEntity<List<ServicioDTO>> getServicioByCategoria(
        @RequestParam(value = "id") Long id,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        Page<ServicioDTO> page = servicioService.findAllByCategoria_id(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /*Servicios vinculados a una categoría concreta*/
    @GetMapping("/servicios/destacados")
    public ResponseEntity<List<ServicioDTO>> getServicioByCategoria(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        Page<ServicioDTO> page = servicioService.findByDestacadoTrue(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
