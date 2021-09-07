package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Article.
 */
@Entity
@Table(name = "article")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "code_article")
    private String codeArticle;

    @Column(name = "designation")
    private String designation;

    @Column(name = "prix_unitaire_ht", precision = 21, scale = 2)
    private BigDecimal prixUnitaireHT;

    @Column(name = "prix_unitaire_ttc", precision = 21, scale = 2)
    private BigDecimal prixUnitaireTtc;

    @Column(name = "taux_tva", precision = 21, scale = 2)
    private BigDecimal tauxTva;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @OneToMany(mappedBy = "entreprise")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "entreprise" }, allowSetters = true)
    private Set<MvtStk> utilisateurs = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "personnel", "articles", "adresse" }, allowSetters = true)
    private Entreprise entreprise;

    @ManyToOne
    @JsonIgnoreProperties(value = { "articles", "commandeClient" }, allowSetters = true)
    private LigneCommandeClient ligneCommandeClient;

    @ManyToOne
    @JsonIgnoreProperties(value = { "articles", "commandeFournisseur" }, allowSetters = true)
    private LigneCommandeFournisseur ligneCommandeFournisseur;

    @ManyToOne
    @JsonIgnoreProperties(value = { "articles", "vente" }, allowSetters = true)
    private LigneVente ligneVente;

    @ManyToOne
    @JsonIgnoreProperties(value = { "articles" }, allowSetters = true)
    private Categorie categorie;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Article id(Long id) {
        this.id = id;
        return this;
    }

    public String getCodeArticle() {
        return this.codeArticle;
    }

    public Article codeArticle(String codeArticle) {
        this.codeArticle = codeArticle;
        return this;
    }

    public void setCodeArticle(String codeArticle) {
        this.codeArticle = codeArticle;
    }

    public String getDesignation() {
        return this.designation;
    }

    public Article designation(String designation) {
        this.designation = designation;
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public BigDecimal getPrixUnitaireHT() {
        return this.prixUnitaireHT;
    }

    public Article prixUnitaireHT(BigDecimal prixUnitaireHT) {
        this.prixUnitaireHT = prixUnitaireHT;
        return this;
    }

    public void setPrixUnitaireHT(BigDecimal prixUnitaireHT) {
        this.prixUnitaireHT = prixUnitaireHT;
    }

    public BigDecimal getPrixUnitaireTtc() {
        return this.prixUnitaireTtc;
    }

    public Article prixUnitaireTtc(BigDecimal prixUnitaireTtc) {
        this.prixUnitaireTtc = prixUnitaireTtc;
        return this;
    }

    public void setPrixUnitaireTtc(BigDecimal prixUnitaireTtc) {
        this.prixUnitaireTtc = prixUnitaireTtc;
    }

    public BigDecimal getTauxTva() {
        return this.tauxTva;
    }

    public Article tauxTva(BigDecimal tauxTva) {
        this.tauxTva = tauxTva;
        return this;
    }

    public void setTauxTva(BigDecimal tauxTva) {
        this.tauxTva = tauxTva;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public Article photo(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public Article photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Set<MvtStk> getUtilisateurs() {
        return this.utilisateurs;
    }

    public Article utilisateurs(Set<MvtStk> mvtStks) {
        this.setUtilisateurs(mvtStks);
        return this;
    }

    public Article addUtilisateur(MvtStk mvtStk) {
        this.utilisateurs.add(mvtStk);
        mvtStk.setEntreprise(this);
        return this;
    }

    public Article removeUtilisateur(MvtStk mvtStk) {
        this.utilisateurs.remove(mvtStk);
        mvtStk.setEntreprise(null);
        return this;
    }

    public void setUtilisateurs(Set<MvtStk> mvtStks) {
        if (this.utilisateurs != null) {
            this.utilisateurs.forEach(i -> i.setEntreprise(null));
        }
        if (mvtStks != null) {
            mvtStks.forEach(i -> i.setEntreprise(this));
        }
        this.utilisateurs = mvtStks;
    }

    public Entreprise getEntreprise() {
        return this.entreprise;
    }

    public Article entreprise(Entreprise entreprise) {
        this.setEntreprise(entreprise);
        return this;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public LigneCommandeClient getLigneCommandeClient() {
        return this.ligneCommandeClient;
    }

    public Article ligneCommandeClient(LigneCommandeClient ligneCommandeClient) {
        this.setLigneCommandeClient(ligneCommandeClient);
        return this;
    }

    public void setLigneCommandeClient(LigneCommandeClient ligneCommandeClient) {
        this.ligneCommandeClient = ligneCommandeClient;
    }

    public LigneCommandeFournisseur getLigneCommandeFournisseur() {
        return this.ligneCommandeFournisseur;
    }

    public Article ligneCommandeFournisseur(LigneCommandeFournisseur ligneCommandeFournisseur) {
        this.setLigneCommandeFournisseur(ligneCommandeFournisseur);
        return this;
    }

    public void setLigneCommandeFournisseur(LigneCommandeFournisseur ligneCommandeFournisseur) {
        this.ligneCommandeFournisseur = ligneCommandeFournisseur;
    }

    public LigneVente getLigneVente() {
        return this.ligneVente;
    }

    public Article ligneVente(LigneVente ligneVente) {
        this.setLigneVente(ligneVente);
        return this;
    }

    public void setLigneVente(LigneVente ligneVente) {
        this.ligneVente = ligneVente;
    }

    public Categorie getCategorie() {
        return this.categorie;
    }

    public Article categorie(Categorie categorie) {
        this.setCategorie(categorie);
        return this;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Article)) {
            return false;
        }
        return id != null && id.equals(((Article) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Article{" +
            "id=" + getId() +
            ", codeArticle='" + getCodeArticle() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", prixUnitaireHT=" + getPrixUnitaireHT() +
            ", prixUnitaireTtc=" + getPrixUnitaireTtc() +
            ", tauxTva=" + getTauxTva() +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            "}";
    }
}
