package com.themakers.mitienda.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.themakers.mitienda.web.rest.TestUtil;

public class TipoListaDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoListaDTO.class);
        TipoListaDTO tipoListaDTO1 = new TipoListaDTO();
        tipoListaDTO1.setId(1L);
        TipoListaDTO tipoListaDTO2 = new TipoListaDTO();
        assertThat(tipoListaDTO1).isNotEqualTo(tipoListaDTO2);
        tipoListaDTO2.setId(tipoListaDTO1.getId());
        assertThat(tipoListaDTO1).isEqualTo(tipoListaDTO2);
        tipoListaDTO2.setId(2L);
        assertThat(tipoListaDTO1).isNotEqualTo(tipoListaDTO2);
        tipoListaDTO1.setId(null);
        assertThat(tipoListaDTO1).isNotEqualTo(tipoListaDTO2);
    }
}
