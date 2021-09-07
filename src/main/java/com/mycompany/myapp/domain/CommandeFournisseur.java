package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CommandeFournisseur.
 */
@Entity
@Table(name = "commande_fournisseur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CommandeFournisseur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "date_commande")
    private Instant dateCommande;

    @OneToMany(mappedBy = "commandeFournisseur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "articles", "commandeFournisseur" }, allowSetters = true)
    private Set<LigneCommandeFournisseur> ligneCommandeFournisseurs = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "commandeFournisseurs", "adresse" }, allowSetters = true)
    private Fournisseur fournisseur;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CommandeFournisseur id(Long id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public CommandeFournisseur code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getDateCommande() {
        return this.dateCommande;
    }

    public CommandeFournisseur dateCommande(Instant dateCommande) {
        this.dateCommande = dateCommande;
        return this;
    }

    public void setDateCommande(Instant dateCommande) {
        this.dateCommande = dateCommande;
    }

    public Set<LigneCommandeFournisseur> getLigneCommandeFournisseurs() {
        return this.ligneCommandeFournisseurs;
    }

    public CommandeFournisseur ligneCommandeFournisseurs(Set<LigneCommandeFournisseur> ligneCommandeFournisseurs) {
        this.setLigneCommandeFournisseurs(ligneCommandeFournisseurs);
        return this;
    }

    public CommandeFournisseur addLigneCommandeFournisseur(LigneCommandeFournisseur ligneCommandeFournisseur) {
        this.ligneCommandeFournisseurs.add(ligneCommandeFournisseur);
        ligneCommandeFournisseur.setCommandeFournisseur(this);
        return this;
    }

    public CommandeFournisseur removeLigneCommandeFournisseur(LigneCommandeFournisseur ligneCommandeFournisseur) {
        this.ligneCommandeFournisseurs.remove(ligneCommandeFournisseur);
        ligneCommandeFournisseur.setCommandeFournisseur(null);
        return this;
    }

    public void setLigneCommandeFournisseurs(Set<LigneCommandeFournisseur> ligneCommandeFournisseurs) {
        if (this.ligneCommandeFournisseurs != null) {
            this.ligneCommandeFournisseurs.forEach(i -> i.setCommandeFournisseur(null));
        }
        if (ligneCommandeFournisseurs != null) {
            ligneCommandeFournisseurs.forEach(i -> i.setCommandeFournisseur(this));
        }
        this.ligneCommandeFournisseurs = ligneCommandeFournisseurs;
    }

    public Fournisseur getFournisseur() {
        return this.fournisseur;
    }

    public CommandeFournisseur fournisseur(Fournisseur fournisseur) {
        this.setFournisseur(fournisseur);
        return this;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommandeFournisseur)) {
            return false;
        }
        return id != null && id.equals(((CommandeFournisseur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommandeFournisseur{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", dateCommande='" + getDateCommande() + "'" +
            "}";
    }
}
