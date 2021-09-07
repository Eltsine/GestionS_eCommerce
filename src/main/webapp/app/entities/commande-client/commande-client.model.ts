import * as dayjs from 'dayjs';
import { ILigneCommandeClient } from 'app/entities/ligne-commande-client/ligne-commande-client.model';
import { IClient } from 'app/entities/client/client.model';

export interface ICommandeClient {
  id?: number;
  code?: string | null;
  dateCommande?: dayjs.Dayjs | null;
  ligneCommandeClients?: ILigneCommandeClient[] | null;
  client?: IClient | null;
}

export class CommandeClient implements ICommandeClient {
  constructor(
    public id?: number,
    public code?: string | null,
    public dateCommande?: dayjs.Dayjs | null,
    public ligneCommandeClients?: ILigneCommandeClient[] | null,
    public client?: IClient | null
  ) {}
}

export function getCommandeClientIdentifier(commandeClient: ICommandeClient): number | undefined {
  return commandeClient.id;
}
