package com.dbs.nlu.voicebot.repository;

import com.dbs.nlu.voicebot.domain.Transcripts;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Transcripts entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TranscriptsRepository extends JpaRepository<Transcripts, Long> {}
