package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.ArticleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Article} and its DTO {@link ArticleDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        EntrepriseMapper.class,
        LigneCommandeClientMapper.class,
        LigneCommandeFournisseurMapper.class,
        LigneVenteMapper.class,
        CategorieMapper.class,
    }
)
public interface ArticleMapper extends EntityMapper<ArticleDTO, Article> {
    @Mapping(target = "entreprise", source = "entreprise", qualifiedByName = "id")
    @Mapping(target = "ligneCommandeClient", source = "ligneCommandeClient", qualifiedByName = "id")
    @Mapping(target = "ligneCommandeFournisseur", source = "ligneCommandeFournisseur", qualifiedByName = "id")
    @Mapping(target = "ligneVente", source = "ligneVente", qualifiedByName = "id")
    @Mapping(target = "categorie", source = "categorie", qualifiedByName = "id")
    ArticleDTO toDto(Article s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ArticleDTO toDtoId(Article article);
}
