package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MvtStkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MvtStk.class);
        MvtStk mvtStk1 = new MvtStk();
        mvtStk1.setId(1L);
        MvtStk mvtStk2 = new MvtStk();
        mvtStk2.setId(mvtStk1.getId());
        assertThat(mvtStk1).isEqualTo(mvtStk2);
        mvtStk2.setId(2L);
        assertThat(mvtStk1).isNotEqualTo(mvtStk2);
        mvtStk1.setId(null);
        assertThat(mvtStk1).isNotEqualTo(mvtStk2);
    }
}
