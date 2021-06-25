import { INEREntity } from 'app/shared/model/ner-entity.model';
import { IIntent } from 'app/shared/model/intent.model';

export interface IUtterance {
  id?: number;
  utterance?: string | null;
  entities?: INEREntity[] | null;
  intent?: IIntent | null;
}

export const defaultValue: Readonly<IUtterance> = {};
