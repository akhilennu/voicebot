import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './transcripts.reducer';
import { ITranscripts } from 'app/shared/model/transcripts.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Transcripts = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const transcriptsList = useAppSelector(state => state.transcripts.entities);
  const loading = useAppSelector(state => state.transcripts.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="transcripts-heading" data-cy="TranscriptsHeading">
        Transcripts
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Transcripts
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {transcriptsList && transcriptsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Utterance</th>
                <th>Intent</th>
                <th>Confidence</th>
                <th>Gc Response</th>
                <th>Entity Response</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {transcriptsList.map((transcripts, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${transcripts.id}`} color="link" size="sm">
                      {transcripts.id}
                    </Button>
                  </td>
                  <td>{transcripts.utterance}</td>
                  <td>{transcripts.intent}</td>
                  <td>{transcripts.confidence}</td>
                  <td>{transcripts.gcResponse}</td>
                  <td>{transcripts.entityResponse}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${transcripts.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${transcripts.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${transcripts.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Transcripts found</div>
        )}
      </div>
    </div>
  );
};

export default Transcripts;
