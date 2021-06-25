package com.dbs.nlu.voicebot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Utterance.
 */
@Entity
@Table(name = "utterance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Utterance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "utterance")
    private String utterance;

    @OneToMany(mappedBy = "utterance")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "utterance" }, allowSetters = true)
    private Set<NEREntity> entities = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "utterances" }, allowSetters = true)
    private Intent intent;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utterance id(Long id) {
        this.id = id;
        return this;
    }

    public String getUtterance() {
        return this.utterance;
    }

    public Utterance utterance(String utterance) {
        this.utterance = utterance;
        return this;
    }

    public void setUtterance(String utterance) {
        this.utterance = utterance;
    }

    public Set<NEREntity> getEntities() {
        return this.entities;
    }

    public Utterance entities(Set<NEREntity> nEREntities) {
        this.setEntities(nEREntities);
        return this;
    }

    public Utterance addEntities(NEREntity nEREntity) {
        this.entities.add(nEREntity);
        nEREntity.setUtterance(this);
        return this;
    }

    public Utterance removeEntities(NEREntity nEREntity) {
        this.entities.remove(nEREntity);
        nEREntity.setUtterance(null);
        return this;
    }

    public void setEntities(Set<NEREntity> nEREntities) {
        if (this.entities != null) {
            this.entities.forEach(i -> i.setUtterance(null));
        }
        if (nEREntities != null) {
            nEREntities.forEach(i -> i.setUtterance(this));
        }
        this.entities = nEREntities;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public Utterance intent(Intent intent) {
        this.setIntent(intent);
        return this;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Utterance)) {
            return false;
        }
        return id != null && id.equals(((Utterance) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Utterance{" +
            "id=" + getId() +
            ", utterance='" + getUtterance() + "'" +
            "}";
    }
}
