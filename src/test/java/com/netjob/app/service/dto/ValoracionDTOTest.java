package com.netjob.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.netjob.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ValoracionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ValoracionDTO.class);
        ValoracionDTO valoracionDTO1 = new ValoracionDTO();
        valoracionDTO1.setId(1L);
        ValoracionDTO valoracionDTO2 = new ValoracionDTO();
        assertThat(valoracionDTO1).isNotEqualTo(valoracionDTO2);
        valoracionDTO2.setId(valoracionDTO1.getId());
        assertThat(valoracionDTO1).isEqualTo(valoracionDTO2);
        valoracionDTO2.setId(2L);
        assertThat(valoracionDTO1).isNotEqualTo(valoracionDTO2);
        valoracionDTO1.setId(null);
        assertThat(valoracionDTO1).isNotEqualTo(valoracionDTO2);
    }
}
