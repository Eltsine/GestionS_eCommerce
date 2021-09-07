import { IPersonnel } from 'app/entities/personnel/personnel.model';
import { IArticle } from 'app/entities/article/article.model';
import { IAdresse } from 'app/entities/adresse/adresse.model';

export interface IEntreprise {
  id?: number;
  nom?: string | null;
  description?: string | null;
  codeFiscal?: string | null;
  photoContentType?: string | null;
  photo?: string | null;
  email?: string | null;
  numTe?: string | null;
  siteWeb?: string | null;
  personnel?: IPersonnel[] | null;
  articles?: IArticle[] | null;
  adresse?: IAdresse | null;
}

export class Entreprise implements IEntreprise {
  constructor(
    public id?: number,
    public nom?: string | null,
    public description?: string | null,
    public codeFiscal?: string | null,
    public photoContentType?: string | null,
    public photo?: string | null,
    public email?: string | null,
    public numTe?: string | null,
    public siteWeb?: string | null,
    public personnel?: IPersonnel[] | null,
    public articles?: IArticle[] | null,
    public adresse?: IAdresse | null
  ) {}
}

export function getEntrepriseIdentifier(entreprise: IEntreprise): number | undefined {
  return entreprise.id;
}
