package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.MvtStk;
import com.mycompany.myapp.repository.MvtStkRepository;
import com.mycompany.myapp.service.dto.MvtStkDTO;
import com.mycompany.myapp.service.mapper.MvtStkMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MvtStk}.
 */
@Service
@Transactional
public class MvtStkService {

    private final Logger log = LoggerFactory.getLogger(MvtStkService.class);

    private final MvtStkRepository mvtStkRepository;

    private final MvtStkMapper mvtStkMapper;

    public MvtStkService(MvtStkRepository mvtStkRepository, MvtStkMapper mvtStkMapper) {
        this.mvtStkRepository = mvtStkRepository;
        this.mvtStkMapper = mvtStkMapper;
    }

    /**
     * Save a mvtStk.
     *
     * @param mvtStkDTO the entity to save.
     * @return the persisted entity.
     */
    public MvtStkDTO save(MvtStkDTO mvtStkDTO) {
        log.debug("Request to save MvtStk : {}", mvtStkDTO);
        MvtStk mvtStk = mvtStkMapper.toEntity(mvtStkDTO);
        mvtStk = mvtStkRepository.save(mvtStk);
        return mvtStkMapper.toDto(mvtStk);
    }

    /**
     * Partially update a mvtStk.
     *
     * @param mvtStkDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MvtStkDTO> partialUpdate(MvtStkDTO mvtStkDTO) {
        log.debug("Request to partially update MvtStk : {}", mvtStkDTO);

        return mvtStkRepository
            .findById(mvtStkDTO.getId())
            .map(
                existingMvtStk -> {
                    mvtStkMapper.partialUpdate(existingMvtStk, mvtStkDTO);

                    return existingMvtStk;
                }
            )
            .map(mvtStkRepository::save)
            .map(mvtStkMapper::toDto);
    }

    /**
     * Get all the mvtStks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MvtStkDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MvtStks");
        return mvtStkRepository.findAll(pageable).map(mvtStkMapper::toDto);
    }

    /**
     * Get one mvtStk by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MvtStkDTO> findOne(Long id) {
        log.debug("Request to get MvtStk : {}", id);
        return mvtStkRepository.findById(id).map(mvtStkMapper::toDto);
    }

    /**
     * Delete the mvtStk by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MvtStk : {}", id);
        mvtStkRepository.deleteById(id);
    }
}
