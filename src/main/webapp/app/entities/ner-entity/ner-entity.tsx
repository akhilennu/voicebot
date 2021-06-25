import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './ner-entity.reducer';
import { INEREntity } from 'app/shared/model/ner-entity.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const NEREntity = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const nEREntityList = useAppSelector(state => state.nEREntity.entities);
  const loading = useAppSelector(state => state.nEREntity.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="ner-entity-heading" data-cy="NEREntityHeading">
        NER Entities
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new NER Entity
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {nEREntityList && nEREntityList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Entity Name</th>
                <th>Start</th>
                <th>End</th>
                <th>Utterance</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {nEREntityList.map((nEREntity, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${nEREntity.id}`} color="link" size="sm">
                      {nEREntity.id}
                    </Button>
                  </td>
                  <td>{nEREntity.entityName}</td>
                  <td>{nEREntity.start}</td>
                  <td>{nEREntity.end}</td>
                  <td>{nEREntity.utterance ? <Link to={`utterance/${nEREntity.utterance.id}`}>{nEREntity.utterance.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${nEREntity.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${nEREntity.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${nEREntity.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No NER Entities found</div>
        )}
      </div>
    </div>
  );
};

export default NEREntity;
