package com.dbs.nlu.voicebot.web.rest;

import com.dbs.nlu.voicebot.domain.Transcripts;
import com.dbs.nlu.voicebot.repository.TranscriptsRepository;
import com.dbs.nlu.voicebot.service.TranscriptsService;
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
 * REST controller for managing {@link com.dbs.nlu.voicebot.domain.Transcripts}.
 */
@RestController
@RequestMapping("/api")
public class TranscriptsResource {

    private final Logger log = LoggerFactory.getLogger(TranscriptsResource.class);

    private static final String ENTITY_NAME = "transcripts";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TranscriptsService transcriptsService;

    private final TranscriptsRepository transcriptsRepository;

    public TranscriptsResource(TranscriptsService transcriptsService, TranscriptsRepository transcriptsRepository) {
        this.transcriptsService = transcriptsService;
        this.transcriptsRepository = transcriptsRepository;
    }

    /**
     * {@code POST  /transcripts} : Create a new transcripts.
     *
     * @param transcripts the transcripts to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transcripts, or with status {@code 400 (Bad Request)} if the transcripts has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transcripts")
    public ResponseEntity<Transcripts> createTranscripts(@RequestBody Transcripts transcripts) throws URISyntaxException {
        log.debug("REST request to save Transcripts : {}", transcripts);
        if (transcripts.getId() != null) {
            throw new BadRequestAlertException("A new transcripts cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Transcripts result = transcriptsService.save(transcripts);
        return ResponseEntity
            .created(new URI("/api/transcripts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transcripts/:id} : Updates an existing transcripts.
     *
     * @param id the id of the transcripts to save.
     * @param transcripts the transcripts to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transcripts,
     * or with status {@code 400 (Bad Request)} if the transcripts is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transcripts couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transcripts/{id}")
    public ResponseEntity<Transcripts> updateTranscripts(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Transcripts transcripts
    ) throws URISyntaxException {
        log.debug("REST request to update Transcripts : {}, {}", id, transcripts);
        if (transcripts.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transcripts.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transcriptsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Transcripts result = transcriptsService.save(transcripts);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transcripts.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /transcripts/:id} : Partial updates given fields of an existing transcripts, field will ignore if it is null
     *
     * @param id the id of the transcripts to save.
     * @param transcripts the transcripts to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transcripts,
     * or with status {@code 400 (Bad Request)} if the transcripts is not valid,
     * or with status {@code 404 (Not Found)} if the transcripts is not found,
     * or with status {@code 500 (Internal Server Error)} if the transcripts couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transcripts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Transcripts> partialUpdateTranscripts(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Transcripts transcripts
    ) throws URISyntaxException {
        log.debug("REST request to partial update Transcripts partially : {}, {}", id, transcripts);
        if (transcripts.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transcripts.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transcriptsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Transcripts> result = transcriptsService.partialUpdate(transcripts);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transcripts.getId().toString())
        );
    }

    /**
     * {@code GET  /transcripts} : get all the transcripts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transcripts in body.
     */
    @GetMapping("/transcripts")
    public List<Transcripts> getAllTranscripts() {
        log.debug("REST request to get all Transcripts");
        return transcriptsService.findAll();
    }

    /**
     * {@code GET  /transcripts/:id} : get the "id" transcripts.
     *
     * @param id the id of the transcripts to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transcripts, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transcripts/{id}")
    public ResponseEntity<Transcripts> getTranscripts(@PathVariable Long id) {
        log.debug("REST request to get Transcripts : {}", id);
        Optional<Transcripts> transcripts = transcriptsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transcripts);
    }

    /**
     * {@code DELETE  /transcripts/:id} : delete the "id" transcripts.
     *
     * @param id the id of the transcripts to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transcripts/{id}")
    public ResponseEntity<Void> deleteTranscripts(@PathVariable Long id) {
        log.debug("REST request to delete Transcripts : {}", id);
        transcriptsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
