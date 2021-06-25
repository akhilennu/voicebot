package com.dbs.nlu.voicebot.web.rest;

import com.dbs.nlu.voicebot.domain.Utterance;
import com.dbs.nlu.voicebot.repository.UtteranceRepository;
import com.dbs.nlu.voicebot.service.UtteranceService;
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
 * REST controller for managing {@link com.dbs.nlu.voicebot.domain.Utterance}.
 */
@RestController
@RequestMapping("/api")
public class UtteranceResource {

    private final Logger log = LoggerFactory.getLogger(UtteranceResource.class);

    private static final String ENTITY_NAME = "utterance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UtteranceService utteranceService;

    private final UtteranceRepository utteranceRepository;

    public UtteranceResource(UtteranceService utteranceService, UtteranceRepository utteranceRepository) {
        this.utteranceService = utteranceService;
        this.utteranceRepository = utteranceRepository;
    }

    /**
     * {@code POST  /utterances} : Create a new utterance.
     *
     * @param utterance the utterance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new utterance, or with status {@code 400 (Bad Request)} if the utterance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/utterances")
    public ResponseEntity<Utterance> createUtterance(@RequestBody Utterance utterance) throws URISyntaxException {
        log.debug("REST request to save Utterance : {}", utterance);
        if (utterance.getId() != null) {
            throw new BadRequestAlertException("A new utterance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Utterance result = utteranceService.save(utterance);
        return ResponseEntity
            .created(new URI("/api/utterances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /utterances/:id} : Updates an existing utterance.
     *
     * @param id the id of the utterance to save.
     * @param utterance the utterance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated utterance,
     * or with status {@code 400 (Bad Request)} if the utterance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the utterance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/utterances/{id}")
    public ResponseEntity<Utterance> updateUtterance(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Utterance utterance
    ) throws URISyntaxException {
        log.debug("REST request to update Utterance : {}, {}", id, utterance);
        if (utterance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utterance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utteranceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Utterance result = utteranceService.save(utterance);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, utterance.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /utterances/:id} : Partial updates given fields of an existing utterance, field will ignore if it is null
     *
     * @param id the id of the utterance to save.
     * @param utterance the utterance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated utterance,
     * or with status {@code 400 (Bad Request)} if the utterance is not valid,
     * or with status {@code 404 (Not Found)} if the utterance is not found,
     * or with status {@code 500 (Internal Server Error)} if the utterance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/utterances/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Utterance> partialUpdateUtterance(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Utterance utterance
    ) throws URISyntaxException {
        log.debug("REST request to partial update Utterance partially : {}, {}", id, utterance);
        if (utterance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utterance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utteranceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Utterance> result = utteranceService.partialUpdate(utterance);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, utterance.getId().toString())
        );
    }

    /**
     * {@code GET  /utterances} : get all the utterances.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of utterances in body.
     */
    @GetMapping("/utterances")
    public List<Utterance> getAllUtterances() {
        log.debug("REST request to get all Utterances");
        return utteranceService.findAll();
    }

    /**
     * {@code GET  /utterances/:id} : get the "id" utterance.
     *
     * @param id the id of the utterance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the utterance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/utterances/{id}")
    public ResponseEntity<Utterance> getUtterance(@PathVariable Long id) {
        log.debug("REST request to get Utterance : {}", id);
        Optional<Utterance> utterance = utteranceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(utterance);
    }

    /**
     * {@code DELETE  /utterances/:id} : delete the "id" utterance.
     *
     * @param id the id of the utterance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/utterances/{id}")
    public ResponseEntity<Void> deleteUtterance(@PathVariable Long id) {
        log.debug("REST request to delete Utterance : {}", id);
        utteranceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
