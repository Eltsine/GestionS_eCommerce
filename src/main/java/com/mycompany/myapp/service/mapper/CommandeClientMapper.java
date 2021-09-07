package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.CommandeClientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CommandeClient} and its DTO {@link CommandeClientDTO}.
 */
@Mapper(componentModel = "spring", uses = { ClientMapper.class })
public interface CommandeClientMapper extends EntityMapper<CommandeClientDTO, CommandeClient> {
    @Mapping(target = "client", source = "client", qualifiedByName = "id")
    CommandeClientDTO toDto(CommandeClient s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CommandeClientDTO toDtoId(CommandeClient commandeClient);
}
