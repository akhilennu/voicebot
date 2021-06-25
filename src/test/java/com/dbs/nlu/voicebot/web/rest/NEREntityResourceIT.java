package com.dbs.nlu.voicebot.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dbs.nlu.voicebot.IntegrationTest;
import com.dbs.nlu.voicebot.domain.NEREntity;
import com.dbs.nlu.voicebot.repository.NEREntityRepository;
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
 * Integration tests for the {@link NEREntityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NEREntityResourceIT {

    private static final String DEFAULT_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_START = 1;
    private static final Integer UPDATED_START = 2;

    private static final Integer DEFAULT_END = 1;
    private static final Integer UPDATED_END = 2;

    private static final String ENTITY_API_URL = "/api/ner-entities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NEREntityRepository nEREntityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNEREntityMockMvc;

    private NEREntity nEREntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NEREntity createEntity(EntityManager em) {
        NEREntity nEREntity = new NEREntity().entityName(DEFAULT_ENTITY_NAME).start(DEFAULT_START).end(DEFAULT_END);
        return nEREntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NEREntity createUpdatedEntity(EntityManager em) {
        NEREntity nEREntity = new NEREntity().entityName(UPDATED_ENTITY_NAME).start(UPDATED_START).end(UPDATED_END);
        return nEREntity;
    }

    @BeforeEach
    public void initTest() {
        nEREntity = createEntity(em);
    }

    @Test
    @Transactional
    void createNEREntity() throws Exception {
        int databaseSizeBeforeCreate = nEREntityRepository.findAll().size();
        // Create the NEREntity
        restNEREntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nEREntity)))
            .andExpect(status().isCreated());

        // Validate the NEREntity in the database
        List<NEREntity> nEREntityList = nEREntityRepository.findAll();
        assertThat(nEREntityList).hasSize(databaseSizeBeforeCreate + 1);
        NEREntity testNEREntity = nEREntityList.get(nEREntityList.size() - 1);
        assertThat(testNEREntity.getEntityName()).isEqualTo(DEFAULT_ENTITY_NAME);
        assertThat(testNEREntity.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testNEREntity.getEnd()).isEqualTo(DEFAULT_END);
    }

    @Test
    @Transactional
    void createNEREntityWithExistingId() throws Exception {
        // Create the NEREntity with an existing ID
        nEREntity.setId(1L);

        int databaseSizeBeforeCreate = nEREntityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNEREntityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nEREntity)))
            .andExpect(status().isBadRequest());

        // Validate the NEREntity in the database
        List<NEREntity> nEREntityList = nEREntityRepository.findAll();
        assertThat(nEREntityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNEREntities() throws Exception {
        // Initialize the database
        nEREntityRepository.saveAndFlush(nEREntity);

        // Get all the nEREntityList
        restNEREntityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nEREntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START)))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END)));
    }

    @Test
    @Transactional
    void getNEREntity() throws Exception {
        // Initialize the database
        nEREntityRepository.saveAndFlush(nEREntity);

        // Get the nEREntity
        restNEREntityMockMvc
            .perform(get(ENTITY_API_URL_ID, nEREntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nEREntity.getId().intValue()))
            .andExpect(jsonPath("$.entityName").value(DEFAULT_ENTITY_NAME))
            .andExpect(jsonPath("$.start").value(DEFAULT_START))
            .andExpect(jsonPath("$.end").value(DEFAULT_END));
    }

    @Test
    @Transactional
    void getNonExistingNEREntity() throws Exception {
        // Get the nEREntity
        restNEREntityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNEREntity() throws Exception {
        // Initialize the database
        nEREntityRepository.saveAndFlush(nEREntity);

        int databaseSizeBeforeUpdate = nEREntityRepository.findAll().size();

        // Update the nEREntity
        NEREntity updatedNEREntity = nEREntityRepository.findById(nEREntity.getId()).get();
        // Disconnect from session so that the updates on updatedNEREntity are not directly saved in db
        em.detach(updatedNEREntity);
        updatedNEREntity.entityName(UPDATED_ENTITY_NAME).start(UPDATED_START).end(UPDATED_END);

        restNEREntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNEREntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedNEREntity))
            )
            .andExpect(status().isOk());

        // Validate the NEREntity in the database
        List<NEREntity> nEREntityList = nEREntityRepository.findAll();
        assertThat(nEREntityList).hasSize(databaseSizeBeforeUpdate);
        NEREntity testNEREntity = nEREntityList.get(nEREntityList.size() - 1);
        assertThat(testNEREntity.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
        assertThat(testNEREntity.getStart()).isEqualTo(UPDATED_START);
        assertThat(testNEREntity.getEnd()).isEqualTo(UPDATED_END);
    }

    @Test
    @Transactional
    void putNonExistingNEREntity() throws Exception {
        int databaseSizeBeforeUpdate = nEREntityRepository.findAll().size();
        nEREntity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNEREntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nEREntity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nEREntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the NEREntity in the database
        List<NEREntity> nEREntityList = nEREntityRepository.findAll();
        assertThat(nEREntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNEREntity() throws Exception {
        int databaseSizeBeforeUpdate = nEREntityRepository.findAll().size();
        nEREntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNEREntityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nEREntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the NEREntity in the database
        List<NEREntity> nEREntityList = nEREntityRepository.findAll();
        assertThat(nEREntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNEREntity() throws Exception {
        int databaseSizeBeforeUpdate = nEREntityRepository.findAll().size();
        nEREntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNEREntityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nEREntity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NEREntity in the database
        List<NEREntity> nEREntityList = nEREntityRepository.findAll();
        assertThat(nEREntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNEREntityWithPatch() throws Exception {
        // Initialize the database
        nEREntityRepository.saveAndFlush(nEREntity);

        int databaseSizeBeforeUpdate = nEREntityRepository.findAll().size();

        // Update the nEREntity using partial update
        NEREntity partialUpdatedNEREntity = new NEREntity();
        partialUpdatedNEREntity.setId(nEREntity.getId());

        partialUpdatedNEREntity.start(UPDATED_START).end(UPDATED_END);

        restNEREntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNEREntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNEREntity))
            )
            .andExpect(status().isOk());

        // Validate the NEREntity in the database
        List<NEREntity> nEREntityList = nEREntityRepository.findAll();
        assertThat(nEREntityList).hasSize(databaseSizeBeforeUpdate);
        NEREntity testNEREntity = nEREntityList.get(nEREntityList.size() - 1);
        assertThat(testNEREntity.getEntityName()).isEqualTo(DEFAULT_ENTITY_NAME);
        assertThat(testNEREntity.getStart()).isEqualTo(UPDATED_START);
        assertThat(testNEREntity.getEnd()).isEqualTo(UPDATED_END);
    }

    @Test
    @Transactional
    void fullUpdateNEREntityWithPatch() throws Exception {
        // Initialize the database
        nEREntityRepository.saveAndFlush(nEREntity);

        int databaseSizeBeforeUpdate = nEREntityRepository.findAll().size();

        // Update the nEREntity using partial update
        NEREntity partialUpdatedNEREntity = new NEREntity();
        partialUpdatedNEREntity.setId(nEREntity.getId());

        partialUpdatedNEREntity.entityName(UPDATED_ENTITY_NAME).start(UPDATED_START).end(UPDATED_END);

        restNEREntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNEREntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNEREntity))
            )
            .andExpect(status().isOk());

        // Validate the NEREntity in the database
        List<NEREntity> nEREntityList = nEREntityRepository.findAll();
        assertThat(nEREntityList).hasSize(databaseSizeBeforeUpdate);
        NEREntity testNEREntity = nEREntityList.get(nEREntityList.size() - 1);
        assertThat(testNEREntity.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
        assertThat(testNEREntity.getStart()).isEqualTo(UPDATED_START);
        assertThat(testNEREntity.getEnd()).isEqualTo(UPDATED_END);
    }

    @Test
    @Transactional
    void patchNonExistingNEREntity() throws Exception {
        int databaseSizeBeforeUpdate = nEREntityRepository.findAll().size();
        nEREntity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNEREntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nEREntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nEREntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the NEREntity in the database
        List<NEREntity> nEREntityList = nEREntityRepository.findAll();
        assertThat(nEREntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNEREntity() throws Exception {
        int databaseSizeBeforeUpdate = nEREntityRepository.findAll().size();
        nEREntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNEREntityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nEREntity))
            )
            .andExpect(status().isBadRequest());

        // Validate the NEREntity in the database
        List<NEREntity> nEREntityList = nEREntityRepository.findAll();
        assertThat(nEREntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNEREntity() throws Exception {
        int databaseSizeBeforeUpdate = nEREntityRepository.findAll().size();
        nEREntity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNEREntityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(nEREntity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NEREntity in the database
        List<NEREntity> nEREntityList = nEREntityRepository.findAll();
        assertThat(nEREntityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNEREntity() throws Exception {
        // Initialize the database
        nEREntityRepository.saveAndFlush(nEREntity);

        int databaseSizeBeforeDelete = nEREntityRepository.findAll().size();

        // Delete the nEREntity
        restNEREntityMockMvc
            .perform(delete(ENTITY_API_URL_ID, nEREntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NEREntity> nEREntityList = nEREntityRepository.findAll();
        assertThat(nEREntityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
