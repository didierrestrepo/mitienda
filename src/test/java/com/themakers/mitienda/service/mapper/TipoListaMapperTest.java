package com.themakers.mitienda.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class TipoListaMapperTest {

    private TipoListaMapper tipoListaMapper;

    @BeforeEach
    public void setUp() {
        tipoListaMapper = new TipoListaMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(tipoListaMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(tipoListaMapper.fromId(null)).isNull();
    }
}
