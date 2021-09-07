import { IArticle } from 'app/entities/article/article.model';

export interface ICategorie {
  id?: number;
  code?: string | null;
  designation?: string | null;
  articles?: IArticle[] | null;
}

export class Categorie implements ICategorie {
  constructor(public id?: number, public code?: string | null, public designation?: string | null, public articles?: IArticle[] | null) {}
}

export function getCategorieIdentifier(categorie: ICategorie): number | undefined {
  return categorie.id;
}
