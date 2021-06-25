import { IUtterance } from 'app/shared/model/utterance.model';

export interface INEREntity {
  id?: number;
  entityName?: string | null;
  start?: number | null;
  end?: number | null;
  utterance?: IUtterance | null;
}

export const defaultValue: Readonly<INEREntity> = {};
