package com.netjob.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.netjob.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConversacionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Conversacion.class);
        Conversacion conversacion1 = new Conversacion();
        conversacion1.setId(1L);
        Conversacion conversacion2 = new Conversacion();
        conversacion2.setId(conversacion1.getId());
        assertThat(conversacion1).isEqualTo(conversacion2);
        conversacion2.setId(2L);
        assertThat(conversacion1).isNotEqualTo(conversacion2);
        conversacion1.setId(null);
        assertThat(conversacion1).isNotEqualTo(conversacion2);
    }
}
