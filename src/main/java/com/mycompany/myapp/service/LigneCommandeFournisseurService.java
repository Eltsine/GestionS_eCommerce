package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.LigneCommandeFournisseur;
import com.mycompany.myapp.repository.LigneCommandeFournisseurRepository;
import com.mycompany.myapp.service.dto.LigneCommandeFournisseurDTO;
import com.mycompany.myapp.service.mapper.LigneCommandeFournisseurMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LigneCommandeFournisseur}.
 */
@Service
@Transactional
public class LigneCommandeFournisseurService {

    private final Logger log = LoggerFactory.getLogger(LigneCommandeFournisseurService.class);

    private final LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository;

    private final LigneCommandeFournisseurMapper ligneCommandeFournisseurMapper;

    public LigneCommandeFournisseurService(
        LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository,
        LigneCommandeFournisseurMapper ligneCommandeFournisseurMapper
    ) {
        this.ligneCommandeFournisseurRepository = ligneCommandeFournisseurRepository;
        this.ligneCommandeFournisseurMapper = ligneCommandeFournisseurMapper;
    }

    /**
     * Save a ligneCommandeFournisseur.
     *
     * @param ligneCommandeFournisseurDTO the entity to save.
     * @return the persisted entity.
     */
    public LigneCommandeFournisseurDTO save(LigneCommandeFournisseurDTO ligneCommandeFournisseurDTO) {
        log.debug("Request to save LigneCommandeFournisseur : {}", ligneCommandeFournisseurDTO);
        LigneCommandeFournisseur ligneCommandeFournisseur = ligneCommandeFournisseurMapper.toEntity(ligneCommandeFournisseurDTO);
        ligneCommandeFournisseur = ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);
        return ligneCommandeFournisseurMapper.toDto(ligneCommandeFournisseur);
    }

    /**
     * Partially update a ligneCommandeFournisseur.
     *
     * @param ligneCommandeFournisseurDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LigneCommandeFournisseurDTO> partialUpdate(LigneCommandeFournisseurDTO ligneCommandeFournisseurDTO) {
        log.debug("Request to partially update LigneCommandeFournisseur : {}", ligneCommandeFournisseurDTO);

        return ligneCommandeFournisseurRepository
            .findById(ligneCommandeFournisseurDTO.getId())
            .map(
                existingLigneCommandeFournisseur -> {
                    ligneCommandeFournisseurMapper.partialUpdate(existingLigneCommandeFournisseur, ligneCommandeFournisseurDTO);

                    return existingLigneCommandeFournisseur;
                }
            )
            .map(ligneCommandeFournisseurRepository::save)
            .map(ligneCommandeFournisseurMapper::toDto);
    }

    /**
     * Get all the ligneCommandeFournisseurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LigneCommandeFournisseurDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LigneCommandeFournisseurs");
        return ligneCommandeFournisseurRepository.findAll(pageable).map(ligneCommandeFournisseurMapper::toDto);
    }

    /**
     * Get one ligneCommandeFournisseur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LigneCommandeFournisseurDTO> findOne(Long id) {
        log.debug("Request to get LigneCommandeFournisseur : {}", id);
        return ligneCommandeFournisseurRepository.findById(id).map(ligneCommandeFournisseurMapper::toDto);
    }

    /**
     * Delete the ligneCommandeFournisseur by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LigneCommandeFournisseur : {}", id);
        ligneCommandeFournisseurRepository.deleteById(id);
    }
}
