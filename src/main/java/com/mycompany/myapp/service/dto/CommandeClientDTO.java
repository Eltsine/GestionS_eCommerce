package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.CommandeClient} entity.
 */
public class CommandeClientDTO implements Serializable {

    private Long id;

    private String code;

    private Instant dateCommande;

    private ClientDTO client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Instant dateCommande) {
        this.dateCommande = dateCommande;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommandeClientDTO)) {
            return false;
        }

        CommandeClientDTO commandeClientDTO = (CommandeClientDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commandeClientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommandeClientDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", dateCommande='" + getDateCommande() + "'" +
            ", client=" + getClient() +
            "}";
    }
}
