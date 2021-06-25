import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './utterance.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UtteranceDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const utteranceEntity = useAppSelector(state => state.utterance.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="utteranceDetailsHeading">Utterance</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{utteranceEntity.id}</dd>
          <dt>
            <span id="utterance">Utterance</span>
          </dt>
          <dd>{utteranceEntity.utterance}</dd>
          <dt>Intent</dt>
          <dd>{utteranceEntity.intent ? utteranceEntity.intent.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/utterance" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/utterance/${utteranceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default UtteranceDetail;
