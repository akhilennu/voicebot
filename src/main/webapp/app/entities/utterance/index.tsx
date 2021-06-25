import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Utterance from './utterance';
import UtteranceDetail from './utterance-detail';
import UtteranceUpdate from './utterance-update';
import UtteranceDeleteDialog from './utterance-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UtteranceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UtteranceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UtteranceDetail} />
      <ErrorBoundaryRoute path={match.url} component={Utterance} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UtteranceDeleteDialog} />
  </>
);

export default Routes;
