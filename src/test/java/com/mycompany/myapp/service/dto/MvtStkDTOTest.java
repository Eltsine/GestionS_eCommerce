package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MvtStkDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MvtStkDTO.class);
        MvtStkDTO mvtStkDTO1 = new MvtStkDTO();
        mvtStkDTO1.setId(1L);
        MvtStkDTO mvtStkDTO2 = new MvtStkDTO();
        assertThat(mvtStkDTO1).isNotEqualTo(mvtStkDTO2);
        mvtStkDTO2.setId(mvtStkDTO1.getId());
        assertThat(mvtStkDTO1).isEqualTo(mvtStkDTO2);
        mvtStkDTO2.setId(2L);
        assertThat(mvtStkDTO1).isNotEqualTo(mvtStkDTO2);
        mvtStkDTO1.setId(null);
        assertThat(mvtStkDTO1).isNotEqualTo(mvtStkDTO2);
    }
}
