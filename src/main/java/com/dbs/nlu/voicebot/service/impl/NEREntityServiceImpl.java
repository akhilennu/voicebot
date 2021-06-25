package com.dbs.nlu.voicebot.service.impl;

import com.dbs.nlu.voicebot.domain.NEREntity;
import com.dbs.nlu.voicebot.repository.NEREntityRepository;
import com.dbs.nlu.voicebot.service.NEREntityService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link NEREntity}.
 */
@Service
@Transactional
public class NEREntityServiceImpl implements NEREntityService {

    private final Logger log = LoggerFactory.getLogger(NEREntityServiceImpl.class);

    private final NEREntityRepository nEREntityRepository;

    public NEREntityServiceImpl(NEREntityRepository nEREntityRepository) {
        this.nEREntityRepository = nEREntityRepository;
    }

    @Override
    public NEREntity save(NEREntity nEREntity) {
        log.debug("Request to save NEREntity : {}", nEREntity);
        return nEREntityRepository.save(nEREntity);
    }

    @Override
    public Optional<NEREntity> partialUpdate(NEREntity nEREntity) {
        log.debug("Request to partially update NEREntity : {}", nEREntity);

        return nEREntityRepository
            .findById(nEREntity.getId())
            .map(
                existingNEREntity -> {
                    if (nEREntity.getEntityName() != null) {
                        existingNEREntity.setEntityName(nEREntity.getEntityName());
                    }
                    if (nEREntity.getStart() != null) {
                        existingNEREntity.setStart(nEREntity.getStart());
                    }
                    if (nEREntity.getEnd() != null) {
                        existingNEREntity.setEnd(nEREntity.getEnd());
                    }

                    return existingNEREntity;
                }
            )
            .map(nEREntityRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NEREntity> findAll() {
        log.debug("Request to get all NEREntities");
        return nEREntityRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NEREntity> findOne(Long id) {
        log.debug("Request to get NEREntity : {}", id);
        return nEREntityRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete NEREntity : {}", id);
        nEREntityRepository.deleteById(id);
    }
}
