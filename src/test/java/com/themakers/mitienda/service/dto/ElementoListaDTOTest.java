package com.themakers.mitienda.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.themakers.mitienda.web.rest.TestUtil;

public class ElementoListaDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ElementoListaDTO.class);
        ElementoListaDTO elementoListaDTO1 = new ElementoListaDTO();
        elementoListaDTO1.setId(1L);
        ElementoListaDTO elementoListaDTO2 = new ElementoListaDTO();
        assertThat(elementoListaDTO1).isNotEqualTo(elementoListaDTO2);
        elementoListaDTO2.setId(elementoListaDTO1.getId());
        assertThat(elementoListaDTO1).isEqualTo(elementoListaDTO2);
        elementoListaDTO2.setId(2L);
        assertThat(elementoListaDTO1).isNotEqualTo(elementoListaDTO2);
        elementoListaDTO1.setId(null);
        assertThat(elementoListaDTO1).isNotEqualTo(elementoListaDTO2);
    }
}
