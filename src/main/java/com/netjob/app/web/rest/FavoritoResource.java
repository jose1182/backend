package com.netjob.app.web.rest;

import com.netjob.app.repository.FavoritoRepository;
import com.netjob.app.service.FavoritoQueryService;
import com.netjob.app.service.FavoritoService;
import com.netjob.app.service.criteria.FavoritoCriteria;
import com.netjob.app.service.dto.FavoritoDTO;
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
 * REST controller for managing {@link com.netjob.app.domain.Favorito}.
 */
@RestController
@RequestMapping("/api")
public class FavoritoResource {

    private final Logger log = LoggerFactory.getLogger(FavoritoResource.class);

    private static final String ENTITY_NAME = "favorito";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FavoritoService favoritoService;

    private final FavoritoRepository favoritoRepository;

    private final FavoritoQueryService favoritoQueryService;

    public FavoritoResource(
        FavoritoService favoritoService,
        FavoritoRepository favoritoRepository,
        FavoritoQueryService favoritoQueryService
    ) {
        this.favoritoService = favoritoService;
        this.favoritoRepository = favoritoRepository;
        this.favoritoQueryService = favoritoQueryService;
    }

    /**
     * {@code POST  /favoritos} : Create a new favorito.
     *
     * @param favoritoDTO the favoritoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new favoritoDTO, or with status {@code 400 (Bad Request)} if the favorito has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/favoritos")
    public ResponseEntity<FavoritoDTO> createFavorito(@RequestBody FavoritoDTO favoritoDTO) throws URISyntaxException {
        log.debug("REST request to save Favorito : {}", favoritoDTO);
        if (favoritoDTO.getId() != null) {
            throw new BadRequestAlertException("A new favorito cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FavoritoDTO result = favoritoService.save(favoritoDTO);
        return ResponseEntity
            .created(new URI("/api/favoritos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /favoritos/:id} : Updates an existing favorito.
     *
     * @param id the id of the favoritoDTO to save.
     * @param favoritoDTO the favoritoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated favoritoDTO,
     * or with status {@code 400 (Bad Request)} if the favoritoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the favoritoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/favoritos/{id}")
    public ResponseEntity<FavoritoDTO> updateFavorito(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FavoritoDTO favoritoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Favorito : {}, {}", id, favoritoDTO);
        if (favoritoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, favoritoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!favoritoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FavoritoDTO result = favoritoService.save(favoritoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, favoritoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /favoritos/:id} : Partial updates given fields of an existing favorito, field will ignore if it is null
     *
     * @param id the id of the favoritoDTO to save.
     * @param favoritoDTO the favoritoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated favoritoDTO,
     * or with status {@code 400 (Bad Request)} if the favoritoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the favoritoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the favoritoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/favoritos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FavoritoDTO> partialUpdateFavorito(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FavoritoDTO favoritoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Favorito partially : {}, {}", id, favoritoDTO);
        if (favoritoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, favoritoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!favoritoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FavoritoDTO> result = favoritoService.partialUpdate(favoritoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, favoritoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /favoritos} : get all the favoritos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of favoritos in body.
     */
    @GetMapping("/favoritos")
    public ResponseEntity<List<FavoritoDTO>> getAllFavoritos(
        FavoritoCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Favoritos by criteria: {}", criteria);
        Page<FavoritoDTO> page = favoritoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /favoritos/count} : count all the favoritos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/favoritos/count")
    public ResponseEntity<Long> countFavoritos(FavoritoCriteria criteria) {
        log.debug("REST request to count Favoritos by criteria: {}", criteria);
        return ResponseEntity.ok().body(favoritoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /favoritos/:id} : get the "id" favorito.
     *
     * @param id the id of the favoritoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the favoritoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/favoritos/{id}")
    public ResponseEntity<FavoritoDTO> getFavorito(@PathVariable Long id) {
        log.debug("REST request to get Favorito : {}", id);
        Optional<FavoritoDTO> favoritoDTO = favoritoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(favoritoDTO);
    }

    /**
     * {@code DELETE  /favoritos/:id} : delete the "id" favorito.
     *
     * @param id the id of the favoritoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/favoritos/{id}")
    public ResponseEntity<Void> deleteFavorito(@PathVariable Long id) {
        log.debug("REST request to delete Favorito : {}", id);
        favoritoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /* Lista paginada de favoritos según el ID de usuario */
    @GetMapping("/favoritos/usuario")
    public ResponseEntity<List<FavoritoDTO>> getFavoritosByUser(
        @RequestParam(value = "id") Long id,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        Page<FavoritoDTO> page = favoritoService.findAllByUsuario_id(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
