package com.dbs.nlu.voicebot.service;

import com.dbs.nlu.voicebot.domain.NEREntity;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link NEREntity}.
 */
public interface NEREntityService {
    /**
     * Save a nEREntity.
     *
     * @param nEREntity the entity to save.
     * @return the persisted entity.
     */
    NEREntity save(NEREntity nEREntity);

    /**
     * Partially updates a nEREntity.
     *
     * @param nEREntity the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NEREntity> partialUpdate(NEREntity nEREntity);

    /**
     * Get all the nEREntities.
     *
     * @return the list of entities.
     */
    List<NEREntity> findAll();

    /**
     * Get the "id" nEREntity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NEREntity> findOne(Long id);

    /**
     * Delete the "id" nEREntity.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
