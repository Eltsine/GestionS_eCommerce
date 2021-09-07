import { ICommandeClient } from 'app/entities/commande-client/commande-client.model';
import { IAdresse } from 'app/entities/adresse/adresse.model';

export interface IClient {
  id?: number;
  nom?: string | null;
  prenom?: string | null;
  mail?: string | null;
  numTel?: string | null;
  photoContentType?: string | null;
  photo?: string | null;
  commandeClients?: ICommandeClient[] | null;
  adresse?: IAdresse | null;
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public nom?: string | null,
    public prenom?: string | null,
    public mail?: string | null,
    public numTel?: string | null,
    public photoContentType?: string | null,
    public photo?: string | null,
    public commandeClients?: ICommandeClient[] | null,
    public adresse?: IAdresse | null
  ) {}
}

export function getClientIdentifier(client: IClient): number | undefined {
  return client.id;
}
