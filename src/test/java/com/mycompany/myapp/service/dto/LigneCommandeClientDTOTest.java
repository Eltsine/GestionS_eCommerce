package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LigneCommandeClientDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LigneCommandeClientDTO.class);
        LigneCommandeClientDTO ligneCommandeClientDTO1 = new LigneCommandeClientDTO();
        ligneCommandeClientDTO1.setId(1L);
        LigneCommandeClientDTO ligneCommandeClientDTO2 = new LigneCommandeClientDTO();
        assertThat(ligneCommandeClientDTO1).isNotEqualTo(ligneCommandeClientDTO2);
        ligneCommandeClientDTO2.setId(ligneCommandeClientDTO1.getId());
        assertThat(ligneCommandeClientDTO1).isEqualTo(ligneCommandeClientDTO2);
        ligneCommandeClientDTO2.setId(2L);
        assertThat(ligneCommandeClientDTO1).isNotEqualTo(ligneCommandeClientDTO2);
        ligneCommandeClientDTO1.setId(null);
        assertThat(ligneCommandeClientDTO1).isNotEqualTo(ligneCommandeClientDTO2);
    }
}
