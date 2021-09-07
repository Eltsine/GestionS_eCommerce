package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.LigneCommandeClientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LigneCommandeClient} and its DTO {@link LigneCommandeClientDTO}.
 */
@Mapper(componentModel = "spring", uses = { CommandeClientMapper.class })
public interface LigneCommandeClientMapper extends EntityMapper<LigneCommandeClientDTO, LigneCommandeClient> {
    @Mapping(target = "commandeClient", source = "commandeClient", qualifiedByName = "id")
    LigneCommandeClientDTO toDto(LigneCommandeClient s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LigneCommandeClientDTO toDtoId(LigneCommandeClient ligneCommandeClient);
}
