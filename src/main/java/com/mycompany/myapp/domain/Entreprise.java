package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Entreprise.
 */
@Entity
@Table(name = "entreprise")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Entreprise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "description")
    private String description;

    @Column(name = "code_fiscal")
    private String codeFiscal;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @Column(name = "email")
    private String email;

    @Column(name = "num_te")
    private String numTe;

    @Column(name = "site_web")
    private String siteWeb;

    @OneToMany(mappedBy = "entreprise")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "adresse", "entreprise" }, allowSetters = true)
    private Set<Personnel> personnel = new HashSet<>();

    @OneToMany(mappedBy = "entreprise")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "utilisateurs", "entreprise", "ligneCommandeClient", "ligneCommandeFournisseur", "ligneVente", "categorie" },
        allowSetters = true
    )
    private Set<Article> articles = new HashSet<>();

    @ManyToOne
    private Adresse adresse;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Entreprise id(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return this.nom;
    }

    public Entreprise nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public Entreprise description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodeFiscal() {
        return this.codeFiscal;
    }

    public Entreprise codeFiscal(String codeFiscal) {
        this.codeFiscal = codeFiscal;
        return this;
    }

    public void setCodeFiscal(String codeFiscal) {
        this.codeFiscal = codeFiscal;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public Entreprise photo(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public Entreprise photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getEmail() {
        return this.email;
    }

    public Entreprise email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumTe() {
        return this.numTe;
    }

    public Entreprise numTe(String numTe) {
        this.numTe = numTe;
        return this;
    }

    public void setNumTe(String numTe) {
        this.numTe = numTe;
    }

    public String getSiteWeb() {
        return this.siteWeb;
    }

    public Entreprise siteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
        return this;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public Set<Personnel> getPersonnel() {
        return this.personnel;
    }

    public Entreprise personnel(Set<Personnel> personnel) {
        this.setPersonnel(personnel);
        return this;
    }

    public Entreprise addPersonnel(Personnel personnel) {
        this.personnel.add(personnel);
        personnel.setEntreprise(this);
        return this;
    }

    public Entreprise removePersonnel(Personnel personnel) {
        this.personnel.remove(personnel);
        personnel.setEntreprise(null);
        return this;
    }

    public void setPersonnel(Set<Personnel> personnel) {
        if (this.personnel != null) {
            this.personnel.forEach(i -> i.setEntreprise(null));
        }
        if (personnel != null) {
            personnel.forEach(i -> i.setEntreprise(this));
        }
        this.personnel = personnel;
    }

    public Set<Article> getArticles() {
        return this.articles;
    }

    public Entreprise articles(Set<Article> articles) {
        this.setArticles(articles);
        return this;
    }

    public Entreprise addArticle(Article article) {
        this.articles.add(article);
        article.setEntreprise(this);
        return this;
    }

    public Entreprise removeArticle(Article article) {
        this.articles.remove(article);
        article.setEntreprise(null);
        return this;
    }

    public void setArticles(Set<Article> articles) {
        if (this.articles != null) {
            this.articles.forEach(i -> i.setEntreprise(null));
        }
        if (articles != null) {
            articles.forEach(i -> i.setEntreprise(this));
        }
        this.articles = articles;
    }

    public Adresse getAdresse() {
        return this.adresse;
    }

    public Entreprise adresse(Adresse adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entreprise)) {
            return false;
        }
        return id != null && id.equals(((Entreprise) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Entreprise{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", codeFiscal='" + getCodeFiscal() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            ", email='" + getEmail() + "'" +
            ", numTe='" + getNumTe() + "'" +
            ", siteWeb='" + getSiteWeb() + "'" +
            "}";
    }
}
