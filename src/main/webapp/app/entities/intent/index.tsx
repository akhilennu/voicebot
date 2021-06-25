import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Intent from './intent';
import IntentDetail from './intent-detail';
import IntentUpdate from './intent-update';
import IntentDeleteDialog from './intent-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={IntentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={IntentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={IntentDetail} />
      <ErrorBoundaryRoute path={match.url} component={Intent} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={IntentDeleteDialog} />
  </>
);

export default Routes;
