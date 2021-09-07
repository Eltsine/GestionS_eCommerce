package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.LigneCommandeFournisseurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LigneCommandeFournisseur} and its DTO {@link LigneCommandeFournisseurDTO}.
 */
@Mapper(componentModel = "spring", uses = { CommandeFournisseurMapper.class })
public interface LigneCommandeFournisseurMapper extends EntityMapper<LigneCommandeFournisseurDTO, LigneCommandeFournisseur> {
    @Mapping(target = "commandeFournisseur", source = "commandeFournisseur", qualifiedByName = "id")
    LigneCommandeFournisseurDTO toDto(LigneCommandeFournisseur s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LigneCommandeFournisseurDTO toDtoId(LigneCommandeFournisseur ligneCommandeFournisseur);
}
