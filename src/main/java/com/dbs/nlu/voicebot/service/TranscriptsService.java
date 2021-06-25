package com.dbs.nlu.voicebot.service;

import com.dbs.nlu.voicebot.domain.Transcripts;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Transcripts}.
 */
public interface TranscriptsService {
    /**
     * Save a transcripts.
     *
     * @param transcripts the entity to save.
     * @return the persisted entity.
     */
    Transcripts save(Transcripts transcripts);

    /**
     * Partially updates a transcripts.
     *
     * @param transcripts the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Transcripts> partialUpdate(Transcripts transcripts);

    /**
     * Get all the transcripts.
     *
     * @return the list of entities.
     */
    List<Transcripts> findAll();

    /**
     * Get the "id" transcripts.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Transcripts> findOne(Long id);

    /**
     * Delete the "id" transcripts.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
