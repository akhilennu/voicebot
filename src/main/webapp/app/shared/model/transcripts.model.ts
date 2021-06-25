export interface ITranscripts {
  id?: number;
  utterance?: string | null;
  intent?: string | null;
  confidence?: string | null;
  gcResponse?: string | null;
  entityResponse?: string | null;
}

export const defaultValue: Readonly<ITranscripts> = {};
