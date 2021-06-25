package com.dbs.nlu.voicebot.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.dbs.nlu.voicebot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NEREntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NEREntity.class);
        NEREntity nEREntity1 = new NEREntity();
        nEREntity1.setId(1L);
        NEREntity nEREntity2 = new NEREntity();
        nEREntity2.setId(nEREntity1.getId());
        assertThat(nEREntity1).isEqualTo(nEREntity2);
        nEREntity2.setId(2L);
        assertThat(nEREntity1).isNotEqualTo(nEREntity2);
        nEREntity1.setId(null);
        assertThat(nEREntity1).isNotEqualTo(nEREntity2);
    }
}
