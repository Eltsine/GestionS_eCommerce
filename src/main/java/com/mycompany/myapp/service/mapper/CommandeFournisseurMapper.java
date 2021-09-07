package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.CommandeFournisseurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CommandeFournisseur} and its DTO {@link CommandeFournisseurDTO}.
 */
@Mapper(componentModel = "spring", uses = { FournisseurMapper.class })
public interface CommandeFournisseurMapper extends EntityMapper<CommandeFournisseurDTO, CommandeFournisseur> {
    @Mapping(target = "fournisseur", source = "fournisseur", qualifiedByName = "id")
    CommandeFournisseurDTO toDto(CommandeFournisseur s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CommandeFournisseurDTO toDtoId(CommandeFournisseur commandeFournisseur);
}
