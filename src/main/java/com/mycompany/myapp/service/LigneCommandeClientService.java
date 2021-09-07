package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.LigneCommandeClient;
import com.mycompany.myapp.repository.LigneCommandeClientRepository;
import com.mycompany.myapp.service.dto.LigneCommandeClientDTO;
import com.mycompany.myapp.service.mapper.LigneCommandeClientMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LigneCommandeClient}.
 */
@Service
@Transactional
public class LigneCommandeClientService {

    private final Logger log = LoggerFactory.getLogger(LigneCommandeClientService.class);

    private final LigneCommandeClientRepository ligneCommandeClientRepository;

    private final LigneCommandeClientMapper ligneCommandeClientMapper;

    public LigneCommandeClientService(
        LigneCommandeClientRepository ligneCommandeClientRepository,
        LigneCommandeClientMapper ligneCommandeClientMapper
    ) {
        this.ligneCommandeClientRepository = ligneCommandeClientRepository;
        this.ligneCommandeClientMapper = ligneCommandeClientMapper;
    }

    /**
     * Save a ligneCommandeClient.
     *
     * @param ligneCommandeClientDTO the entity to save.
     * @return the persisted entity.
     */
    public LigneCommandeClientDTO save(LigneCommandeClientDTO ligneCommandeClientDTO) {
        log.debug("Request to save LigneCommandeClient : {}", ligneCommandeClientDTO);
        LigneCommandeClient ligneCommandeClient = ligneCommandeClientMapper.toEntity(ligneCommandeClientDTO);
        ligneCommandeClient = ligneCommandeClientRepository.save(ligneCommandeClient);
        return ligneCommandeClientMapper.toDto(ligneCommandeClient);
    }

    /**
     * Partially update a ligneCommandeClient.
     *
     * @param ligneCommandeClientDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LigneCommandeClientDTO> partialUpdate(LigneCommandeClientDTO ligneCommandeClientDTO) {
        log.debug("Request to partially update LigneCommandeClient : {}", ligneCommandeClientDTO);

        return ligneCommandeClientRepository
            .findById(ligneCommandeClientDTO.getId())
            .map(
                existingLigneCommandeClient -> {
                    ligneCommandeClientMapper.partialUpdate(existingLigneCommandeClient, ligneCommandeClientDTO);

                    return existingLigneCommandeClient;
                }
            )
            .map(ligneCommandeClientRepository::save)
            .map(ligneCommandeClientMapper::toDto);
    }

    /**
     * Get all the ligneCommandeClients.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LigneCommandeClientDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LigneCommandeClients");
        return ligneCommandeClientRepository.findAll(pageable).map(ligneCommandeClientMapper::toDto);
    }

    /**
     * Get one ligneCommandeClient by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LigneCommandeClientDTO> findOne(Long id) {
        log.debug("Request to get LigneCommandeClient : {}", id);
        return ligneCommandeClientRepository.findById(id).map(ligneCommandeClientMapper::toDto);
    }

    /**
     * Delete the ligneCommandeClient by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LigneCommandeClient : {}", id);
        ligneCommandeClientRepository.deleteById(id);
    }
}
