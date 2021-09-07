package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Article} entity.
 */
public class ArticleDTO implements Serializable {

    private Long id;

    private String codeArticle;

    private String designation;

    private BigDecimal prixUnitaireHT;

    private BigDecimal prixUnitaireTtc;

    private BigDecimal tauxTva;

    @Lob
    private byte[] photo;

    private String photoContentType;
    private EntrepriseDTO entreprise;

    private LigneCommandeClientDTO ligneCommandeClient;

    private LigneCommandeFournisseurDTO ligneCommandeFournisseur;

    private LigneVenteDTO ligneVente;

    private CategorieDTO categorie;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(String codeArticle) {
        this.codeArticle = codeArticle;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public BigDecimal getPrixUnitaireHT() {
        return prixUnitaireHT;
    }

    public void setPrixUnitaireHT(BigDecimal prixUnitaireHT) {
        this.prixUnitaireHT = prixUnitaireHT;
    }

    public BigDecimal getPrixUnitaireTtc() {
        return prixUnitaireTtc;
    }

    public void setPrixUnitaireTtc(BigDecimal prixUnitaireTtc) {
        this.prixUnitaireTtc = prixUnitaireTtc;
    }

    public BigDecimal getTauxTva() {
        return tauxTva;
    }

    public void setTauxTva(BigDecimal tauxTva) {
        this.tauxTva = tauxTva;
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

    public EntrepriseDTO getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(EntrepriseDTO entreprise) {
        this.entreprise = entreprise;
    }

    public LigneCommandeClientDTO getLigneCommandeClient() {
        return ligneCommandeClient;
    }

    public void setLigneCommandeClient(LigneCommandeClientDTO ligneCommandeClient) {
        this.ligneCommandeClient = ligneCommandeClient;
    }

    public LigneCommandeFournisseurDTO getLigneCommandeFournisseur() {
        return ligneCommandeFournisseur;
    }

    public void setLigneCommandeFournisseur(LigneCommandeFournisseurDTO ligneCommandeFournisseur) {
        this.ligneCommandeFournisseur = ligneCommandeFournisseur;
    }

    public LigneVenteDTO getLigneVente() {
        return ligneVente;
    }

    public void setLigneVente(LigneVenteDTO ligneVente) {
        this.ligneVente = ligneVente;
    }

    public CategorieDTO getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieDTO categorie) {
        this.categorie = categorie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArticleDTO)) {
            return false;
        }

        ArticleDTO articleDTO = (ArticleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, articleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArticleDTO{" +
            "id=" + getId() +
            ", codeArticle='" + getCodeArticle() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", prixUnitaireHT=" + getPrixUnitaireHT() +
            ", prixUnitaireTtc=" + getPrixUnitaireTtc() +
            ", tauxTva=" + getTauxTva() +
            ", photo='" + getPhoto() + "'" +
            ", entreprise=" + getEntreprise() +
            ", ligneCommandeClient=" + getLigneCommandeClient() +
            ", ligneCommandeFournisseur=" + getLigneCommandeFournisseur() +
            ", ligneVente=" + getLigneVente() +
            ", categorie=" + getCategorie() +
            "}";
    }
}
