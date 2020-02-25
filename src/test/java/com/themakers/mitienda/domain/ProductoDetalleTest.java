package com.themakers.mitienda.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.themakers.mitienda.web.rest.TestUtil;

public class ProductoDetalleTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductoDetalle.class);
        ProductoDetalle productoDetalle1 = new ProductoDetalle();
        productoDetalle1.setId(1L);
        ProductoDetalle productoDetalle2 = new ProductoDetalle();
        productoDetalle2.setId(productoDetalle1.getId());
        assertThat(productoDetalle1).isEqualTo(productoDetalle2);
        productoDetalle2.setId(2L);
        assertThat(productoDetalle1).isNotEqualTo(productoDetalle2);
        productoDetalle1.setId(null);
        assertThat(productoDetalle1).isNotEqualTo(productoDetalle2);
    }
}
