package com.netjob.app.web.rest;

import com.netjob.app.repository.ConversacionRepository;
import com.netjob.app.service.ConversacionQueryService;
import com.netjob.app.service.ConversacionService;
import com.netjob.app.service.criteria.ConversacionCriteria;
import com.netjob.app.service.dto.ConversacionDTO;
import com.netjob.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.netjob.app.domain.Conversacion}.
 */
@RestController
@RequestMapping("/api")
public class ConversacionResource {

    private final Logger log = LoggerFactory.getLogger(ConversacionResource.class);

    private static final String ENTITY_NAME = "conversacion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConversacionService conversacionService;

    private final ConversacionRepository conversacionRepository;

    private final ConversacionQueryService conversacionQueryService;

    public ConversacionResource(
        ConversacionService conversacionService,
        ConversacionRepository conversacionRepository,
        ConversacionQueryService conversacionQueryService
    ) {
        this.conversacionService = conversacionService;
        this.conversacionRepository = conversacionRepository;
        this.conversacionQueryService = conversacionQueryService;
    }

    /**
     * {@code POST  /conversacions} : Create a new conversacion.
     *
     * @param conversacionDTO the conversacionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new conversacionDTO, or with status {@code 400 (Bad Request)} if the conversacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/conversacions")
    public ResponseEntity<ConversacionDTO> createConversacion(@RequestBody ConversacionDTO conversacionDTO) throws URISyntaxException {
        log.debug("REST request to save Conversacion : {}", conversacionDTO);
        if (conversacionDTO.getId() != null) {
            throw new BadRequestAlertException("A new conversacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConversacionDTO result = conversacionService.save(conversacionDTO);
        return ResponseEntity
            .created(new URI("/api/conversacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /conversacions/:id} : Updates an existing conversacion.
     *
     * @param id the id of the conversacionDTO to save.
     * @param conversacionDTO the conversacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conversacionDTO,
     * or with status {@code 400 (Bad Request)} if the conversacionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the conversacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/conversacions/{id}")
    public ResponseEntity<ConversacionDTO> updateConversacion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConversacionDTO conversacionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Conversacion : {}, {}", id, conversacionDTO);
        if (conversacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conversacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conversacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ConversacionDTO result = conversacionService.save(conversacionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, conversacionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /conversacions/:id} : Partial updates given fields of an existing conversacion, field will ignore if it is null
     *
     * @param id the id of the conversacionDTO to save.
     * @param conversacionDTO the conversacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conversacionDTO,
     * or with status {@code 400 (Bad Request)} if the conversacionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the conversacionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the conversacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/conversacions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConversacionDTO> partialUpdateConversacion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConversacionDTO conversacionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Conversacion partially : {}, {}", id, conversacionDTO);
        if (conversacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conversacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conversacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConversacionDTO> result = conversacionService.partialUpdate(conversacionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, conversacionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /conversacions} : get all the conversacions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of conversacions in body.
     */
    @GetMapping("/conversacions")
    public ResponseEntity<List<ConversacionDTO>> getAllConversacions(
        ConversacionCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Conversacions by criteria: {}", criteria);
        Page<ConversacionDTO> page = conversacionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /conversacions/count} : count all the conversacions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/conversacions/count")
    public ResponseEntity<Long> countConversacions(ConversacionCriteria criteria) {
        log.debug("REST request to count Conversacions by criteria: {}", criteria);
        return ResponseEntity.ok().body(conversacionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /conversacions/:id} : get the "id" conversacion.
     *
     * @param id the id of the conversacionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the conversacionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/conversacions/{id}")
    public ResponseEntity<ConversacionDTO> getConversacion(@PathVariable Long id) {
        log.debug("REST request to get Conversacion : {}", id);
        Optional<ConversacionDTO> conversacionDTO = conversacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(conversacionDTO);
    }

    /**
     * {@code DELETE  /conversacions/:id} : delete the "id" conversacion.
     *
     * @param id the id of the conversacionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/conversacions/{id}")
    public ResponseEntity<Void> deleteConversacion(@PathVariable Long id) {
        log.debug("REST request to delete Conversacion : {}", id);
        conversacionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/conversacions/usuario")
    public ResponseEntity<List<ConversacionDTO>> getConversacionsByUser(
        @RequestParam(value = "id") Long id,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        Page<ConversacionDTO> page = conversacionService.findByUsuarios_id(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
