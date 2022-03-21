package com.netjob.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConversacionMapperTest {

    private ConversacionMapper conversacionMapper;

    @BeforeEach
    public void setUp() {
        conversacionMapper = new ConversacionMapperImpl();
    }
}
