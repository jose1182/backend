package com.netjob.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValoracionMapperTest {

    private ValoracionMapper valoracionMapper;

    @BeforeEach
    public void setUp() {
        valoracionMapper = new ValoracionMapperImpl();
    }
}
