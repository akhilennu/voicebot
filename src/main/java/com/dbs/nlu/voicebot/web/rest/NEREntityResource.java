package com.dbs.nlu.voicebot.web.rest;

import com.dbs.nlu.voicebot.domain.NEREntity;
import com.dbs.nlu.voicebot.repository.NEREntityRepository;
import com.dbs.nlu.voicebot.service.NEREntityService;
import com.dbs.nlu.voicebot.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.dbs.nlu.voicebot.domain.NEREntity}.
 */
@RestController
@RequestMapping("/api")
public class NEREntityResource {

    private final Logger log = LoggerFactory.getLogger(NEREntityResource.class);

    private static final String ENTITY_NAME = "nEREntity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NEREntityService nEREntityService;

    private final NEREntityRepository nEREntityRepository;

    public NEREntityResource(NEREntityService nEREntityService, NEREntityRepository nEREntityRepository) {
        this.nEREntityService = nEREntityService;
        this.nEREntityRepository = nEREntityRepository;
    }

    /**
     * {@code POST  /ner-entities} : Create a new nEREntity.
     *
     * @param nEREntity the nEREntity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nEREntity, or with status {@code 400 (Bad Request)} if the nEREntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ner-entities")
    public ResponseEntity<NEREntity> createNEREntity(@RequestBody NEREntity nEREntity) throws URISyntaxException {
        log.debug("REST request to save NEREntity : {}", nEREntity);
        if (nEREntity.getId() != null) {
            throw new BadRequestAlertException("A new nEREntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NEREntity result = nEREntityService.save(nEREntity);
        return ResponseEntity
            .created(new URI("/api/ner-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ner-entities/:id} : Updates an existing nEREntity.
     *
     * @param id the id of the nEREntity to save.
     * @param nEREntity the nEREntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nEREntity,
     * or with status {@code 400 (Bad Request)} if the nEREntity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nEREntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ner-entities/{id}")
    public ResponseEntity<NEREntity> updateNEREntity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NEREntity nEREntity
    ) throws URISyntaxException {
        log.debug("REST request to update NEREntity : {}, {}", id, nEREntity);
        if (nEREntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nEREntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nEREntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NEREntity result = nEREntityService.save(nEREntity);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, nEREntity.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ner-entities/:id} : Partial updates given fields of an existing nEREntity, field will ignore if it is null
     *
     * @param id the id of the nEREntity to save.
     * @param nEREntity the nEREntity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nEREntity,
     * or with status {@code 400 (Bad Request)} if the nEREntity is not valid,
     * or with status {@code 404 (Not Found)} if the nEREntity is not found,
     * or with status {@code 500 (Internal Server Error)} if the nEREntity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ner-entities/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<NEREntity> partialUpdateNEREntity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NEREntity nEREntity
    ) throws URISyntaxException {
        log.debug("REST request to partial update NEREntity partially : {}, {}", id, nEREntity);
        if (nEREntity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nEREntity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nEREntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NEREntity> result = nEREntityService.partialUpdate(nEREntity);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, nEREntity.getId().toString())
        );
    }

    /**
     * {@code GET  /ner-entities} : get all the nEREntities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nEREntities in body.
     */
    @GetMapping("/ner-entities")
    public List<NEREntity> getAllNEREntities() {
        log.debug("REST request to get all NEREntities");
        return nEREntityService.findAll();
    }

    /**
     * {@code GET  /ner-entities/:id} : get the "id" nEREntity.
     *
     * @param id the id of the nEREntity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nEREntity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ner-entities/{id}")
    public ResponseEntity<NEREntity> getNEREntity(@PathVariable Long id) {
        log.debug("REST request to get NEREntity : {}", id);
        Optional<NEREntity> nEREntity = nEREntityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nEREntity);
    }

    /**
     * {@code DELETE  /ner-entities/:id} : delete the "id" nEREntity.
     *
     * @param id the id of the nEREntity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ner-entities/{id}")
    public ResponseEntity<Void> deleteNEREntity(@PathVariable Long id) {
        log.debug("REST request to delete NEREntity : {}", id);
        nEREntityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
