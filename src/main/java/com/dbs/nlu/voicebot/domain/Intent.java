package com.dbs.nlu.voicebot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Intent.
 */
@Entity
@Table(name = "intent")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Intent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "intent")
    private String intent;

    @Column(name = "intent_name")
    private String intentName;

    @OneToMany(mappedBy = "intent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "entities", "intent" }, allowSetters = true)
    private Set<Utterance> utterances = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Intent id(Long id) {
        this.id = id;
        return this;
    }

    public String getIntent() {
        return this.intent;
    }

    public Intent intent(String intent) {
        this.intent = intent;
        return this;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getIntentName() {
        return this.intentName;
    }

    public Intent intentName(String intentName) {
        this.intentName = intentName;
        return this;
    }

    public void setIntentName(String intentName) {
        this.intentName = intentName;
    }

    public Set<Utterance> getUtterances() {
        return this.utterances;
    }

    public Intent utterances(Set<Utterance> utterances) {
        this.setUtterances(utterances);
        return this;
    }

    public Intent addUtterances(Utterance utterance) {
        this.utterances.add(utterance);
        utterance.setIntent(this);
        return this;
    }

    public Intent removeUtterances(Utterance utterance) {
        this.utterances.remove(utterance);
        utterance.setIntent(null);
        return this;
    }

    public void setUtterances(Set<Utterance> utterances) {
        if (this.utterances != null) {
            this.utterances.forEach(i -> i.setIntent(null));
        }
        if (utterances != null) {
            utterances.forEach(i -> i.setIntent(this));
        }
        this.utterances = utterances;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Intent)) {
            return false;
        }
        return id != null && id.equals(((Intent) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Intent{" +
            "id=" + getId() +
            ", intent='" + getIntent() + "'" +
            ", intentName='" + getIntentName() + "'" +
            "}";
    }
}
