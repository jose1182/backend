package com.netjob.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MensajeMapperTest {

    private MensajeMapper mensajeMapper;

    @BeforeEach
    public void setUp() {
        mensajeMapper = new MensajeMapperImpl();
    }
}
