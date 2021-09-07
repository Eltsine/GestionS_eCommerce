package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.TypeMvtStk;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.MvtStk} entity.
 */
public class MvtStkDTO implements Serializable {

    private Long id;

    private Instant dateMvt;

    private BigDecimal quantite;

    private TypeMvtStk typeMvtStk;

    private ArticleDTO entreprise;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateMvt() {
        return dateMvt;
    }

    public void setDateMvt(Instant dateMvt) {
        this.dateMvt = dateMvt;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public TypeMvtStk getTypeMvtStk() {
        return typeMvtStk;
    }

    public void setTypeMvtStk(TypeMvtStk typeMvtStk) {
        this.typeMvtStk = typeMvtStk;
    }

    public ArticleDTO getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(ArticleDTO entreprise) {
        this.entreprise = entreprise;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MvtStkDTO)) {
            return false;
        }

        MvtStkDTO mvtStkDTO = (MvtStkDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mvtStkDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MvtStkDTO{" +
            "id=" + getId() +
            ", dateMvt='" + getDateMvt() + "'" +
            ", quantite=" + getQuantite() +
            ", typeMvtStk='" + getTypeMvtStk() + "'" +
            ", entreprise=" + getEntreprise() +
            "}";
    }
}
