package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.LigneCommandeClientRepository;
import com.mycompany.myapp.service.LigneCommandeClientService;
import com.mycompany.myapp.service.dto.LigneCommandeClientDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.LigneCommandeClient}.
 */
@RestController
@RequestMapping("/api")
public class LigneCommandeClientResource {

    private final Logger log = LoggerFactory.getLogger(LigneCommandeClientResource.class);

    private static final String ENTITY_NAME = "ligneCommandeClient";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LigneCommandeClientService ligneCommandeClientService;

    private final LigneCommandeClientRepository ligneCommandeClientRepository;

    public LigneCommandeClientResource(
        LigneCommandeClientService ligneCommandeClientService,
        LigneCommandeClientRepository ligneCommandeClientRepository
    ) {
        this.ligneCommandeClientService = ligneCommandeClientService;
        this.ligneCommandeClientRepository = ligneCommandeClientRepository;
    }

    /**
     * {@code POST  /ligne-commande-clients} : Create a new ligneCommandeClient.
     *
     * @param ligneCommandeClientDTO the ligneCommandeClientDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ligneCommandeClientDTO, or with status {@code 400 (Bad Request)} if the ligneCommandeClient has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ligne-commande-clients")
    public ResponseEntity<LigneCommandeClientDTO> createLigneCommandeClient(@RequestBody LigneCommandeClientDTO ligneCommandeClientDTO)
        throws URISyntaxException {
        log.debug("REST request to save LigneCommandeClient : {}", ligneCommandeClientDTO);
        if (ligneCommandeClientDTO.getId() != null) {
            throw new BadRequestAlertException("A new ligneCommandeClient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LigneCommandeClientDTO result = ligneCommandeClientService.save(ligneCommandeClientDTO);
        return ResponseEntity
            .created(new URI("/api/ligne-commande-clients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ligne-commande-clients/:id} : Updates an existing ligneCommandeClient.
     *
     * @param id the id of the ligneCommandeClientDTO to save.
     * @param ligneCommandeClientDTO the ligneCommandeClientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ligneCommandeClientDTO,
     * or with status {@code 400 (Bad Request)} if the ligneCommandeClientDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ligneCommandeClientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ligne-commande-clients/{id}")
    public ResponseEntity<LigneCommandeClientDTO> updateLigneCommandeClient(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LigneCommandeClientDTO ligneCommandeClientDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LigneCommandeClient : {}, {}", id, ligneCommandeClientDTO);
        if (ligneCommandeClientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ligneCommandeClientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ligneCommandeClientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LigneCommandeClientDTO result = ligneCommandeClientService.save(ligneCommandeClientDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ligneCommandeClientDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ligne-commande-clients/:id} : Partial updates given fields of an existing ligneCommandeClient, field will ignore if it is null
     *
     * @param id the id of the ligneCommandeClientDTO to save.
     * @param ligneCommandeClientDTO the ligneCommandeClientDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ligneCommandeClientDTO,
     * or with status {@code 400 (Bad Request)} if the ligneCommandeClientDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ligneCommandeClientDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ligneCommandeClientDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ligne-commande-clients/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LigneCommandeClientDTO> partialUpdateLigneCommandeClient(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LigneCommandeClientDTO ligneCommandeClientDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LigneCommandeClient partially : {}, {}", id, ligneCommandeClientDTO);
        if (ligneCommandeClientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ligneCommandeClientDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ligneCommandeClientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LigneCommandeClientDTO> result = ligneCommandeClientService.partialUpdate(ligneCommandeClientDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ligneCommandeClientDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ligne-commande-clients} : get all the ligneCommandeClients.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ligneCommandeClients in body.
     */
    @GetMapping("/ligne-commande-clients")
    public ResponseEntity<List<LigneCommandeClientDTO>> getAllLigneCommandeClients(Pageable pageable) {
        log.debug("REST request to get a page of LigneCommandeClients");
        Page<LigneCommandeClientDTO> page = ligneCommandeClientService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ligne-commande-clients/:id} : get the "id" ligneCommandeClient.
     *
     * @param id the id of the ligneCommandeClientDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ligneCommandeClientDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ligne-commande-clients/{id}")
    public ResponseEntity<LigneCommandeClientDTO> getLigneCommandeClient(@PathVariable Long id) {
        log.debug("REST request to get LigneCommandeClient : {}", id);
        Optional<LigneCommandeClientDTO> ligneCommandeClientDTO = ligneCommandeClientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ligneCommandeClientDTO);
    }

    /**
     * {@code DELETE  /ligne-commande-clients/:id} : delete the "id" ligneCommandeClient.
     *
     * @param id the id of the ligneCommandeClientDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ligne-commande-clients/{id}")
    public ResponseEntity<Void> deleteLigneCommandeClient(@PathVariable Long id) {
        log.debug("REST request to delete LigneCommandeClient : {}", id);
        ligneCommandeClientService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
