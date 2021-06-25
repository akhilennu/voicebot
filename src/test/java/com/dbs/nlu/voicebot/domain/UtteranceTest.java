package com.dbs.nlu.voicebot.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.dbs.nlu.voicebot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UtteranceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Utterance.class);
        Utterance utterance1 = new Utterance();
        utterance1.setId(1L);
        Utterance utterance2 = new Utterance();
        utterance2.setId(utterance1.getId());
        assertThat(utterance1).isEqualTo(utterance2);
        utterance2.setId(2L);
        assertThat(utterance1).isNotEqualTo(utterance2);
        utterance1.setId(null);
        assertThat(utterance1).isNotEqualTo(utterance2);
    }
}
