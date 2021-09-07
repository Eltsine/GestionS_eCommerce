import { IMvtStk } from 'app/entities/mvt-stk/mvt-stk.model';
import { IEntreprise } from 'app/entities/entreprise/entreprise.model';
import { ILigneCommandeClient } from 'app/entities/ligne-commande-client/ligne-commande-client.model';
import { ILigneCommandeFournisseur } from 'app/entities/ligne-commande-fournisseur/ligne-commande-fournisseur.model';
import { ILigneVente } from 'app/entities/ligne-vente/ligne-vente.model';
import { ICategorie } from 'app/entities/categorie/categorie.model';

export interface IArticle {
  id?: number;
  codeArticle?: string | null;
  designation?: string | null;
  prixUnitaireHT?: number | null;
  prixUnitaireTtc?: number | null;
  tauxTva?: number | null;
  photoContentType?: string | null;
  photo?: string | null;
  utilisateurs?: IMvtStk[] | null;
  entreprise?: IEntreprise | null;
  ligneCommandeClient?: ILigneCommandeClient | null;
  ligneCommandeFournisseur?: ILigneCommandeFournisseur | null;
  ligneVente?: ILigneVente | null;
  categorie?: ICategorie | null;
}

export class Article implements IArticle {
  constructor(
    public id?: number,
    public codeArticle?: string | null,
    public designation?: string | null,
    public prixUnitaireHT?: number | null,
    public prixUnitaireTtc?: number | null,
    public tauxTva?: number | null,
    public photoContentType?: string | null,
    public photo?: string | null,
    public utilisateurs?: IMvtStk[] | null,
    public entreprise?: IEntreprise | null,
    public ligneCommandeClient?: ILigneCommandeClient | null,
    public ligneCommandeFournisseur?: ILigneCommandeFournisseur | null,
    public ligneVente?: ILigneVente | null,
    public categorie?: ICategorie | null
  ) {}
}

export function getArticleIdentifier(article: IArticle): number | undefined {
  return article.id;
}
