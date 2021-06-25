package com.dbs.nlu.voicebot.service;

import com.dbs.nlu.voicebot.domain.Utterance;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Utterance}.
 */
public interface UtteranceService {
    /**
     * Save a utterance.
     *
     * @param utterance the entity to save.
     * @return the persisted entity.
     */
    Utterance save(Utterance utterance);

    /**
     * Partially updates a utterance.
     *
     * @param utterance the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Utterance> partialUpdate(Utterance utterance);

    /**
     * Get all the utterances.
     *
     * @return the list of entities.
     */
    List<Utterance> findAll();

    /**
     * Get the "id" utterance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Utterance> findOne(Long id);

    /**
     * Delete the "id" utterance.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
