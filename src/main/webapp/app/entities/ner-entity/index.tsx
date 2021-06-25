import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import NEREntity from './ner-entity';
import NEREntityDetail from './ner-entity-detail';
import NEREntityUpdate from './ner-entity-update';
import NEREntityDeleteDialog from './ner-entity-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={NEREntityUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={NEREntityUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={NEREntityDetail} />
      <ErrorBoundaryRoute path={match.url} component={NEREntity} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={NEREntityDeleteDialog} />
  </>
);

export default Routes;
