package com.netjob.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.netjob.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MensajeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MensajeDTO.class);
        MensajeDTO mensajeDTO1 = new MensajeDTO();
        mensajeDTO1.setId(1L);
        MensajeDTO mensajeDTO2 = new MensajeDTO();
        assertThat(mensajeDTO1).isNotEqualTo(mensajeDTO2);
        mensajeDTO2.setId(mensajeDTO1.getId());
        assertThat(mensajeDTO1).isEqualTo(mensajeDTO2);
        mensajeDTO2.setId(2L);
        assertThat(mensajeDTO1).isNotEqualTo(mensajeDTO2);
        mensajeDTO1.setId(null);
        assertThat(mensajeDTO1).isNotEqualTo(mensajeDTO2);
    }
}
