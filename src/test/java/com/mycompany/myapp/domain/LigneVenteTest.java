package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LigneVenteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LigneVente.class);
        LigneVente ligneVente1 = new LigneVente();
        ligneVente1.setId(1L);
        LigneVente ligneVente2 = new LigneVente();
        ligneVente2.setId(ligneVente1.getId());
        assertThat(ligneVente1).isEqualTo(ligneVente2);
        ligneVente2.setId(2L);
        assertThat(ligneVente1).isNotEqualTo(ligneVente2);
        ligneVente1.setId(null);
        assertThat(ligneVente1).isNotEqualTo(ligneVente2);
    }
}
