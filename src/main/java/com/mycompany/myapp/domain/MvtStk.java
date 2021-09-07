package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.TypeMvtStk;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MvtStk.
 */
@Entity
@Table(name = "mvt_stk")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MvtStk implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "date_mvt")
    private Instant dateMvt;

    @Column(name = "quantite", precision = 21, scale = 2)
    private BigDecimal quantite;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_mvt_stk")
    private TypeMvtStk typeMvtStk;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "utilisateurs", "entreprise", "ligneCommandeClient", "ligneCommandeFournisseur", "ligneVente", "categorie" },
        allowSetters = true
    )
    private Article entreprise;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MvtStk id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getDateMvt() {
        return this.dateMvt;
    }

    public MvtStk dateMvt(Instant dateMvt) {
        this.dateMvt = dateMvt;
        return this;
    }

    public void setDateMvt(Instant dateMvt) {
        this.dateMvt = dateMvt;
    }

    public BigDecimal getQuantite() {
        return this.quantite;
    }

    public MvtStk quantite(BigDecimal quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public TypeMvtStk getTypeMvtStk() {
        return this.typeMvtStk;
    }

    public MvtStk typeMvtStk(TypeMvtStk typeMvtStk) {
        this.typeMvtStk = typeMvtStk;
        return this;
    }

    public void setTypeMvtStk(TypeMvtStk typeMvtStk) {
        this.typeMvtStk = typeMvtStk;
    }

    public Article getEntreprise() {
        return this.entreprise;
    }

    public MvtStk entreprise(Article article) {
        this.setEntreprise(article);
        return this;
    }

    public void setEntreprise(Article article) {
        this.entreprise = article;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MvtStk)) {
            return false;
        }
        return id != null && id.equals(((MvtStk) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MvtStk{" +
            "id=" + getId() +
            ", dateMvt='" + getDateMvt() + "'" +
            ", quantite=" + getQuantite() +
            ", typeMvtStk='" + getTypeMvtStk() + "'" +
            "}";
    }
}
