package com.themakers.mitienda.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.themakers.mitienda.web.rest.TestUtil;

public class TipoListaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoLista.class);
        TipoLista tipoLista1 = new TipoLista();
        tipoLista1.setId(1L);
        TipoLista tipoLista2 = new TipoLista();
        tipoLista2.setId(tipoLista1.getId());
        assertThat(tipoLista1).isEqualTo(tipoLista2);
        tipoLista2.setId(2L);
        assertThat(tipoLista1).isNotEqualTo(tipoLista2);
        tipoLista1.setId(null);
        assertThat(tipoLista1).isNotEqualTo(tipoLista2);
    }
}
