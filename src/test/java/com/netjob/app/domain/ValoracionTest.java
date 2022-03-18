package com.netjob.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.netjob.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ValoracionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Valoracion.class);
        Valoracion valoracion1 = new Valoracion();
        valoracion1.setId(1L);
        Valoracion valoracion2 = new Valoracion();
        valoracion2.setId(valoracion1.getId());
        assertThat(valoracion1).isEqualTo(valoracion2);
        valoracion2.setId(2L);
        assertThat(valoracion1).isNotEqualTo(valoracion2);
        valoracion1.setId(null);
        assertThat(valoracion1).isNotEqualTo(valoracion2);
    }
}
