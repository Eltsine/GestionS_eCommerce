package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.LigneCommandeFournisseurRepository;
import com.mycompany.myapp.service.LigneCommandeFournisseurService;
import com.mycompany.myapp.service.dto.LigneCommandeFournisseurDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.LigneCommandeFournisseur}.
 */
@RestController
@RequestMapping("/api")
public class LigneCommandeFournisseurResource {

    private final Logger log = LoggerFactory.getLogger(LigneCommandeFournisseurResource.class);

    private static final String ENTITY_NAME = "ligneCommandeFournisseur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LigneCommandeFournisseurService ligneCommandeFournisseurService;

    private final LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository;

    public LigneCommandeFournisseurResource(
        LigneCommandeFournisseurService ligneCommandeFournisseurService,
        LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository
    ) {
        this.ligneCommandeFournisseurService = ligneCommandeFournisseurService;
        this.ligneCommandeFournisseurRepository = ligneCommandeFournisseurRepository;
    }

    /**
     * {@code POST  /ligne-commande-fournisseurs} : Create a new ligneCommandeFournisseur.
     *
     * @param ligneCommandeFournisseurDTO the ligneCommandeFournisseurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ligneCommandeFournisseurDTO, or with status {@code 400 (Bad Request)} if the ligneCommandeFournisseur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ligne-commande-fournisseurs")
    public ResponseEntity<LigneCommandeFournisseurDTO> createLigneCommandeFournisseur(
        @RequestBody LigneCommandeFournisseurDTO ligneCommandeFournisseurDTO
    ) throws URISyntaxException {
        log.debug("REST request to save LigneCommandeFournisseur : {}", ligneCommandeFournisseurDTO);
        if (ligneCommandeFournisseurDTO.getId() != null) {
            throw new BadRequestAlertException("A new ligneCommandeFournisseur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LigneCommandeFournisseurDTO result = ligneCommandeFournisseurService.save(ligneCommandeFournisseurDTO);
        return ResponseEntity
            .created(new URI("/api/ligne-commande-fournisseurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ligne-commande-fournisseurs/:id} : Updates an existing ligneCommandeFournisseur.
     *
     * @param id the id of the ligneCommandeFournisseurDTO to save.
     * @param ligneCommandeFournisseurDTO the ligneCommandeFournisseurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ligneCommandeFournisseurDTO,
     * or with status {@code 400 (Bad Request)} if the ligneCommandeFournisseurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ligneCommandeFournisseurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ligne-commande-fournisseurs/{id}")
    public ResponseEntity<LigneCommandeFournisseurDTO> updateLigneCommandeFournisseur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LigneCommandeFournisseurDTO ligneCommandeFournisseurDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LigneCommandeFournisseur : {}, {}", id, ligneCommandeFournisseurDTO);
        if (ligneCommandeFournisseurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ligneCommandeFournisseurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ligneCommandeFournisseurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LigneCommandeFournisseurDTO result = ligneCommandeFournisseurService.save(ligneCommandeFournisseurDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ligneCommandeFournisseurDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ligne-commande-fournisseurs/:id} : Partial updates given fields of an existing ligneCommandeFournisseur, field will ignore if it is null
     *
     * @param id the id of the ligneCommandeFournisseurDTO to save.
     * @param ligneCommandeFournisseurDTO the ligneCommandeFournisseurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ligneCommandeFournisseurDTO,
     * or with status {@code 400 (Bad Request)} if the ligneCommandeFournisseurDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ligneCommandeFournisseurDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ligneCommandeFournisseurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ligne-commande-fournisseurs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LigneCommandeFournisseurDTO> partialUpdateLigneCommandeFournisseur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LigneCommandeFournisseurDTO ligneCommandeFournisseurDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LigneCommandeFournisseur partially : {}, {}", id, ligneCommandeFournisseurDTO);
        if (ligneCommandeFournisseurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ligneCommandeFournisseurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ligneCommandeFournisseurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LigneCommandeFournisseurDTO> result = ligneCommandeFournisseurService.partialUpdate(ligneCommandeFournisseurDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ligneCommandeFournisseurDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ligne-commande-fournisseurs} : get all the ligneCommandeFournisseurs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ligneCommandeFournisseurs in body.
     */
    @GetMapping("/ligne-commande-fournisseurs")
    public ResponseEntity<List<LigneCommandeFournisseurDTO>> getAllLigneCommandeFournisseurs(Pageable pageable) {
        log.debug("REST request to get a page of LigneCommandeFournisseurs");
        Page<LigneCommandeFournisseurDTO> page = ligneCommandeFournisseurService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ligne-commande-fournisseurs/:id} : get the "id" ligneCommandeFournisseur.
     *
     * @param id the id of the ligneCommandeFournisseurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ligneCommandeFournisseurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ligne-commande-fournisseurs/{id}")
    public ResponseEntity<LigneCommandeFournisseurDTO> getLigneCommandeFournisseur(@PathVariable Long id) {
        log.debug("REST request to get LigneCommandeFournisseur : {}", id);
        Optional<LigneCommandeFournisseurDTO> ligneCommandeFournisseurDTO = ligneCommandeFournisseurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ligneCommandeFournisseurDTO);
    }

    /**
     * {@code DELETE  /ligne-commande-fournisseurs/:id} : delete the "id" ligneCommandeFournisseur.
     *
     * @param id the id of the ligneCommandeFournisseurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ligne-commande-fournisseurs/{id}")
    public ResponseEntity<Void> deleteLigneCommandeFournisseur(@PathVariable Long id) {
        log.debug("REST request to delete LigneCommandeFournisseur : {}", id);
        ligneCommandeFournisseurService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
