package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.CommandeFournisseur} entity.
 */
public class CommandeFournisseurDTO implements Serializable {

    private Long id;

    private String code;

    private Instant dateCommande;

    private FournisseurDTO fournisseur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Instant dateCommande) {
        this.dateCommande = dateCommande;
    }

    public FournisseurDTO getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(FournisseurDTO fournisseur) {
        this.fournisseur = fournisseur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommandeFournisseurDTO)) {
            return false;
        }

        CommandeFournisseurDTO commandeFournisseurDTO = (CommandeFournisseurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commandeFournisseurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommandeFournisseurDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", dateCommande='" + getDateCommande() + "'" +
            ", fournisseur=" + getFournisseur() +
            "}";
    }
}
