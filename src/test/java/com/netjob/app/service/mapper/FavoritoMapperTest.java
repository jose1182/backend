package com.netjob.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FavoritoMapperTest {

    private FavoritoMapper favoritoMapper;

    @BeforeEach
    public void setUp() {
        favoritoMapper = new FavoritoMapperImpl();
    }
}
