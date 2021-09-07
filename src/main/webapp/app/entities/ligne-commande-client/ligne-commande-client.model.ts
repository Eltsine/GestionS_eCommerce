import { IArticle } from 'app/entities/article/article.model';
import { ICommandeClient } from 'app/entities/commande-client/commande-client.model';

export interface ILigneCommandeClient {
  id?: number;
  quantite?: number | null;
  prixUnitaire?: number | null;
  articles?: IArticle[] | null;
  commandeClient?: ICommandeClient | null;
}

export class LigneCommandeClient implements ILigneCommandeClient {
  constructor(
    public id?: number,
    public quantite?: number | null,
    public prixUnitaire?: number | null,
    public articles?: IArticle[] | null,
    public commandeClient?: ICommandeClient | null
  ) {}
}

export function getLigneCommandeClientIdentifier(ligneCommandeClient: ILigneCommandeClient): number | undefined {
  return ligneCommandeClient.id;
}
