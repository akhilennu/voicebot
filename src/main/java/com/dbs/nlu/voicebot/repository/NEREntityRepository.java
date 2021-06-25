package com.dbs.nlu.voicebot.repository;

import com.dbs.nlu.voicebot.domain.NEREntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the NEREntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NEREntityRepository extends JpaRepository<NEREntity, Long> {}
