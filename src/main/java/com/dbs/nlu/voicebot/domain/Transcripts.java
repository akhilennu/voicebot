package com.dbs.nlu.voicebot.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Transcripts.
 */
@Entity
@Table(name = "transcripts")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Transcripts implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "utterance")
    private String utterance;

    @Column(name = "intent")
    private String intent;

    @Column(name = "confidence")
    private String confidence;

    @Column(name = "gc_response")
    private String gcResponse;

    @Column(name = "entity_response")
    private String entityResponse;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Transcripts id(Long id) {
        this.id = id;
        return this;
    }

    public String getUtterance() {
        return this.utterance;
    }

    public Transcripts utterance(String utterance) {
        this.utterance = utterance;
        return this;
    }

    public void setUtterance(String utterance) {
        this.utterance = utterance;
    }

    public String getIntent() {
        return this.intent;
    }

    public Transcripts intent(String intent) {
        this.intent = intent;
        return this;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getConfidence() {
        return this.confidence;
    }

    public Transcripts confidence(String confidence) {
        this.confidence = confidence;
        return this;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public String getGcResponse() {
        return this.gcResponse;
    }

    public Transcripts gcResponse(String gcResponse) {
        this.gcResponse = gcResponse;
        return this;
    }

    public void setGcResponse(String gcResponse) {
        this.gcResponse = gcResponse;
    }

    public String getEntityResponse() {
        return this.entityResponse;
    }

    public Transcripts entityResponse(String entityResponse) {
        this.entityResponse = entityResponse;
        return this;
    }

    public void setEntityResponse(String entityResponse) {
        this.entityResponse = entityResponse;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transcripts)) {
            return false;
        }
        return id != null && id.equals(((Transcripts) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transcripts{" +
            "id=" + getId() +
            ", utterance='" + getUtterance() + "'" +
            ", intent='" + getIntent() + "'" +
            ", confidence='" + getConfidence() + "'" +
            ", gcResponse='" + getGcResponse() + "'" +
            ", entityResponse='" + getEntityResponse() + "'" +
            "}";
    }
}
