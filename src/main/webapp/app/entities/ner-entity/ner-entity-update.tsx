import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IUtterance } from 'app/shared/model/utterance.model';
import { getEntities as getUtterances } from 'app/entities/utterance/utterance.reducer';
import { getEntity, updateEntity, createEntity, reset } from './ner-entity.reducer';
import { INEREntity } from 'app/shared/model/ner-entity.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const NEREntityUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const utterances = useAppSelector(state => state.utterance.entities);
  const nEREntityEntity = useAppSelector(state => state.nEREntity.entity);
  const loading = useAppSelector(state => state.nEREntity.loading);
  const updating = useAppSelector(state => state.nEREntity.updating);
  const updateSuccess = useAppSelector(state => state.nEREntity.updateSuccess);

  const handleClose = () => {
    props.history.push('/ner-entity');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUtterances({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...nEREntityEntity,
      ...values,
      utterance: utterances.find(it => it.id.toString() === values.utteranceId.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...nEREntityEntity,
          utteranceId: nEREntityEntity?.utterance?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="voicebotApp.nEREntity.home.createOrEditLabel" data-cy="NEREntityCreateUpdateHeading">
            Create or edit a NEREntity
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="ner-entity-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Entity Name" id="ner-entity-entityName" name="entityName" data-cy="entityName" type="text" />
              <ValidatedField label="Start" id="ner-entity-start" name="start" data-cy="start" type="text" />
              <ValidatedField label="End" id="ner-entity-end" name="end" data-cy="end" type="text" />
              <ValidatedField id="ner-entity-utterance" name="utteranceId" data-cy="utterance" label="Utterance" type="select">
                <option value="" key="0" />
                {utterances
                  ? utterances.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ner-entity" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default NEREntityUpdate;
