package com.dbs.nlu.voicebot.service;

import com.dbs.nlu.voicebot.domain.Intent;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Intent}.
 */
public interface IntentService {
    /**
     * Save a intent.
     *
     * @param intent the entity to save.
     * @return the persisted entity.
     */
    Intent save(Intent intent);

    /**
     * Partially updates a intent.
     *
     * @param intent the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Intent> partialUpdate(Intent intent);

    /**
     * Get all the intents.
     *
     * @return the list of entities.
     */
    List<Intent> findAll();

    /**
     * Get the "id" intent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Intent> findOne(Long id);

    /**
     * Delete the "id" intent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
