import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Transcripts from './transcripts';
import TranscriptsDetail from './transcripts-detail';
import TranscriptsUpdate from './transcripts-update';
import TranscriptsDeleteDialog from './transcripts-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TranscriptsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TranscriptsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TranscriptsDetail} />
      <ErrorBoundaryRoute path={match.url} component={Transcripts} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TranscriptsDeleteDialog} />
  </>
);

export default Routes;
