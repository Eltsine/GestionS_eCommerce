import * as dayjs from 'dayjs';
import { ILigneCommandeFournisseur } from 'app/entities/ligne-commande-fournisseur/ligne-commande-fournisseur.model';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';

export interface ICommandeFournisseur {
  id?: number;
  code?: string | null;
  dateCommande?: dayjs.Dayjs | null;
  ligneCommandeFournisseurs?: ILigneCommandeFournisseur[] | null;
  fournisseur?: IFournisseur | null;
}

export class CommandeFournisseur implements ICommandeFournisseur {
  constructor(
    public id?: number,
    public code?: string | null,
    public dateCommande?: dayjs.Dayjs | null,
    public ligneCommandeFournisseurs?: ILigneCommandeFournisseur[] | null,
    public fournisseur?: IFournisseur | null
  ) {}
}

export function getCommandeFournisseurIdentifier(commandeFournisseur: ICommandeFournisseur): number | undefined {
  return commandeFournisseur.id;
}
