package com.netjob.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.netjob.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ServicioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Servicio.class);
        Servicio servicio1 = new Servicio();
        servicio1.setId(1L);
        Servicio servicio2 = new Servicio();
        servicio2.setId(servicio1.getId());
        assertThat(servicio1).isEqualTo(servicio2);
        servicio2.setId(2L);
        assertThat(servicio1).isNotEqualTo(servicio2);
        servicio1.setId(null);
        assertThat(servicio1).isNotEqualTo(servicio2);
    }
}
