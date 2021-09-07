package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Adresse} entity.
 */
public class AdresseDTO implements Serializable {

    private Long id;

    private String adresse1;

    private String adresse2;

    private String ville;

    private String codePostal;

    private String pays;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdresse1() {
        return adresse1;
    }

    public void setAdresse1(String adresse1) {
        this.adresse1 = adresse1;
    }

    public String getAdresse2() {
        return adresse2;
    }

    public void setAdresse2(String adresse2) {
        this.adresse2 = adresse2;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdresseDTO)) {
            return false;
        }

        AdresseDTO adresseDTO = (AdresseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, adresseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdresseDTO{" +
            "id=" + getId() +
            ", adresse1='" + getAdresse1() + "'" +
            ", adresse2='" + getAdresse2() + "'" +
            ", ville='" + getVille() + "'" +
            ", codePostal='" + getCodePostal() + "'" +
            ", pays='" + getPays() + "'" +
            "}";
    }
}
