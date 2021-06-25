package com.dbs.nlu.voicebot.repository;

import com.dbs.nlu.voicebot.domain.Utterance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Utterance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UtteranceRepository extends JpaRepository<Utterance, Long> {}
