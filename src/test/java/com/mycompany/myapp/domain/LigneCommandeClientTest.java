package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LigneCommandeClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LigneCommandeClient.class);
        LigneCommandeClient ligneCommandeClient1 = new LigneCommandeClient();
        ligneCommandeClient1.setId(1L);
        LigneCommandeClient ligneCommandeClient2 = new LigneCommandeClient();
        ligneCommandeClient2.setId(ligneCommandeClient1.getId());
        assertThat(ligneCommandeClient1).isEqualTo(ligneCommandeClient2);
        ligneCommandeClient2.setId(2L);
        assertThat(ligneCommandeClient1).isNotEqualTo(ligneCommandeClient2);
        ligneCommandeClient1.setId(null);
        assertThat(ligneCommandeClient1).isNotEqualTo(ligneCommandeClient2);
    }
}
