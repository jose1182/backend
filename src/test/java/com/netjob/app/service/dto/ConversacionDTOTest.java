package com.netjob.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.netjob.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConversacionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConversacionDTO.class);
        ConversacionDTO conversacionDTO1 = new ConversacionDTO();
        conversacionDTO1.setId(1L);
        ConversacionDTO conversacionDTO2 = new ConversacionDTO();
        assertThat(conversacionDTO1).isNotEqualTo(conversacionDTO2);
        conversacionDTO2.setId(conversacionDTO1.getId());
        assertThat(conversacionDTO1).isEqualTo(conversacionDTO2);
        conversacionDTO2.setId(2L);
        assertThat(conversacionDTO1).isNotEqualTo(conversacionDTO2);
        conversacionDTO1.setId(null);
        assertThat(conversacionDTO1).isNotEqualTo(conversacionDTO2);
    }
}
