package com.dbs.nlu.voicebot.service.impl;

import com.dbs.nlu.voicebot.domain.Intent;
import com.dbs.nlu.voicebot.repository.IntentRepository;
import com.dbs.nlu.voicebot.service.IntentService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Intent}.
 */
@Service
@Transactional
public class IntentServiceImpl implements IntentService {

    private final Logger log = LoggerFactory.getLogger(IntentServiceImpl.class);

    private final IntentRepository intentRepository;

    public IntentServiceImpl(IntentRepository intentRepository) {
        this.intentRepository = intentRepository;
    }

    @Override
    public Intent save(Intent intent) {
        log.debug("Request to save Intent : {}", intent);
        return intentRepository.save(intent);
    }

    @Override
    public Optional<Intent> partialUpdate(Intent intent) {
        log.debug("Request to partially update Intent : {}", intent);

        return intentRepository
            .findById(intent.getId())
            .map(
                existingIntent -> {
                    if (intent.getIntent() != null) {
                        existingIntent.setIntent(intent.getIntent());
                    }
                    if (intent.getIntentName() != null) {
                        existingIntent.setIntentName(intent.getIntentName());
                    }

                    return existingIntent;
                }
            )
            .map(intentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Intent> findAll() {
        log.debug("Request to get all Intents");
        return intentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Intent> findOne(Long id) {
        log.debug("Request to get Intent : {}", id);
        return intentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Intent : {}", id);
        intentRepository.deleteById(id);
    }
}
