package com.themakers.mitienda.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ProductoCategoriaMapperTest {

    private ProductoCategoriaMapper productoCategoriaMapper;

    @BeforeEach
    public void setUp() {
        productoCategoriaMapper = new ProductoCategoriaMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(productoCategoriaMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(productoCategoriaMapper.fromId(null)).isNull();
    }
}
