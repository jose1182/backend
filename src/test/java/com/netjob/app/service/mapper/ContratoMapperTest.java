package com.netjob.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContratoMapperTest {

    private ContratoMapper contratoMapper;

    @BeforeEach
    public void setUp() {
        contratoMapper = new ContratoMapperImpl();
    }
}
