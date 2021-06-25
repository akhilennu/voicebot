package com.dbs.nlu.voicebot.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.dbs.nlu.voicebot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IntentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Intent.class);
        Intent intent1 = new Intent();
        intent1.setId(1L);
        Intent intent2 = new Intent();
        intent2.setId(intent1.getId());
        assertThat(intent1).isEqualTo(intent2);
        intent2.setId(2L);
        assertThat(intent1).isNotEqualTo(intent2);
        intent1.setId(null);
        assertThat(intent1).isNotEqualTo(intent2);
    }
}
