package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.LigneVenteRepository;
import com.mycompany.myapp.service.LigneVenteService;
import com.mycompany.myapp.service.dto.LigneVenteDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.LigneVente}.
 */
@RestController
@RequestMapping("/api")
public class LigneVenteResource {

    private final Logger log = LoggerFactory.getLogger(LigneVenteResource.class);

    private static final String ENTITY_NAME = "ligneVente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LigneVenteService ligneVenteService;

    private final LigneVenteRepository ligneVenteRepository;

    public LigneVenteResource(LigneVenteService ligneVenteService, LigneVenteRepository ligneVenteRepository) {
        this.ligneVenteService = ligneVenteService;
        this.ligneVenteRepository = ligneVenteRepository;
    }

    /**
     * {@code POST  /ligne-ventes} : Create a new ligneVente.
     *
     * @param ligneVenteDTO the ligneVenteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ligneVenteDTO, or with status {@code 400 (Bad Request)} if the ligneVente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ligne-ventes")
    public ResponseEntity<LigneVenteDTO> createLigneVente(@RequestBody LigneVenteDTO ligneVenteDTO) throws URISyntaxException {
        log.debug("REST request to save LigneVente : {}", ligneVenteDTO);
        if (ligneVenteDTO.getId() != null) {
            throw new BadRequestAlertException("A new ligneVente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LigneVenteDTO result = ligneVenteService.save(ligneVenteDTO);
        return ResponseEntity
            .created(new URI("/api/ligne-ventes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ligne-ventes/:id} : Updates an existing ligneVente.
     *
     * @param id the id of the ligneVenteDTO to save.
     * @param ligneVenteDTO the ligneVenteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ligneVenteDTO,
     * or with status {@code 400 (Bad Request)} if the ligneVenteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ligneVenteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ligne-ventes/{id}")
    public ResponseEntity<LigneVenteDTO> updateLigneVente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LigneVenteDTO ligneVenteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LigneVente : {}, {}", id, ligneVenteDTO);
        if (ligneVenteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ligneVenteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ligneVenteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LigneVenteDTO result = ligneVenteService.save(ligneVenteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ligneVenteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ligne-ventes/:id} : Partial updates given fields of an existing ligneVente, field will ignore if it is null
     *
     * @param id the id of the ligneVenteDTO to save.
     * @param ligneVenteDTO the ligneVenteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ligneVenteDTO,
     * or with status {@code 400 (Bad Request)} if the ligneVenteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ligneVenteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ligneVenteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ligne-ventes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LigneVenteDTO> partialUpdateLigneVente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LigneVenteDTO ligneVenteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LigneVente partially : {}, {}", id, ligneVenteDTO);
        if (ligneVenteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ligneVenteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ligneVenteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LigneVenteDTO> result = ligneVenteService.partialUpdate(ligneVenteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ligneVenteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ligne-ventes} : get all the ligneVentes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ligneVentes in body.
     */
    @GetMapping("/ligne-ventes")
    public ResponseEntity<List<LigneVenteDTO>> getAllLigneVentes(Pageable pageable) {
        log.debug("REST request to get a page of LigneVentes");
        Page<LigneVenteDTO> page = ligneVenteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ligne-ventes/:id} : get the "id" ligneVente.
     *
     * @param id the id of the ligneVenteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ligneVenteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ligne-ventes/{id}")
    public ResponseEntity<LigneVenteDTO> getLigneVente(@PathVariable Long id) {
        log.debug("REST request to get LigneVente : {}", id);
        Optional<LigneVenteDTO> ligneVenteDTO = ligneVenteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ligneVenteDTO);
    }

    /**
     * {@code DELETE  /ligne-ventes/:id} : delete the "id" ligneVente.
     *
     * @param id the id of the ligneVenteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ligne-ventes/{id}")
    public ResponseEntity<Void> deleteLigneVente(@PathVariable Long id) {
        log.debug("REST request to delete LigneVente : {}", id);
        ligneVenteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
