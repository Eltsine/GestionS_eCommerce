import * as dayjs from 'dayjs';
import { IAdresse } from 'app/entities/adresse/adresse.model';
import { IEntreprise } from 'app/entities/entreprise/entreprise.model';

export interface IPersonnel {
  id?: number;
  nom?: string | null;
  prenom?: string | null;
  dateNaissance?: dayjs.Dayjs | null;
  email?: string | null;
  motDePass?: string | null;
  photoContentType?: string | null;
  photo?: string | null;
  adresse?: IAdresse | null;
  entreprise?: IEntreprise | null;
}

export class Personnel implements IPersonnel {
  constructor(
    public id?: number,
    public nom?: string | null,
    public prenom?: string | null,
    public dateNaissance?: dayjs.Dayjs | null,
    public email?: string | null,
    public motDePass?: string | null,
    public photoContentType?: string | null,
    public photo?: string | null,
    public adresse?: IAdresse | null,
    public entreprise?: IEntreprise | null
  ) {}
}

export function getPersonnelIdentifier(personnel: IPersonnel): number | undefined {
  return personnel.id;
}
