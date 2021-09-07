import * as dayjs from 'dayjs';
import { ILigneVente } from 'app/entities/ligne-vente/ligne-vente.model';

export interface IVente {
  id?: number;
  code?: string | null;
  dateVente?: dayjs.Dayjs | null;
  commentaire?: string | null;
  ligneVentes?: ILigneVente[] | null;
}

export class Vente implements IVente {
  constructor(
    public id?: number,
    public code?: string | null,
    public dateVente?: dayjs.Dayjs | null,
    public commentaire?: string | null,
    public ligneVentes?: ILigneVente[] | null
  ) {}
}

export function getVenteIdentifier(vente: IVente): number | undefined {
  return vente.id;
}
