package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LigneCommandeFournisseurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LigneCommandeFournisseur.class);
        LigneCommandeFournisseur ligneCommandeFournisseur1 = new LigneCommandeFournisseur();
        ligneCommandeFournisseur1.setId(1L);
        LigneCommandeFournisseur ligneCommandeFournisseur2 = new LigneCommandeFournisseur();
        ligneCommandeFournisseur2.setId(ligneCommandeFournisseur1.getId());
        assertThat(ligneCommandeFournisseur1).isEqualTo(ligneCommandeFournisseur2);
        ligneCommandeFournisseur2.setId(2L);
        assertThat(ligneCommandeFournisseur1).isNotEqualTo(ligneCommandeFournisseur2);
        ligneCommandeFournisseur1.setId(null);
        assertThat(ligneCommandeFournisseur1).isNotEqualTo(ligneCommandeFournisseur2);
    }
}
