package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.LigneVente;
import com.mycompany.myapp.repository.LigneVenteRepository;
import com.mycompany.myapp.service.dto.LigneVenteDTO;
import com.mycompany.myapp.service.mapper.LigneVenteMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LigneVente}.
 */
@Service
@Transactional
public class LigneVenteService {

    private final Logger log = LoggerFactory.getLogger(LigneVenteService.class);

    private final LigneVenteRepository ligneVenteRepository;

    private final LigneVenteMapper ligneVenteMapper;

    public LigneVenteService(LigneVenteRepository ligneVenteRepository, LigneVenteMapper ligneVenteMapper) {
        this.ligneVenteRepository = ligneVenteRepository;
        this.ligneVenteMapper = ligneVenteMapper;
    }

    /**
     * Save a ligneVente.
     *
     * @param ligneVenteDTO the entity to save.
     * @return the persisted entity.
     */
    public LigneVenteDTO save(LigneVenteDTO ligneVenteDTO) {
        log.debug("Request to save LigneVente : {}", ligneVenteDTO);
        LigneVente ligneVente = ligneVenteMapper.toEntity(ligneVenteDTO);
        ligneVente = ligneVenteRepository.save(ligneVente);
        return ligneVenteMapper.toDto(ligneVente);
    }

    /**
     * Partially update a ligneVente.
     *
     * @param ligneVenteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LigneVenteDTO> partialUpdate(LigneVenteDTO ligneVenteDTO) {
        log.debug("Request to partially update LigneVente : {}", ligneVenteDTO);

        return ligneVenteRepository
            .findById(ligneVenteDTO.getId())
            .map(
                existingLigneVente -> {
                    ligneVenteMapper.partialUpdate(existingLigneVente, ligneVenteDTO);

                    return existingLigneVente;
                }
            )
            .map(ligneVenteRepository::save)
            .map(ligneVenteMapper::toDto);
    }

    /**
     * Get all the ligneVentes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LigneVenteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LigneVentes");
        return ligneVenteRepository.findAll(pageable).map(ligneVenteMapper::toDto);
    }

    /**
     * Get one ligneVente by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LigneVenteDTO> findOne(Long id) {
        log.debug("Request to get LigneVente : {}", id);
        return ligneVenteRepository.findById(id).map(ligneVenteMapper::toDto);
    }

    /**
     * Delete the ligneVente by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LigneVente : {}", id);
        ligneVenteRepository.deleteById(id);
    }
}
