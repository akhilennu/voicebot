import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Intent from './intent';
import Utterance from './utterance';
import NEREntity from './ner-entity';
import Transcripts from './transcripts';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}intent`} component={Intent} />
      <ErrorBoundaryRoute path={`${match.url}utterance`} component={Utterance} />
      <ErrorBoundaryRoute path={`${match.url}ner-entity`} component={NEREntity} />
      <ErrorBoundaryRoute path={`${match.url}transcripts`} component={Transcripts} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
