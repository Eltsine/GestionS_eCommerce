package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.MvtStkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MvtStk} and its DTO {@link MvtStkDTO}.
 */
@Mapper(componentModel = "spring", uses = { ArticleMapper.class })
public interface MvtStkMapper extends EntityMapper<MvtStkDTO, MvtStk> {
    @Mapping(target = "entreprise", source = "entreprise", qualifiedByName = "id")
    MvtStkDTO toDto(MvtStk s);
}
