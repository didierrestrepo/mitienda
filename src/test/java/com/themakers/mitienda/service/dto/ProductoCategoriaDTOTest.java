package com.themakers.mitienda.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.themakers.mitienda.web.rest.TestUtil;

public class ProductoCategoriaDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductoCategoriaDTO.class);
        ProductoCategoriaDTO productoCategoriaDTO1 = new ProductoCategoriaDTO();
        productoCategoriaDTO1.setId(1L);
        ProductoCategoriaDTO productoCategoriaDTO2 = new ProductoCategoriaDTO();
        assertThat(productoCategoriaDTO1).isNotEqualTo(productoCategoriaDTO2);
        productoCategoriaDTO2.setId(productoCategoriaDTO1.getId());
        assertThat(productoCategoriaDTO1).isEqualTo(productoCategoriaDTO2);
        productoCategoriaDTO2.setId(2L);
        assertThat(productoCategoriaDTO1).isNotEqualTo(productoCategoriaDTO2);
        productoCategoriaDTO1.setId(null);
        assertThat(productoCategoriaDTO1).isNotEqualTo(productoCategoriaDTO2);
    }
}
