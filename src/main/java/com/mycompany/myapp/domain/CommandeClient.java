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
 * A CommandeClient.
 */
@Entity
@Table(name = "commande_client")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CommandeClient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "date_commande")
    private Instant dateCommande;

    @OneToMany(mappedBy = "commandeClient")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "articles", "commandeClient" }, allowSetters = true)
    private Set<LigneCommandeClient> ligneCommandeClients = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "commandeClients", "adresse" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CommandeClient id(Long id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public CommandeClient code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getDateCommande() {
        return this.dateCommande;
    }

    public CommandeClient dateCommande(Instant dateCommande) {
        this.dateCommande = dateCommande;
        return this;
    }

    public void setDateCommande(Instant dateCommande) {
        this.dateCommande = dateCommande;
    }

    public Set<LigneCommandeClient> getLigneCommandeClients() {
        return this.ligneCommandeClients;
    }

    public CommandeClient ligneCommandeClients(Set<LigneCommandeClient> ligneCommandeClients) {
        this.setLigneCommandeClients(ligneCommandeClients);
        return this;
    }

    public CommandeClient addLigneCommandeClient(LigneCommandeClient ligneCommandeClient) {
        this.ligneCommandeClients.add(ligneCommandeClient);
        ligneCommandeClient.setCommandeClient(this);
        return this;
    }

    public CommandeClient removeLigneCommandeClient(LigneCommandeClient ligneCommandeClient) {
        this.ligneCommandeClients.remove(ligneCommandeClient);
        ligneCommandeClient.setCommandeClient(null);
        return this;
    }

    public void setLigneCommandeClients(Set<LigneCommandeClient> ligneCommandeClients) {
        if (this.ligneCommandeClients != null) {
            this.ligneCommandeClients.forEach(i -> i.setCommandeClient(null));
        }
        if (ligneCommandeClients != null) {
            ligneCommandeClients.forEach(i -> i.setCommandeClient(this));
        }
        this.ligneCommandeClients = ligneCommandeClients;
    }

    public Client getClient() {
        return this.client;
    }

    public CommandeClient client(Client client) {
        this.setClient(client);
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommandeClient)) {
            return false;
        }
        return id != null && id.equals(((CommandeClient) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommandeClient{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", dateCommande='" + getDateCommande() + "'" +
            "}";
    }
}
