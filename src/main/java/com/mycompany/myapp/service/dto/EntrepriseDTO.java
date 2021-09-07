package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Entreprise} entity.
 */
public class EntrepriseDTO implements Serializable {

    private Long id;

    private String nom;

    private String description;

    private String codeFiscal;

    @Lob
    private byte[] photo;

    private String photoContentType;
    private String email;

    private String numTe;

    private String siteWeb;

    private AdresseDTO adresse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodeFiscal() {
        return codeFiscal;
    }

    public void setCodeFiscal(String codeFiscal) {
        this.codeFiscal = codeFiscal;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumTe() {
        return numTe;
    }

    public void setNumTe(String numTe) {
        this.numTe = numTe;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public AdresseDTO getAdresse() {
        return adresse;
    }

    public void setAdresse(AdresseDTO adresse) {
        this.adresse = adresse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntrepriseDTO)) {
            return false;
        }

        EntrepriseDTO entrepriseDTO = (EntrepriseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, entrepriseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EntrepriseDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", codeFiscal='" + getCodeFiscal() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", email='" + getEmail() + "'" +
            ", numTe='" + getNumTe() + "'" +
            ", siteWeb='" + getSiteWeb() + "'" +
            ", adresse=" + getAdresse() +
            "}";
    }
}
