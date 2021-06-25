package com.dbs.nlu.voicebot.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.dbs.nlu.voicebot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TranscriptsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transcripts.class);
        Transcripts transcripts1 = new Transcripts();
        transcripts1.setId(1L);
        Transcripts transcripts2 = new Transcripts();
        transcripts2.setId(transcripts1.getId());
        assertThat(transcripts1).isEqualTo(transcripts2);
        transcripts2.setId(2L);
        assertThat(transcripts1).isNotEqualTo(transcripts2);
        transcripts1.setId(null);
        assertThat(transcripts1).isNotEqualTo(transcripts2);
    }
}
