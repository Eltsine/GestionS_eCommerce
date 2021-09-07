import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILigneCommandeClient, getLigneCommandeClientIdentifier } from '../ligne-commande-client.model';

export type EntityResponseType = HttpResponse<ILigneCommandeClient>;
export type EntityArrayResponseType = HttpResponse<ILigneCommandeClient[]>;

@Injectable({ providedIn: 'root' })
export class LigneCommandeClientService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ligne-commande-clients');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ligneCommandeClient: ILigneCommandeClient): Observable<EntityResponseType> {
    return this.http.post<ILigneCommandeClient>(this.resourceUrl, ligneCommandeClient, { observe: 'response' });
  }

  update(ligneCommandeClient: ILigneCommandeClient): Observable<EntityResponseType> {
    return this.http.put<ILigneCommandeClient>(
      `${this.resourceUrl}/${getLigneCommandeClientIdentifier(ligneCommandeClient) as number}`,
      ligneCommandeClient,
      { observe: 'response' }
    );
  }

  partialUpdate(ligneCommandeClient: ILigneCommandeClient): Observable<EntityResponseType> {
    return this.http.patch<ILigneCommandeClient>(
      `${this.resourceUrl}/${getLigneCommandeClientIdentifier(ligneCommandeClient) as number}`,
      ligneCommandeClient,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILigneCommandeClient>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILigneCommandeClient[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLigneCommandeClientToCollectionIfMissing(
    ligneCommandeClientCollection: ILigneCommandeClient[],
    ...ligneCommandeClientsToCheck: (ILigneCommandeClient | null | undefined)[]
  ): ILigneCommandeClient[] {
    const ligneCommandeClients: ILigneCommandeClient[] = ligneCommandeClientsToCheck.filter(isPresent);
    if (ligneCommandeClients.length > 0) {
      const ligneCommandeClientCollectionIdentifiers = ligneCommandeClientCollection.map(
        ligneCommandeClientItem => getLigneCommandeClientIdentifier(ligneCommandeClientItem)!
      );
      const ligneCommandeClientsToAdd = ligneCommandeClients.filter(ligneCommandeClientItem => {
        const ligneCommandeClientIdentifier = getLigneCommandeClientIdentifier(ligneCommandeClientItem);
        if (ligneCommandeClientIdentifier == null || ligneCommandeClientCollectionIdentifiers.includes(ligneCommandeClientIdentifier)) {
          return false;
        }
        ligneCommandeClientCollectionIdentifiers.push(ligneCommandeClientIdentifier);
        return true;
      });
      return [...ligneCommandeClientsToAdd, ...ligneCommandeClientCollection];
    }
    return ligneCommandeClientCollection;
  }
}
