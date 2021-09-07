import { IArticle } from 'app/entities/article/article.model';
import { ICommandeFournisseur } from 'app/entities/commande-fournisseur/commande-fournisseur.model';

export interface ILigneCommandeFournisseur {
  id?: number;
  quantite?: number | null;
  prixUnitaire?: number | null;
  articles?: IArticle[] | null;
  commandeFournisseur?: ICommandeFournisseur | null;
}

export class LigneCommandeFournisseur implements ILigneCommandeFournisseur {
  constructor(
    public id?: number,
    public quantite?: number | null,
    public prixUnitaire?: number | null,
    public articles?: IArticle[] | null,
    public commandeFournisseur?: ICommandeFournisseur | null
  ) {}
}

export function getLigneCommandeFournisseurIdentifier(ligneCommandeFournisseur: ILigneCommandeFournisseur): number | undefined {
  return ligneCommandeFournisseur.id;
}
