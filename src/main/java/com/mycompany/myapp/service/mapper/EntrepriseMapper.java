package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.EntrepriseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Entreprise} and its DTO {@link EntrepriseDTO}.
 */
@Mapper(componentModel = "spring", uses = { AdresseMapper.class })
public interface EntrepriseMapper extends EntityMapper<EntrepriseDTO, Entreprise> {
    @Mapping(target = "adresse", source = "adresse", qualifiedByName = "id")
    EntrepriseDTO toDto(Entreprise s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EntrepriseDTO toDtoId(Entreprise entreprise);
}
