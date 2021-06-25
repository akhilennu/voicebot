package com.dbs.nlu.voicebot.service.impl;

import com.dbs.nlu.voicebot.domain.Transcripts;
import com.dbs.nlu.voicebot.repository.TranscriptsRepository;
import com.dbs.nlu.voicebot.service.TranscriptsService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Transcripts}.
 */
@Service
@Transactional
public class TranscriptsServiceImpl implements TranscriptsService {

    private final Logger log = LoggerFactory.getLogger(TranscriptsServiceImpl.class);

    private final TranscriptsRepository transcriptsRepository;

    public TranscriptsServiceImpl(TranscriptsRepository transcriptsRepository) {
        this.transcriptsRepository = transcriptsRepository;
    }

    @Override
    public Transcripts save(Transcripts transcripts) {
        log.debug("Request to save Transcripts : {}", transcripts);
        return transcriptsRepository.save(transcripts);
    }

    @Override
    public Optional<Transcripts> partialUpdate(Transcripts transcripts) {
        log.debug("Request to partially update Transcripts : {}", transcripts);

        return transcriptsRepository
            .findById(transcripts.getId())
            .map(
                existingTranscripts -> {
                    if (transcripts.getUtterance() != null) {
                        existingTranscripts.setUtterance(transcripts.getUtterance());
                    }
                    if (transcripts.getIntent() != null) {
                        existingTranscripts.setIntent(transcripts.getIntent());
                    }
                    if (transcripts.getConfidence() != null) {
                        existingTranscripts.setConfidence(transcripts.getConfidence());
                    }
                    if (transcripts.getGcResponse() != null) {
                        existingTranscripts.setGcResponse(transcripts.getGcResponse());
                    }
                    if (transcripts.getEntityResponse() != null) {
                        existingTranscripts.setEntityResponse(transcripts.getEntityResponse());
                    }

                    return existingTranscripts;
                }
            )
            .map(transcriptsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transcripts> findAll() {
        log.debug("Request to get all Transcripts");
        return transcriptsRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Transcripts> findOne(Long id) {
        log.debug("Request to get Transcripts : {}", id);
        return transcriptsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Transcripts : {}", id);
        transcriptsRepository.deleteById(id);
    }
}
