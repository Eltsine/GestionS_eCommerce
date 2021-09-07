package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.PersonnelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Personnel} and its DTO {@link PersonnelDTO}.
 */
@Mapper(componentModel = "spring", uses = { AdresseMapper.class, EntrepriseMapper.class })
public interface PersonnelMapper extends EntityMapper<PersonnelDTO, Personnel> {
    @Mapping(target = "adresse", source = "adresse", qualifiedByName = "id")
    @Mapping(target = "entreprise", source = "entreprise", qualifiedByName = "id")
    PersonnelDTO toDto(Personnel s);
}
