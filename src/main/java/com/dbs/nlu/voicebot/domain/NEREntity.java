package com.dbs.nlu.voicebot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * not an ignored comment
 */
@ApiModel(description = "not an ignored comment")
@Entity
@Table(name = "ner_entity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NEREntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "start")
    private Integer start;

    @Column(name = "end")
    private Integer end;

    @ManyToOne
    @JsonIgnoreProperties(value = { "entities", "intent" }, allowSetters = true)
    private Utterance utterance;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NEREntity id(Long id) {
        this.id = id;
        return this;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public NEREntity entityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Integer getStart() {
        return this.start;
    }

    public NEREntity start(Integer start) {
        this.start = start;
        return this;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return this.end;
    }

    public NEREntity end(Integer end) {
        this.end = end;
        return this;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public Utterance getUtterance() {
        return this.utterance;
    }

    public NEREntity utterance(Utterance utterance) {
        this.setUtterance(utterance);
        return this;
    }

    public void setUtterance(Utterance utterance) {
        this.utterance = utterance;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NEREntity)) {
            return false;
        }
        return id != null && id.equals(((NEREntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NEREntity{" +
            "id=" + getId() +
            ", entityName='" + getEntityName() + "'" +
            ", start=" + getStart() +
            ", end=" + getEnd() +
            "}";
    }
}
