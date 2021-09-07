export interface IAdresse {
  id?: number;
  adresse1?: string | null;
  adresse2?: string | null;
  ville?: string | null;
  codePostal?: string | null;
  pays?: string | null;
}

export class Adresse implements IAdresse {
  constructor(
    public id?: number,
    public adresse1?: string | null,
    public adresse2?: string | null,
    public ville?: string | null,
    public codePostal?: string | null,
    public pays?: string | null
  ) {}
}

export function getAdresseIdentifier(adresse: IAdresse): number | undefined {
  return adresse.id;
}
