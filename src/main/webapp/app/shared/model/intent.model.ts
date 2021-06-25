import { IUtterance } from 'app/shared/model/utterance.model';

export interface IIntent {
  id?: number;
  intent?: string | null;
  intentName?: string | null;
  utterances?: IUtterance[] | null;
}

export const defaultValue: Readonly<IIntent> = {};
