package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.LigneCommandeFournisseur} entity.
 */
public class LigneCommandeFournisseurDTO implements Serializable {

    private Long id;

    private BigDecimal quantite;

    private BigDecimal prixUnitaire;

    private CommandeFournisseurDTO commandeFournisseur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public CommandeFournisseurDTO getCommandeFournisseur() {
        return commandeFournisseur;
    }

    public void setCommandeFournisseur(CommandeFournisseurDTO commandeFournisseur) {
        this.commandeFournisseur = commandeFournisseur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LigneCommandeFournisseurDTO)) {
            return false;
        }

        LigneCommandeFournisseurDTO ligneCommandeFournisseurDTO = (LigneCommandeFournisseurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ligneCommandeFournisseurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LigneCommandeFournisseurDTO{" +
            "id=" + getId() +
            ", quantite=" + getQuantite() +
            ", prixUnitaire=" + getPrixUnitaire() +
            ", commandeFournisseur=" + getCommandeFournisseur() +
            "}";
    }
}
