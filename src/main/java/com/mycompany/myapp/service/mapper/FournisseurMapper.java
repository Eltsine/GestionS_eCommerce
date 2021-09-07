package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.FournisseurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Fournisseur} and its DTO {@link FournisseurDTO}.
 */
@Mapper(componentModel = "spring", uses = { AdresseMapper.class })
public interface FournisseurMapper extends EntityMapper<FournisseurDTO, Fournisseur> {
    @Mapping(target = "adresse", source = "adresse", qualifiedByName = "id")
    FournisseurDTO toDto(Fournisseur s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FournisseurDTO toDtoId(Fournisseur fournisseur);
}
