package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Personnel;
import com.mycompany.myapp.repository.PersonnelRepository;
import com.mycompany.myapp.service.dto.PersonnelDTO;
import com.mycompany.myapp.service.mapper.PersonnelMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Personnel}.
 */
@Service
@Transactional
public class PersonnelService {

    private final Logger log = LoggerFactory.getLogger(PersonnelService.class);

    private final PersonnelRepository personnelRepository;

    private final PersonnelMapper personnelMapper;

    public PersonnelService(PersonnelRepository personnelRepository, PersonnelMapper personnelMapper) {
        this.personnelRepository = personnelRepository;
        this.personnelMapper = personnelMapper;
    }

    /**
     * Save a personnel.
     *
     * @param personnelDTO the entity to save.
     * @return the persisted entity.
     */
    public PersonnelDTO save(PersonnelDTO personnelDTO) {
        log.debug("Request to save Personnel : {}", personnelDTO);
        Personnel personnel = personnelMapper.toEntity(personnelDTO);
        personnel = personnelRepository.save(personnel);
        return personnelMapper.toDto(personnel);
    }

    /**
     * Partially update a personnel.
     *
     * @param personnelDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PersonnelDTO> partialUpdate(PersonnelDTO personnelDTO) {
        log.debug("Request to partially update Personnel : {}", personnelDTO);

        return personnelRepository
            .findById(personnelDTO.getId())
            .map(
                existingPersonnel -> {
                    personnelMapper.partialUpdate(existingPersonnel, personnelDTO);

                    return existingPersonnel;
                }
            )
            .map(personnelRepository::save)
            .map(personnelMapper::toDto);
    }

    /**
     * Get all the personnel.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonnelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Personnel");
        return personnelRepository.findAll(pageable).map(personnelMapper::toDto);
    }

    /**
     * Get one personnel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PersonnelDTO> findOne(Long id) {
        log.debug("Request to get Personnel : {}", id);
        return personnelRepository.findById(id).map(personnelMapper::toDto);
    }

    /**
     * Delete the personnel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Personnel : {}", id);
        personnelRepository.deleteById(id);
    }
}
