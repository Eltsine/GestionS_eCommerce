package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonnelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonnelDTO.class);
        PersonnelDTO personnelDTO1 = new PersonnelDTO();
        personnelDTO1.setId(1L);
        PersonnelDTO personnelDTO2 = new PersonnelDTO();
        assertThat(personnelDTO1).isNotEqualTo(personnelDTO2);
        personnelDTO2.setId(personnelDTO1.getId());
        assertThat(personnelDTO1).isEqualTo(personnelDTO2);
        personnelDTO2.setId(2L);
        assertThat(personnelDTO1).isNotEqualTo(personnelDTO2);
        personnelDTO1.setId(null);
        assertThat(personnelDTO1).isNotEqualTo(personnelDTO2);
    }
}
