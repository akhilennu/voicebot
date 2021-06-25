package com.dbs.nlu.voicebot.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dbs.nlu.voicebot.IntegrationTest;
import com.dbs.nlu.voicebot.domain.Utterance;
import com.dbs.nlu.voicebot.repository.UtteranceRepository;
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
 * Integration tests for the {@link UtteranceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UtteranceResourceIT {

    private static final String DEFAULT_UTTERANCE = "AAAAAAAAAA";
    private static final String UPDATED_UTTERANCE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/utterances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UtteranceRepository utteranceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUtteranceMockMvc;

    private Utterance utterance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utterance createEntity(EntityManager em) {
        Utterance utterance = new Utterance().utterance(DEFAULT_UTTERANCE);
        return utterance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utterance createUpdatedEntity(EntityManager em) {
        Utterance utterance = new Utterance().utterance(UPDATED_UTTERANCE);
        return utterance;
    }

    @BeforeEach
    public void initTest() {
        utterance = createEntity(em);
    }

    @Test
    @Transactional
    void createUtterance() throws Exception {
        int databaseSizeBeforeCreate = utteranceRepository.findAll().size();
        // Create the Utterance
        restUtteranceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utterance)))
            .andExpect(status().isCreated());

        // Validate the Utterance in the database
        List<Utterance> utteranceList = utteranceRepository.findAll();
        assertThat(utteranceList).hasSize(databaseSizeBeforeCreate + 1);
        Utterance testUtterance = utteranceList.get(utteranceList.size() - 1);
        assertThat(testUtterance.getUtterance()).isEqualTo(DEFAULT_UTTERANCE);
    }

    @Test
    @Transactional
    void createUtteranceWithExistingId() throws Exception {
        // Create the Utterance with an existing ID
        utterance.setId(1L);

        int databaseSizeBeforeCreate = utteranceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUtteranceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utterance)))
            .andExpect(status().isBadRequest());

        // Validate the Utterance in the database
        List<Utterance> utteranceList = utteranceRepository.findAll();
        assertThat(utteranceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUtterances() throws Exception {
        // Initialize the database
        utteranceRepository.saveAndFlush(utterance);

        // Get all the utteranceList
        restUtteranceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utterance.getId().intValue())))
            .andExpect(jsonPath("$.[*].utterance").value(hasItem(DEFAULT_UTTERANCE)));
    }

    @Test
    @Transactional
    void getUtterance() throws Exception {
        // Initialize the database
        utteranceRepository.saveAndFlush(utterance);

        // Get the utterance
        restUtteranceMockMvc
            .perform(get(ENTITY_API_URL_ID, utterance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(utterance.getId().intValue()))
            .andExpect(jsonPath("$.utterance").value(DEFAULT_UTTERANCE));
    }

    @Test
    @Transactional
    void getNonExistingUtterance() throws Exception {
        // Get the utterance
        restUtteranceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUtterance() throws Exception {
        // Initialize the database
        utteranceRepository.saveAndFlush(utterance);

        int databaseSizeBeforeUpdate = utteranceRepository.findAll().size();

        // Update the utterance
        Utterance updatedUtterance = utteranceRepository.findById(utterance.getId()).get();
        // Disconnect from session so that the updates on updatedUtterance are not directly saved in db
        em.detach(updatedUtterance);
        updatedUtterance.utterance(UPDATED_UTTERANCE);

        restUtteranceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUtterance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUtterance))
            )
            .andExpect(status().isOk());

        // Validate the Utterance in the database
        List<Utterance> utteranceList = utteranceRepository.findAll();
        assertThat(utteranceList).hasSize(databaseSizeBeforeUpdate);
        Utterance testUtterance = utteranceList.get(utteranceList.size() - 1);
        assertThat(testUtterance.getUtterance()).isEqualTo(UPDATED_UTTERANCE);
    }

    @Test
    @Transactional
    void putNonExistingUtterance() throws Exception {
        int databaseSizeBeforeUpdate = utteranceRepository.findAll().size();
        utterance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtteranceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utterance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utterance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utterance in the database
        List<Utterance> utteranceList = utteranceRepository.findAll();
        assertThat(utteranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUtterance() throws Exception {
        int databaseSizeBeforeUpdate = utteranceRepository.findAll().size();
        utterance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtteranceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utterance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utterance in the database
        List<Utterance> utteranceList = utteranceRepository.findAll();
        assertThat(utteranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUtterance() throws Exception {
        int databaseSizeBeforeUpdate = utteranceRepository.findAll().size();
        utterance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtteranceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utterance)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Utterance in the database
        List<Utterance> utteranceList = utteranceRepository.findAll();
        assertThat(utteranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUtteranceWithPatch() throws Exception {
        // Initialize the database
        utteranceRepository.saveAndFlush(utterance);

        int databaseSizeBeforeUpdate = utteranceRepository.findAll().size();

        // Update the utterance using partial update
        Utterance partialUpdatedUtterance = new Utterance();
        partialUpdatedUtterance.setId(utterance.getId());

        restUtteranceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtterance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUtterance))
            )
            .andExpect(status().isOk());

        // Validate the Utterance in the database
        List<Utterance> utteranceList = utteranceRepository.findAll();
        assertThat(utteranceList).hasSize(databaseSizeBeforeUpdate);
        Utterance testUtterance = utteranceList.get(utteranceList.size() - 1);
        assertThat(testUtterance.getUtterance()).isEqualTo(DEFAULT_UTTERANCE);
    }

    @Test
    @Transactional
    void fullUpdateUtteranceWithPatch() throws Exception {
        // Initialize the database
        utteranceRepository.saveAndFlush(utterance);

        int databaseSizeBeforeUpdate = utteranceRepository.findAll().size();

        // Update the utterance using partial update
        Utterance partialUpdatedUtterance = new Utterance();
        partialUpdatedUtterance.setId(utterance.getId());

        partialUpdatedUtterance.utterance(UPDATED_UTTERANCE);

        restUtteranceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtterance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUtterance))
            )
            .andExpect(status().isOk());

        // Validate the Utterance in the database
        List<Utterance> utteranceList = utteranceRepository.findAll();
        assertThat(utteranceList).hasSize(databaseSizeBeforeUpdate);
        Utterance testUtterance = utteranceList.get(utteranceList.size() - 1);
        assertThat(testUtterance.getUtterance()).isEqualTo(UPDATED_UTTERANCE);
    }

    @Test
    @Transactional
    void patchNonExistingUtterance() throws Exception {
        int databaseSizeBeforeUpdate = utteranceRepository.findAll().size();
        utterance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtteranceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, utterance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(utterance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utterance in the database
        List<Utterance> utteranceList = utteranceRepository.findAll();
        assertThat(utteranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUtterance() throws Exception {
        int databaseSizeBeforeUpdate = utteranceRepository.findAll().size();
        utterance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtteranceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(utterance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utterance in the database
        List<Utterance> utteranceList = utteranceRepository.findAll();
        assertThat(utteranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUtterance() throws Exception {
        int databaseSizeBeforeUpdate = utteranceRepository.findAll().size();
        utterance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtteranceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(utterance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Utterance in the database
        List<Utterance> utteranceList = utteranceRepository.findAll();
        assertThat(utteranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUtterance() throws Exception {
        // Initialize the database
        utteranceRepository.saveAndFlush(utterance);

        int databaseSizeBeforeDelete = utteranceRepository.findAll().size();

        // Delete the utterance
        restUtteranceMockMvc
            .perform(delete(ENTITY_API_URL_ID, utterance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Utterance> utteranceList = utteranceRepository.findAll();
        assertThat(utteranceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
