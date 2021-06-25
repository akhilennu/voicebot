import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './transcripts.reducer';
import { ITranscripts } from 'app/shared/model/transcripts.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TranscriptsUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const transcriptsEntity = useAppSelector(state => state.transcripts.entity);
  const loading = useAppSelector(state => state.transcripts.loading);
  const updating = useAppSelector(state => state.transcripts.updating);
  const updateSuccess = useAppSelector(state => state.transcripts.updateSuccess);

  const handleClose = () => {
    props.history.push('/transcripts');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...transcriptsEntity,
      ...values,
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
          ...transcriptsEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="voicebotApp.transcripts.home.createOrEditLabel" data-cy="TranscriptsCreateUpdateHeading">
            Create or edit a Transcripts
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="transcripts-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Utterance" id="transcripts-utterance" name="utterance" data-cy="utterance" type="text" />
              <ValidatedField label="Intent" id="transcripts-intent" name="intent" data-cy="intent" type="text" />
              <ValidatedField label="Confidence" id="transcripts-confidence" name="confidence" data-cy="confidence" type="text" />
              <ValidatedField label="Gc Response" id="transcripts-gcResponse" name="gcResponse" data-cy="gcResponse" type="text" />
              <ValidatedField
                label="Entity Response"
                id="transcripts-entityResponse"
                name="entityResponse"
                data-cy="entityResponse"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/transcripts" replace color="info">
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

export default TranscriptsUpdate;
