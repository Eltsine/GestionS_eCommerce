import { IArticle } from 'app/entities/article/article.model';
import { IVente } from 'app/entities/vente/vente.model';

export interface ILigneVente {
  id?: number;
  quantite?: number | null;
  prixUnitaire?: number | null;
  articles?: IArticle[] | null;
  vente?: IVente | null;
}

export class LigneVente implements ILigneVente {
  constructor(
    public id?: number,
    public quantite?: number | null,
    public prixUnitaire?: number | null,
    public articles?: IArticle[] | null,
    public vente?: IVente | null
  ) {}
}

export function getLigneVenteIdentifier(ligneVente: ILigneVente): number | undefined {
  return ligneVente.id;
}
