import { ICommandeFournisseur } from 'app/entities/commande-fournisseur/commande-fournisseur.model';
import { IAdresse } from 'app/entities/adresse/adresse.model';

export interface IFournisseur {
  id?: number;
  nom?: string | null;
  prenom?: string | null;
  mail?: string | null;
  numTel?: string | null;
  photoContentType?: string | null;
  photo?: string | null;
  commandeFournisseurs?: ICommandeFournisseur[] | null;
  adresse?: IAdresse | null;
}

export class Fournisseur implements IFournisseur {
  constructor(
    public id?: number,
    public nom?: string | null,
    public prenom?: string | null,
    public mail?: string | null,
    public numTel?: string | null,
    public photoContentType?: string | null,
    public photo?: string | null,
    public commandeFournisseurs?: ICommandeFournisseur[] | null,
    public adresse?: IAdresse | null
  ) {}
}

export function getFournisseurIdentifier(fournisseur: IFournisseur): number | undefined {
  return fournisseur.id;
}
