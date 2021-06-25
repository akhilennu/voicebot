package com.dbs.nlu.voicebot.service.impl;

import com.dbs.nlu.voicebot.domain.Utterance;
import com.dbs.nlu.voicebot.repository.UtteranceRepository;
import com.dbs.nlu.voicebot.service.UtteranceService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Utterance}.
 */
@Service
@Transactional
public class UtteranceServiceImpl implements UtteranceService {

    private final Logger log = LoggerFactory.getLogger(UtteranceServiceImpl.class);

    private final UtteranceRepository utteranceRepository;

    public UtteranceServiceImpl(UtteranceRepository utteranceRepository) {
        this.utteranceRepository = utteranceRepository;
    }

    @Override
    public Utterance save(Utterance utterance) {
        log.debug("Request to save Utterance : {}", utterance);
        return utteranceRepository.save(utterance);
    }

    @Override
    public Optional<Utterance> partialUpdate(Utterance utterance) {
        log.debug("Request to partially update Utterance : {}", utterance);

        return utteranceRepository
            .findById(utterance.getId())
            .map(
                existingUtterance -> {
                    if (utterance.getUtterance() != null) {
                        existingUtterance.setUtterance(utterance.getUtterance());
                    }

                    return existingUtterance;
                }
            )
            .map(utteranceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Utterance> findAll() {
        log.debug("Request to get all Utterances");
        return utteranceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Utterance> findOne(Long id) {
        log.debug("Request to get Utterance : {}", id);
        return utteranceRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Utterance : {}", id);
        utteranceRepository.deleteById(id);
    }
}
