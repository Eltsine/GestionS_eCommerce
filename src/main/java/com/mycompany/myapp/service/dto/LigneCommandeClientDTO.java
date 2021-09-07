package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.LigneCommandeClient} entity.
 */
public class LigneCommandeClientDTO implements Serializable {

    private Long id;

    private BigDecimal quantite;

    private BigDecimal prixUnitaire;

    private CommandeClientDTO commandeClient;

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

    public CommandeClientDTO getCommandeClient() {
        return commandeClient;
    }

    public void setCommandeClient(CommandeClientDTO commandeClient) {
        this.commandeClient = commandeClient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LigneCommandeClientDTO)) {
            return false;
        }

        LigneCommandeClientDTO ligneCommandeClientDTO = (LigneCommandeClientDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ligneCommandeClientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LigneCommandeClientDTO{" +
            "id=" + getId() +
            ", quantite=" + getQuantite() +
            ", prixUnitaire=" + getPrixUnitaire() +
            ", commandeClient=" + getCommandeClient() +
            "}";
    }
}
