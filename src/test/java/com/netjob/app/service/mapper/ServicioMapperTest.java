package com.netjob.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServicioMapperTest {

    private ServicioMapper servicioMapper;

    @BeforeEach
    public void setUp() {
        servicioMapper = new ServicioMapperImpl();
    }
}
