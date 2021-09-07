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
 * A Vente.
 */
@Entity
@Table(name = "vente")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "date_vente")
    private Instant dateVente;

    @Column(name = "commentaire")
    private String commentaire;

    @OneToMany(mappedBy = "vente")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "articles", "vente" }, allowSetters = true)
    private Set<LigneVente> ligneVentes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vente id(Long id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public Vente code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getDateVente() {
        return this.dateVente;
    }

    public Vente dateVente(Instant dateVente) {
        this.dateVente = dateVente;
        return this;
    }

    public void setDateVente(Instant dateVente) {
        this.dateVente = dateVente;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public Vente commentaire(String commentaire) {
        this.commentaire = commentaire;
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Set<LigneVente> getLigneVentes() {
        return this.ligneVentes;
    }

    public Vente ligneVentes(Set<LigneVente> ligneVentes) {
        this.setLigneVentes(ligneVentes);
        return this;
    }

    public Vente addLigneVente(LigneVente ligneVente) {
        this.ligneVentes.add(ligneVente);
        ligneVente.setVente(this);
        return this;
    }

    public Vente removeLigneVente(LigneVente ligneVente) {
        this.ligneVentes.remove(ligneVente);
        ligneVente.setVente(null);
        return this;
    }

    public void setLigneVentes(Set<LigneVente> ligneVentes) {
        if (this.ligneVentes != null) {
            this.ligneVentes.forEach(i -> i.setVente(null));
        }
        if (ligneVentes != null) {
            ligneVentes.forEach(i -> i.setVente(this));
        }
        this.ligneVentes = ligneVentes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vente)) {
            return false;
        }
        return id != null && id.equals(((Vente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vente{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", dateVente='" + getDateVente() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            "}";
    }
}
