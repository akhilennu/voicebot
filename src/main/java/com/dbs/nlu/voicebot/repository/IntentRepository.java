package com.dbs.nlu.voicebot.repository;

import com.dbs.nlu.voicebot.domain.Intent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Intent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntentRepository extends JpaRepository<Intent, Long> {}
