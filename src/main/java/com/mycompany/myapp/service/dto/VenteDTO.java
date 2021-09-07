package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Vente} entity.
 */
public class VenteDTO implements Serializable {

    private Long id;

    private String code;

    private Instant dateVente;

    private String commentaire;

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

    public Instant getDateVente() {
        return dateVente;
    }

    public void setDateVente(Instant dateVente) {
        this.dateVente = dateVente;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VenteDTO)) {
            return false;
        }

        VenteDTO venteDTO = (VenteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, venteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VenteDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", dateVente='" + getDateVente() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            "}";
    }
}
