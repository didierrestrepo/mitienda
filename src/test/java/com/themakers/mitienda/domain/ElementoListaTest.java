package com.themakers.mitienda.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.themakers.mitienda.web.rest.TestUtil;

public class ElementoListaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ElementoLista.class);
        ElementoLista elementoLista1 = new ElementoLista();
        elementoLista1.setId(1L);
        ElementoLista elementoLista2 = new ElementoLista();
        elementoLista2.setId(elementoLista1.getId());
        assertThat(elementoLista1).isEqualTo(elementoLista2);
        elementoLista2.setId(2L);
        assertThat(elementoLista1).isNotEqualTo(elementoLista2);
        elementoLista1.setId(null);
        assertThat(elementoLista1).isNotEqualTo(elementoLista2);
    }
}
