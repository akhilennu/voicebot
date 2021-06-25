import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IIntent } from 'app/shared/model/intent.model';
import { getEntities as getIntents } from 'app/entities/intent/intent.reducer';
import { getEntity, updateEntity, createEntity, reset } from './utterance.reducer';
import { IUtterance } from 'app/shared/model/utterance.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UtteranceUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const intents = useAppSelector(state => state.intent.entities);
  const utteranceEntity = useAppSelector(state => state.utterance.entity);
  const loading = useAppSelector(state => state.utterance.loading);
  const updating = useAppSelector(state => state.utterance.updating);
  const updateSuccess = useAppSelector(state => state.utterance.updateSuccess);

  const handleClose = () => {
    props.history.push('/utterance');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getIntents({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...utteranceEntity,
      ...values,
      intent: intents.find(it => it.id.toString() === values.intentId.toString()),
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
          ...utteranceEntity,
          intentId: utteranceEntity?.intent?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="voicebotApp.utterance.home.createOrEditLabel" data-cy="UtteranceCreateUpdateHeading">
            Create or edit a Utterance
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="utterance-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Utterance" id="utterance-utterance" name="utterance" data-cy="utterance" type="text" />
              <ValidatedField id="utterance-intent" name="intentId" data-cy="intent" label="Intent" type="select">
                <option value="" key="0" />
                {intents
                  ? intents.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/utterance" replace color="info">
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

export default UtteranceUpdate;
