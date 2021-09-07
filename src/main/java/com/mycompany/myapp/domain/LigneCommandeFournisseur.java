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
 * A LigneCommandeFournisseur.
 */
@Entity
@Table(name = "ligne_commande_fournisseur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LigneCommandeFournisseur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "quantite", precision = 21, scale = 2)
    private BigDecimal quantite;

    @Column(name = "prix_unitaire", precision = 21, scale = 2)
    private BigDecimal prixUnitaire;

    @OneToMany(mappedBy = "ligneCommandeFournisseur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "utilisateurs", "entreprise", "ligneCommandeClient", "ligneCommandeFournisseur", "ligneVente", "categorie" },
        allowSetters = true
    )
    private Set<Article> articles = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "ligneCommandeFournisseurs", "fournisseur" }, allowSetters = true)
    private CommandeFournisseur commandeFournisseur;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LigneCommandeFournisseur id(Long id) {
        this.id = id;
        return this;
    }

    public BigDecimal getQuantite() {
        return this.quantite;
    }

    public LigneCommandeFournisseur quantite(BigDecimal quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrixUnitaire() {
        return this.prixUnitaire;
    }

    public LigneCommandeFournisseur prixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
        return this;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Set<Article> getArticles() {
        return this.articles;
    }

    public LigneCommandeFournisseur articles(Set<Article> articles) {
        this.setArticles(articles);
        return this;
    }

    public LigneCommandeFournisseur addArticle(Article article) {
        this.articles.add(article);
        article.setLigneCommandeFournisseur(this);
        return this;
    }

    public LigneCommandeFournisseur removeArticle(Article article) {
        this.articles.remove(article);
        article.setLigneCommandeFournisseur(null);
        return this;
    }

    public void setArticles(Set<Article> articles) {
        if (this.articles != null) {
            this.articles.forEach(i -> i.setLigneCommandeFournisseur(null));
        }
        if (articles != null) {
            articles.forEach(i -> i.setLigneCommandeFournisseur(this));
        }
        this.articles = articles;
    }

    public CommandeFournisseur getCommandeFournisseur() {
        return this.commandeFournisseur;
    }

    public LigneCommandeFournisseur commandeFournisseur(CommandeFournisseur commandeFournisseur) {
        this.setCommandeFournisseur(commandeFournisseur);
        return this;
    }

    public void setCommandeFournisseur(CommandeFournisseur commandeFournisseur) {
        this.commandeFournisseur = commandeFournisseur;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LigneCommandeFournisseur)) {
            return false;
        }
        return id != null && id.equals(((LigneCommandeFournisseur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LigneCommandeFournisseur{" +
            "id=" + getId() +
            ", quantite=" + getQuantite() +
            ", prixUnitaire=" + getPrixUnitaire() +
            "}";
    }
}
