package com.dbs.nlu.voicebot.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dbs.nlu.voicebot.IntegrationTest;
import com.dbs.nlu.voicebot.domain.Intent;
import com.dbs.nlu.voicebot.repository.IntentRepository;
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
 * Integration tests for the {@link IntentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IntentResourceIT {

    private static final String DEFAULT_INTENT = "AAAAAAAAAA";
    private static final String UPDATED_INTENT = "BBBBBBBBBB";

    private static final String DEFAULT_INTENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_INTENT_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/intents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IntentRepository intentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIntentMockMvc;

    private Intent intent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intent createEntity(EntityManager em) {
        Intent intent = new Intent().intent(DEFAULT_INTENT).intentName(DEFAULT_INTENT_NAME);
        return intent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intent createUpdatedEntity(EntityManager em) {
        Intent intent = new Intent().intent(UPDATED_INTENT).intentName(UPDATED_INTENT_NAME);
        return intent;
    }

    @BeforeEach
    public void initTest() {
        intent = createEntity(em);
    }

    @Test
    @Transactional
    void createIntent() throws Exception {
        int databaseSizeBeforeCreate = intentRepository.findAll().size();
        // Create the Intent
        restIntentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(intent)))
            .andExpect(status().isCreated());

        // Validate the Intent in the database
        List<Intent> intentList = intentRepository.findAll();
        assertThat(intentList).hasSize(databaseSizeBeforeCreate + 1);
        Intent testIntent = intentList.get(intentList.size() - 1);
        assertThat(testIntent.getIntent()).isEqualTo(DEFAULT_INTENT);
        assertThat(testIntent.getIntentName()).isEqualTo(DEFAULT_INTENT_NAME);
    }

    @Test
    @Transactional
    void createIntentWithExistingId() throws Exception {
        // Create the Intent with an existing ID
        intent.setId(1L);

        int databaseSizeBeforeCreate = intentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIntentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(intent)))
            .andExpect(status().isBadRequest());

        // Validate the Intent in the database
        List<Intent> intentList = intentRepository.findAll();
        assertThat(intentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIntents() throws Exception {
        // Initialize the database
        intentRepository.saveAndFlush(intent);

        // Get all the intentList
        restIntentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(intent.getId().intValue())))
            .andExpect(jsonPath("$.[*].intent").value(hasItem(DEFAULT_INTENT)))
            .andExpect(jsonPath("$.[*].intentName").value(hasItem(DEFAULT_INTENT_NAME)));
    }

    @Test
    @Transactional
    void getIntent() throws Exception {
        // Initialize the database
        intentRepository.saveAndFlush(intent);

        // Get the intent
        restIntentMockMvc
            .perform(get(ENTITY_API_URL_ID, intent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(intent.getId().intValue()))
            .andExpect(jsonPath("$.intent").value(DEFAULT_INTENT))
            .andExpect(jsonPath("$.intentName").value(DEFAULT_INTENT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingIntent() throws Exception {
        // Get the intent
        restIntentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIntent() throws Exception {
        // Initialize the database
        intentRepository.saveAndFlush(intent);

        int databaseSizeBeforeUpdate = intentRepository.findAll().size();

        // Update the intent
        Intent updatedIntent = intentRepository.findById(intent.getId()).get();
        // Disconnect from session so that the updates on updatedIntent are not directly saved in db
        em.detach(updatedIntent);
        updatedIntent.intent(UPDATED_INTENT).intentName(UPDATED_INTENT_NAME);

        restIntentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIntent.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIntent))
            )
            .andExpect(status().isOk());

        // Validate the Intent in the database
        List<Intent> intentList = intentRepository.findAll();
        assertThat(intentList).hasSize(databaseSizeBeforeUpdate);
        Intent testIntent = intentList.get(intentList.size() - 1);
        assertThat(testIntent.getIntent()).isEqualTo(UPDATED_INTENT);
        assertThat(testIntent.getIntentName()).isEqualTo(UPDATED_INTENT_NAME);
    }

    @Test
    @Transactional
    void putNonExistingIntent() throws Exception {
        int databaseSizeBeforeUpdate = intentRepository.findAll().size();
        intent.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIntentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, intent.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intent))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intent in the database
        List<Intent> intentList = intentRepository.findAll();
        assertThat(intentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIntent() throws Exception {
        int databaseSizeBeforeUpdate = intentRepository.findAll().size();
        intent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(intent))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intent in the database
        List<Intent> intentList = intentRepository.findAll();
        assertThat(intentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIntent() throws Exception {
        int databaseSizeBeforeUpdate = intentRepository.findAll().size();
        intent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(intent)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Intent in the database
        List<Intent> intentList = intentRepository.findAll();
        assertThat(intentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIntentWithPatch() throws Exception {
        // Initialize the database
        intentRepository.saveAndFlush(intent);

        int databaseSizeBeforeUpdate = intentRepository.findAll().size();

        // Update the intent using partial update
        Intent partialUpdatedIntent = new Intent();
        partialUpdatedIntent.setId(intent.getId());

        restIntentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIntent))
            )
            .andExpect(status().isOk());

        // Validate the Intent in the database
        List<Intent> intentList = intentRepository.findAll();
        assertThat(intentList).hasSize(databaseSizeBeforeUpdate);
        Intent testIntent = intentList.get(intentList.size() - 1);
        assertThat(testIntent.getIntent()).isEqualTo(DEFAULT_INTENT);
        assertThat(testIntent.getIntentName()).isEqualTo(DEFAULT_INTENT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateIntentWithPatch() throws Exception {
        // Initialize the database
        intentRepository.saveAndFlush(intent);

        int databaseSizeBeforeUpdate = intentRepository.findAll().size();

        // Update the intent using partial update
        Intent partialUpdatedIntent = new Intent();
        partialUpdatedIntent.setId(intent.getId());

        partialUpdatedIntent.intent(UPDATED_INTENT).intentName(UPDATED_INTENT_NAME);

        restIntentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIntent))
            )
            .andExpect(status().isOk());

        // Validate the Intent in the database
        List<Intent> intentList = intentRepository.findAll();
        assertThat(intentList).hasSize(databaseSizeBeforeUpdate);
        Intent testIntent = intentList.get(intentList.size() - 1);
        assertThat(testIntent.getIntent()).isEqualTo(UPDATED_INTENT);
        assertThat(testIntent.getIntentName()).isEqualTo(UPDATED_INTENT_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingIntent() throws Exception {
        int databaseSizeBeforeUpdate = intentRepository.findAll().size();
        intent.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIntentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, intent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(intent))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intent in the database
        List<Intent> intentList = intentRepository.findAll();
        assertThat(intentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIntent() throws Exception {
        int databaseSizeBeforeUpdate = intentRepository.findAll().size();
        intent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(intent))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intent in the database
        List<Intent> intentList = intentRepository.findAll();
        assertThat(intentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIntent() throws Exception {
        int databaseSizeBeforeUpdate = intentRepository.findAll().size();
        intent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(intent)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Intent in the database
        List<Intent> intentList = intentRepository.findAll();
        assertThat(intentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIntent() throws Exception {
        // Initialize the database
        intentRepository.saveAndFlush(intent);

        int databaseSizeBeforeDelete = intentRepository.findAll().size();

        // Delete the intent
        restIntentMockMvc
            .perform(delete(ENTITY_API_URL_ID, intent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Intent> intentList = intentRepository.findAll();
        assertThat(intentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
