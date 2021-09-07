package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.LigneVenteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LigneVente} and its DTO {@link LigneVenteDTO}.
 */
@Mapper(componentModel = "spring", uses = { VenteMapper.class })
public interface LigneVenteMapper extends EntityMapper<LigneVenteDTO, LigneVente> {
    @Mapping(target = "vente", source = "vente", qualifiedByName = "id")
    LigneVenteDTO toDto(LigneVente s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LigneVenteDTO toDtoId(LigneVente ligneVente);
}
