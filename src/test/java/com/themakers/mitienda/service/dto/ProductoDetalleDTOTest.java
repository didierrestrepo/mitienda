package com.themakers.mitienda.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.themakers.mitienda.web.rest.TestUtil;

public class ProductoDetalleDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductoDetalleDTO.class);
        ProductoDetalleDTO productoDetalleDTO1 = new ProductoDetalleDTO();
        productoDetalleDTO1.setId(1L);
        ProductoDetalleDTO productoDetalleDTO2 = new ProductoDetalleDTO();
        assertThat(productoDetalleDTO1).isNotEqualTo(productoDetalleDTO2);
        productoDetalleDTO2.setId(productoDetalleDTO1.getId());
        assertThat(productoDetalleDTO1).isEqualTo(productoDetalleDTO2);
        productoDetalleDTO2.setId(2L);
        assertThat(productoDetalleDTO1).isNotEqualTo(productoDetalleDTO2);
        productoDetalleDTO1.setId(null);
        assertThat(productoDetalleDTO1).isNotEqualTo(productoDetalleDTO2);
    }
}
