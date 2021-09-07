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
 * A LigneVente.
 */
@Entity
@Table(name = "ligne_vente")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LigneVente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "quantite", precision = 21, scale = 2)
    private BigDecimal quantite;

    @Column(name = "prix_unitaire", precision = 21, scale = 2)
    private BigDecimal prixUnitaire;

    @OneToMany(mappedBy = "ligneVente")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "utilisateurs", "entreprise", "ligneCommandeClient", "ligneCommandeFournisseur", "ligneVente", "categorie" },
        allowSetters = true
    )
    private Set<Article> articles = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "ligneVentes" }, allowSetters = true)
    private Vente vente;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LigneVente id(Long id) {
        this.id = id;
        return this;
    }

    public BigDecimal getQuantite() {
        return this.quantite;
    }

    public LigneVente quantite(BigDecimal quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrixUnitaire() {
        return this.prixUnitaire;
    }

    public LigneVente prixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
        return this;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Set<Article> getArticles() {
        return this.articles;
    }

    public LigneVente articles(Set<Article> articles) {
        this.setArticles(articles);
        return this;
    }

    public LigneVente addArticle(Article article) {
        this.articles.add(article);
        article.setLigneVente(this);
        return this;
    }

    public LigneVente removeArticle(Article article) {
        this.articles.remove(article);
        article.setLigneVente(null);
        return this;
    }

    public void setArticles(Set<Article> articles) {
        if (this.articles != null) {
            this.articles.forEach(i -> i.setLigneVente(null));
        }
        if (articles != null) {
            articles.forEach(i -> i.setLigneVente(this));
        }
        this.articles = articles;
    }

    public Vente getVente() {
        return this.vente;
    }

    public LigneVente vente(Vente vente) {
        this.setVente(vente);
        return this;
    }

    public void setVente(Vente vente) {
        this.vente = vente;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LigneVente)) {
            return false;
        }
        return id != null && id.equals(((LigneVente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LigneVente{" +
            "id=" + getId() +
            ", quantite=" + getQuantite() +
            ", prixUnitaire=" + getPrixUnitaire() +
            "}";
    }
}
