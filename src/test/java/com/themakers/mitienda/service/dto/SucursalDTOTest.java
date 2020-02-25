package com.themakers.mitienda.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.themakers.mitienda.web.rest.TestUtil;

public class SucursalDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SucursalDTO.class);
        SucursalDTO sucursalDTO1 = new SucursalDTO();
        sucursalDTO1.setId(1L);
        SucursalDTO sucursalDTO2 = new SucursalDTO();
        assertThat(sucursalDTO1).isNotEqualTo(sucursalDTO2);
        sucursalDTO2.setId(sucursalDTO1.getId());
        assertThat(sucursalDTO1).isEqualTo(sucursalDTO2);
        sucursalDTO2.setId(2L);
        assertThat(sucursalDTO1).isNotEqualTo(sucursalDTO2);
        sucursalDTO1.setId(null);
        assertThat(sucursalDTO1).isNotEqualTo(sucursalDTO2);
    }
}
