package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Fournisseur.
 */
@Entity
@Table(name = "fournisseur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Fournisseur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "mail")
    private String mail;

    @Column(name = "num_tel")
    private String numTel;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @OneToMany(mappedBy = "fournisseur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ligneCommandeFournisseurs", "fournisseur" }, allowSetters = true)
    private Set<CommandeFournisseur> commandeFournisseurs = new HashSet<>();

    @ManyToOne
    private Adresse adresse;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fournisseur id(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return this.nom;
    }

    public Fournisseur nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Fournisseur prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return this.mail;
    }

    public Fournisseur mail(String mail) {
        this.mail = mail;
        return this;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNumTel() {
        return this.numTel;
    }

    public Fournisseur numTel(String numTel) {
        this.numTel = numTel;
        return this;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public Fournisseur photo(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public Fournisseur photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Set<CommandeFournisseur> getCommandeFournisseurs() {
        return this.commandeFournisseurs;
    }

    public Fournisseur commandeFournisseurs(Set<CommandeFournisseur> commandeFournisseurs) {
        this.setCommandeFournisseurs(commandeFournisseurs);
        return this;
    }

    public Fournisseur addCommandeFournisseur(CommandeFournisseur commandeFournisseur) {
        this.commandeFournisseurs.add(commandeFournisseur);
        commandeFournisseur.setFournisseur(this);
        return this;
    }

    public Fournisseur removeCommandeFournisseur(CommandeFournisseur commandeFournisseur) {
        this.commandeFournisseurs.remove(commandeFournisseur);
        commandeFournisseur.setFournisseur(null);
        return this;
    }

    public void setCommandeFournisseurs(Set<CommandeFournisseur> commandeFournisseurs) {
        if (this.commandeFournisseurs != null) {
            this.commandeFournisseurs.forEach(i -> i.setFournisseur(null));
        }
        if (commandeFournisseurs != null) {
            commandeFournisseurs.forEach(i -> i.setFournisseur(this));
        }
        this.commandeFournisseurs = commandeFournisseurs;
    }

    public Adresse getAdresse() {
        return this.adresse;
    }

    public Fournisseur adresse(Adresse adresse) {
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
        if (!(o instanceof Fournisseur)) {
            return false;
        }
        return id != null && id.equals(((Fournisseur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fournisseur{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", mail='" + getMail() + "'" +
            ", numTel='" + getNumTel() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            "}";
    }
}
