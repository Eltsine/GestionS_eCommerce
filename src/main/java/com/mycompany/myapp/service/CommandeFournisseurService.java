package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.CommandeFournisseur;
import com.mycompany.myapp.repository.CommandeFournisseurRepository;
import com.mycompany.myapp.service.dto.CommandeFournisseurDTO;
import com.mycompany.myapp.service.mapper.CommandeFournisseurMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CommandeFournisseur}.
 */
@Service
@Transactional
public class CommandeFournisseurService {

    private final Logger log = LoggerFactory.getLogger(CommandeFournisseurService.class);

    private final CommandeFournisseurRepository commandeFournisseurRepository;

    private final CommandeFournisseurMapper commandeFournisseurMapper;

    public CommandeFournisseurService(
        CommandeFournisseurRepository commandeFournisseurRepository,
        CommandeFournisseurMapper commandeFournisseurMapper
    ) {
        this.commandeFournisseurRepository = commandeFournisseurRepository;
        this.commandeFournisseurMapper = commandeFournisseurMapper;
    }

    /**
     * Save a commandeFournisseur.
     *
     * @param commandeFournisseurDTO the entity to save.
     * @return the persisted entity.
     */
    public CommandeFournisseurDTO save(CommandeFournisseurDTO commandeFournisseurDTO) {
        log.debug("Request to save CommandeFournisseur : {}", commandeFournisseurDTO);
        CommandeFournisseur commandeFournisseur = commandeFournisseurMapper.toEntity(commandeFournisseurDTO);
        commandeFournisseur = commandeFournisseurRepository.save(commandeFournisseur);
        return commandeFournisseurMapper.toDto(commandeFournisseur);
    }

    /**
     * Partially update a commandeFournisseur.
     *
     * @param commandeFournisseurDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CommandeFournisseurDTO> partialUpdate(CommandeFournisseurDTO commandeFournisseurDTO) {
        log.debug("Request to partially update CommandeFournisseur : {}", commandeFournisseurDTO);

        return commandeFournisseurRepository
            .findById(commandeFournisseurDTO.getId())
            .map(
                existingCommandeFournisseur -> {
                    commandeFournisseurMapper.partialUpdate(existingCommandeFournisseur, commandeFournisseurDTO);

                    return existingCommandeFournisseur;
                }
            )
            .map(commandeFournisseurRepository::save)
            .map(commandeFournisseurMapper::toDto);
    }

    /**
     * Get all the commandeFournisseurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CommandeFournisseurDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CommandeFournisseurs");
        return commandeFournisseurRepository.findAll(pageable).map(commandeFournisseurMapper::toDto);
    }

    /**
     * Get one commandeFournisseur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommandeFournisseurDTO> findOne(Long id) {
        log.debug("Request to get CommandeFournisseur : {}", id);
        return commandeFournisseurRepository.findById(id).map(commandeFournisseurMapper::toDto);
    }

    /**
     * Delete the commandeFournisseur by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CommandeFournisseur : {}", id);
        commandeFournisseurRepository.deleteById(id);
    }
}
