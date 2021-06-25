package com.dbs.nlu.voicebot.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dbs.nlu.voicebot.IntegrationTest;
import com.dbs.nlu.voicebot.domain.Transcripts;
import com.dbs.nlu.voicebot.repository.TranscriptsRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TranscriptsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TranscriptsResourceIT {

    private static final String DEFAULT_UTTERANCE = "AAAAAAAAAA";
    private static final String UPDATED_UTTERANCE = "BBBBBBBBBB";

    private static final String DEFAULT_INTENT = "AAAAAAAAAA";
    private static final String UPDATED_INTENT = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIDENCE = "AAAAAAAAAA";
    private static final String UPDATED_CONFIDENCE = "BBBBBBBBBB";

    private static final String DEFAULT_GC_RESPONSE = "AAAAAAAAAA";
    private static final String UPDATED_GC_RESPONSE = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITY_RESPONSE = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_RESPONSE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/transcripts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TranscriptsRepository transcriptsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTranscriptsMockMvc;

    private Transcripts transcripts;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transcripts createEntity(EntityManager em) {
        Transcripts transcripts = new Transcripts()
            .utterance(DEFAULT_UTTERANCE)
            .intent(DEFAULT_INTENT)
            .confidence(DEFAULT_CONFIDENCE)
            .gcResponse(DEFAULT_GC_RESPONSE)
            .entityResponse(DEFAULT_ENTITY_RESPONSE);
        return transcripts;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transcripts createUpdatedEntity(EntityManager em) {
        Transcripts transcripts = new Transcripts()
            .utterance(UPDATED_UTTERANCE)
            .intent(UPDATED_INTENT)
            .confidence(UPDATED_CONFIDENCE)
            .gcResponse(UPDATED_GC_RESPONSE)
            .entityResponse(UPDATED_ENTITY_RESPONSE);
        return transcripts;
    }

    @BeforeEach
    public void initTest() {
        transcripts = createEntity(em);
    }

    @Test
    @Transactional
    void createTranscripts() throws Exception {
        int databaseSizeBeforeCreate = transcriptsRepository.findAll().size();
        // Create the Transcripts
        restTranscriptsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transcripts)))
            .andExpect(status().isCreated());

        // Validate the Transcripts in the database
        List<Transcripts> transcriptsList = transcriptsRepository.findAll();
        assertThat(transcriptsList).hasSize(databaseSizeBeforeCreate + 1);
        Transcripts testTranscripts = transcriptsList.get(transcriptsList.size() - 1);
        assertThat(testTranscripts.getUtterance()).isEqualTo(DEFAULT_UTTERANCE);
        assertThat(testTranscripts.getIntent()).isEqualTo(DEFAULT_INTENT);
        assertThat(testTranscripts.getConfidence()).isEqualTo(DEFAULT_CONFIDENCE);
        assertThat(testTranscripts.getGcResponse()).isEqualTo(DEFAULT_GC_RESPONSE);
        assertThat(testTranscripts.getEntityResponse()).isEqualTo(DEFAULT_ENTITY_RESPONSE);
    }

    @Test
    @Transactional
    void createTranscriptsWithExistingId() throws Exception {
        // Create the Transcripts with an existing ID
        transcripts.setId(1L);

        int databaseSizeBeforeCreate = transcriptsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTranscriptsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transcripts)))
            .andExpect(status().isBadRequest());

        // Validate the Transcripts in the database
        List<Transcripts> transcriptsList = transcriptsRepository.findAll();
        assertThat(transcriptsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTranscripts() throws Exception {
        // Initialize the database
        transcriptsRepository.saveAndFlush(transcripts);

        // Get all the transcriptsList
        restTranscriptsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transcripts.getId().intValue())))
            .andExpect(jsonPath("$.[*].utterance").value(hasItem(DEFAULT_UTTERANCE)))
            .andExpect(jsonPath("$.[*].intent").value(hasItem(DEFAULT_INTENT)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].gcResponse").value(hasItem(DEFAULT_GC_RESPONSE)))
            .andExpect(jsonPath("$.[*].entityResponse").value(hasItem(DEFAULT_ENTITY_RESPONSE)));
    }

    @Test
    @Transactional
    void getTranscripts() throws Exception {
        // Initialize the database
        transcriptsRepository.saveAndFlush(transcripts);

        // Get the transcripts
        restTranscriptsMockMvc
            .perform(get(ENTITY_API_URL_ID, transcripts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transcripts.getId().intValue()))
            .andExpect(jsonPath("$.utterance").value(DEFAULT_UTTERANCE))
            .andExpect(jsonPath("$.intent").value(DEFAULT_INTENT))
            .andExpect(jsonPath("$.confidence").value(DEFAULT_CONFIDENCE))
            .andExpect(jsonPath("$.gcResponse").value(DEFAULT_GC_RESPONSE))
            .andExpect(jsonPath("$.entityResponse").value(DEFAULT_ENTITY_RESPONSE));
    }

    @Test
    @Transactional
    void getNonExistingTranscripts() throws Exception {
        // Get the transcripts
        restTranscriptsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTranscripts() throws Exception {
        // Initialize the database
        transcriptsRepository.saveAndFlush(transcripts);

        int databaseSizeBeforeUpdate = transcriptsRepository.findAll().size();

        // Update the transcripts
        Transcripts updatedTranscripts = transcriptsRepository.findById(transcripts.getId()).get();
        // Disconnect from session so that the updates on updatedTranscripts are not directly saved in db
        em.detach(updatedTranscripts);
        updatedTranscripts
            .utterance(UPDATED_UTTERANCE)
            .intent(UPDATED_INTENT)
            .confidence(UPDATED_CONFIDENCE)
            .gcResponse(UPDATED_GC_RESPONSE)
            .entityResponse(UPDATED_ENTITY_RESPONSE);

        restTranscriptsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTranscripts.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTranscripts))
            )
            .andExpect(status().isOk());

        // Validate the Transcripts in the database
        List<Transcripts> transcriptsList = transcriptsRepository.findAll();
        assertThat(transcriptsList).hasSize(databaseSizeBeforeUpdate);
        Transcripts testTranscripts = transcriptsList.get(transcriptsList.size() - 1);
        assertThat(testTranscripts.getUtterance()).isEqualTo(UPDATED_UTTERANCE);
        assertThat(testTranscripts.getIntent()).isEqualTo(UPDATED_INTENT);
        assertThat(testTranscripts.getConfidence()).isEqualTo(UPDATED_CONFIDENCE);
        assertThat(testTranscripts.getGcResponse()).isEqualTo(UPDATED_GC_RESPONSE);
        assertThat(testTranscripts.getEntityResponse()).isEqualTo(UPDATED_ENTITY_RESPONSE);
    }

    @Test
    @Transactional
    void putNonExistingTranscripts() throws Exception {
        int databaseSizeBeforeUpdate = transcriptsRepository.findAll().size();
        transcripts.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTranscriptsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transcripts.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transcripts))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transcripts in the database
        List<Transcripts> transcriptsList = transcriptsRepository.findAll();
        assertThat(transcriptsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTranscripts() throws Exception {
        int databaseSizeBeforeUpdate = transcriptsRepository.findAll().size();
        transcripts.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTranscriptsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transcripts))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transcripts in the database
        List<Transcripts> transcriptsList = transcriptsRepository.findAll();
        assertThat(transcriptsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTranscripts() throws Exception {
        int databaseSizeBeforeUpdate = transcriptsRepository.findAll().size();
        transcripts.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTranscriptsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transcripts)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transcripts in the database
        List<Transcripts> transcriptsList = transcriptsRepository.findAll();
        assertThat(transcriptsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTranscriptsWithPatch() throws Exception {
        // Initialize the database
        transcriptsRepository.saveAndFlush(transcripts);

        int databaseSizeBeforeUpdate = transcriptsRepository.findAll().size();

        // Update the transcripts using partial update
        Transcripts partialUpdatedTranscripts = new Transcripts();
        partialUpdatedTranscripts.setId(transcripts.getId());

        partialUpdatedTranscripts.intent(UPDATED_INTENT).confidence(UPDATED_CONFIDENCE).gcResponse(UPDATED_GC_RESPONSE);

        restTranscriptsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTranscripts.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTranscripts))
            )
            .andExpect(status().isOk());

        // Validate the Transcripts in the database
        List<Transcripts> transcriptsList = transcriptsRepository.findAll();
        assertThat(transcriptsList).hasSize(databaseSizeBeforeUpdate);
        Transcripts testTranscripts = transcriptsList.get(transcriptsList.size() - 1);
        assertThat(testTranscripts.getUtterance()).isEqualTo(DEFAULT_UTTERANCE);
        assertThat(testTranscripts.getIntent()).isEqualTo(UPDATED_INTENT);
        assertThat(testTranscripts.getConfidence()).isEqualTo(UPDATED_CONFIDENCE);
        assertThat(testTranscripts.getGcResponse()).isEqualTo(UPDATED_GC_RESPONSE);
        assertThat(testTranscripts.getEntityResponse()).isEqualTo(DEFAULT_ENTITY_RESPONSE);
    }

    @Test
    @Transactional
    void fullUpdateTranscriptsWithPatch() throws Exception {
        // Initialize the database
        transcriptsRepository.saveAndFlush(transcripts);

        int databaseSizeBeforeUpdate = transcriptsRepository.findAll().size();

        // Update the transcripts using partial update
        Transcripts partialUpdatedTranscripts = new Transcripts();
        partialUpdatedTranscripts.setId(transcripts.getId());

        partialUpdatedTranscripts
            .utterance(UPDATED_UTTERANCE)
            .intent(UPDATED_INTENT)
            .confidence(UPDATED_CONFIDENCE)
            .gcResponse(UPDATED_GC_RESPONSE)
            .entityResponse(UPDATED_ENTITY_RESPONSE);

        restTranscriptsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTranscripts.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTranscripts))
            )
            .andExpect(status().isOk());

        // Validate the Transcripts in the database
        List<Transcripts> transcriptsList = transcriptsRepository.findAll();
        assertThat(transcriptsList).hasSize(databaseSizeBeforeUpdate);
        Transcripts testTranscripts = transcriptsList.get(transcriptsList.size() - 1);
        assertThat(testTranscripts.getUtterance()).isEqualTo(UPDATED_UTTERANCE);
        assertThat(testTranscripts.getIntent()).isEqualTo(UPDATED_INTENT);
        assertThat(testTranscripts.getConfidence()).isEqualTo(UPDATED_CONFIDENCE);
        assertThat(testTranscripts.getGcResponse()).isEqualTo(UPDATED_GC_RESPONSE);
        assertThat(testTranscripts.getEntityResponse()).isEqualTo(UPDATED_ENTITY_RESPONSE);
    }

    @Test
    @Transactional
    void patchNonExistingTranscripts() throws Exception {
        int databaseSizeBeforeUpdate = transcriptsRepository.findAll().size();
        transcripts.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTranscriptsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transcripts.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transcripts))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transcripts in the database
        List<Transcripts> transcriptsList = transcriptsRepository.findAll();
        assertThat(transcriptsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTranscripts() throws Exception {
        int databaseSizeBeforeUpdate = transcriptsRepository.findAll().size();
        transcripts.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTranscriptsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transcripts))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transcripts in the database
        List<Transcripts> transcriptsList = transcriptsRepository.findAll();
        assertThat(transcriptsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTranscripts() throws Exception {
        int databaseSizeBeforeUpdate = transcriptsRepository.findAll().size();
        transcripts.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTranscriptsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(transcripts))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transcripts in the database
        List<Transcripts> transcriptsList = transcriptsRepository.findAll();
        assertThat(transcriptsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTranscripts() throws Exception {
        // Initialize the database
        transcriptsRepository.saveAndFlush(transcripts);

        int databaseSizeBeforeDelete = transcriptsRepository.findAll().size();

        // Delete the transcripts
        restTranscriptsMockMvc
            .perform(delete(ENTITY_API_URL_ID, transcripts.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Transcripts> transcriptsList = transcriptsRepository.findAll();
        assertThat(transcriptsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
