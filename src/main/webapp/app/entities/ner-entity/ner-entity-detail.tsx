import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './ner-entity.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const NEREntityDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const nEREntityEntity = useAppSelector(state => state.nEREntity.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="nEREntityDetailsHeading">NEREntity</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{nEREntityEntity.id}</dd>
          <dt>
            <span id="entityName">Entity Name</span>
          </dt>
          <dd>{nEREntityEntity.entityName}</dd>
          <dt>
            <span id="start">Start</span>
          </dt>
          <dd>{nEREntityEntity.start}</dd>
          <dt>
            <span id="end">End</span>
          </dt>
          <dd>{nEREntityEntity.end}</dd>
          <dt>Utterance</dt>
          <dd>{nEREntityEntity.utterance ? nEREntityEntity.utterance.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/ner-entity" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ner-entity/${nEREntityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default NEREntityDetail;
