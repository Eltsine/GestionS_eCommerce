package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.MvtStkRepository;
import com.mycompany.myapp.service.MvtStkService;
import com.mycompany.myapp.service.dto.MvtStkDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.MvtStk}.
 */
@RestController
@RequestMapping("/api")
public class MvtStkResource {

    private final Logger log = LoggerFactory.getLogger(MvtStkResource.class);

    private static final String ENTITY_NAME = "mvtStk";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MvtStkService mvtStkService;

    private final MvtStkRepository mvtStkRepository;

    public MvtStkResource(MvtStkService mvtStkService, MvtStkRepository mvtStkRepository) {
        this.mvtStkService = mvtStkService;
        this.mvtStkRepository = mvtStkRepository;
    }

    /**
     * {@code POST  /mvt-stks} : Create a new mvtStk.
     *
     * @param mvtStkDTO the mvtStkDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mvtStkDTO, or with status {@code 400 (Bad Request)} if the mvtStk has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mvt-stks")
    public ResponseEntity<MvtStkDTO> createMvtStk(@RequestBody MvtStkDTO mvtStkDTO) throws URISyntaxException {
        log.debug("REST request to save MvtStk : {}", mvtStkDTO);
        if (mvtStkDTO.getId() != null) {
            throw new BadRequestAlertException("A new mvtStk cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MvtStkDTO result = mvtStkService.save(mvtStkDTO);
        return ResponseEntity
            .created(new URI("/api/mvt-stks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mvt-stks/:id} : Updates an existing mvtStk.
     *
     * @param id the id of the mvtStkDTO to save.
     * @param mvtStkDTO the mvtStkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mvtStkDTO,
     * or with status {@code 400 (Bad Request)} if the mvtStkDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mvtStkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mvt-stks/{id}")
    public ResponseEntity<MvtStkDTO> updateMvtStk(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MvtStkDTO mvtStkDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MvtStk : {}, {}", id, mvtStkDTO);
        if (mvtStkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mvtStkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mvtStkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MvtStkDTO result = mvtStkService.save(mvtStkDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mvtStkDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mvt-stks/:id} : Partial updates given fields of an existing mvtStk, field will ignore if it is null
     *
     * @param id the id of the mvtStkDTO to save.
     * @param mvtStkDTO the mvtStkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mvtStkDTO,
     * or with status {@code 400 (Bad Request)} if the mvtStkDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mvtStkDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mvtStkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mvt-stks/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MvtStkDTO> partialUpdateMvtStk(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MvtStkDTO mvtStkDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MvtStk partially : {}, {}", id, mvtStkDTO);
        if (mvtStkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mvtStkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mvtStkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MvtStkDTO> result = mvtStkService.partialUpdate(mvtStkDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mvtStkDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /mvt-stks} : get all the mvtStks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mvtStks in body.
     */
    @GetMapping("/mvt-stks")
    public ResponseEntity<List<MvtStkDTO>> getAllMvtStks(Pageable pageable) {
        log.debug("REST request to get a page of MvtStks");
        Page<MvtStkDTO> page = mvtStkService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mvt-stks/:id} : get the "id" mvtStk.
     *
     * @param id the id of the mvtStkDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mvtStkDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mvt-stks/{id}")
    public ResponseEntity<MvtStkDTO> getMvtStk(@PathVariable Long id) {
        log.debug("REST request to get MvtStk : {}", id);
        Optional<MvtStkDTO> mvtStkDTO = mvtStkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mvtStkDTO);
    }

    /**
     * {@code DELETE  /mvt-stks/:id} : delete the "id" mvtStk.
     *
     * @param id the id of the mvtStkDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mvt-stks/{id}")
    public ResponseEntity<Void> deleteMvtStk(@PathVariable Long id) {
        log.debug("REST request to delete MvtStk : {}", id);
        mvtStkService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
