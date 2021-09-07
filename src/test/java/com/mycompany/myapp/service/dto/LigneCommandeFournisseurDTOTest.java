package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LigneCommandeFournisseurDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LigneCommandeFournisseurDTO.class);
        LigneCommandeFournisseurDTO ligneCommandeFournisseurDTO1 = new LigneCommandeFournisseurDTO();
        ligneCommandeFournisseurDTO1.setId(1L);
        LigneCommandeFournisseurDTO ligneCommandeFournisseurDTO2 = new LigneCommandeFournisseurDTO();
        assertThat(ligneCommandeFournisseurDTO1).isNotEqualTo(ligneCommandeFournisseurDTO2);
        ligneCommandeFournisseurDTO2.setId(ligneCommandeFournisseurDTO1.getId());
        assertThat(ligneCommandeFournisseurDTO1).isEqualTo(ligneCommandeFournisseurDTO2);
        ligneCommandeFournisseurDTO2.setId(2L);
        assertThat(ligneCommandeFournisseurDTO1).isNotEqualTo(ligneCommandeFournisseurDTO2);
        ligneCommandeFournisseurDTO1.setId(null);
        assertThat(ligneCommandeFournisseurDTO1).isNotEqualTo(ligneCommandeFournisseurDTO2);
    }
}
