package com.themakers.mitienda.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ElementoListaMapperTest {

    private ElementoListaMapper elementoListaMapper;

    @BeforeEach
    public void setUp() {
        elementoListaMapper = new ElementoListaMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(elementoListaMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(elementoListaMapper.fromId(null)).isNull();
    }
}
