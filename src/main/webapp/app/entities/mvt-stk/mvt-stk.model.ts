import * as dayjs from 'dayjs';
import { IArticle } from 'app/entities/article/article.model';
import { TypeMvtStk } from 'app/entities/enumerations/type-mvt-stk.model';

export interface IMvtStk {
  id?: number;
  dateMvt?: dayjs.Dayjs | null;
  quantite?: number | null;
  typeMvtStk?: TypeMvtStk | null;
  entreprise?: IArticle | null;
}

export class MvtStk implements IMvtStk {
  constructor(
    public id?: number,
    public dateMvt?: dayjs.Dayjs | null,
    public quantite?: number | null,
    public typeMvtStk?: TypeMvtStk | null,
    public entreprise?: IArticle | null
  ) {}
}

export function getMvtStkIdentifier(mvtStk: IMvtStk): number | undefined {
  return mvtStk.id;
}
