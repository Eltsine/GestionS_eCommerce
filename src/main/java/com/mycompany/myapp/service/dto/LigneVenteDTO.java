package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.LigneVente} entity.
 */
public class LigneVenteDTO implements Serializable {

    private Long id;

    private BigDecimal quantite;

    private BigDecimal prixUnitaire;

    private VenteDTO vente;

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

    public VenteDTO getVente() {
        return vente;
    }

    public void setVente(VenteDTO vente) {
        this.vente = vente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LigneVenteDTO)) {
            return false;
        }

        LigneVenteDTO ligneVenteDTO = (LigneVenteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ligneVenteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LigneVenteDTO{" +
            "id=" + getId() +
            ", quantite=" + getQuantite() +
            ", prixUnitaire=" + getPrixUnitaire() +
            ", vente=" + getVente() +
            "}";
    }
}
