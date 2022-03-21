package com.netjob.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.netjob.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FavoritoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FavoritoDTO.class);
        FavoritoDTO favoritoDTO1 = new FavoritoDTO();
        favoritoDTO1.setId(1L);
        FavoritoDTO favoritoDTO2 = new FavoritoDTO();
        assertThat(favoritoDTO1).isNotEqualTo(favoritoDTO2);
        favoritoDTO2.setId(favoritoDTO1.getId());
        assertThat(favoritoDTO1).isEqualTo(favoritoDTO2);
        favoritoDTO2.setId(2L);
        assertThat(favoritoDTO1).isNotEqualTo(favoritoDTO2);
        favoritoDTO1.setId(null);
        assertThat(favoritoDTO1).isNotEqualTo(favoritoDTO2);
    }
}
