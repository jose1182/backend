package com.netjob.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.netjob.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FavoritoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Favorito.class);
        Favorito favorito1 = new Favorito();
        favorito1.setId(1L);
        Favorito favorito2 = new Favorito();
        favorito2.setId(favorito1.getId());
        assertThat(favorito1).isEqualTo(favorito2);
        favorito2.setId(2L);
        assertThat(favorito1).isNotEqualTo(favorito2);
        favorito1.setId(null);
        assertThat(favorito1).isNotEqualTo(favorito2);
    }
}
