import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILigneVente, getLigneVenteIdentifier } from '../ligne-vente.model';

export type EntityResponseType = HttpResponse<ILigneVente>;
export type EntityArrayResponseType = HttpResponse<ILigneVente[]>;

@Injectable({ providedIn: 'root' })
export class LigneVenteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ligne-ventes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ligneVente: ILigneVente): Observable<EntityResponseType> {
    return this.http.post<ILigneVente>(this.resourceUrl, ligneVente, { observe: 'response' });
  }

  update(ligneVente: ILigneVente): Observable<EntityResponseType> {
    return this.http.put<ILigneVente>(`${this.resourceUrl}/${getLigneVenteIdentifier(ligneVente) as number}`, ligneVente, {
      observe: 'response',
    });
  }

  partialUpdate(ligneVente: ILigneVente): Observable<EntityResponseType> {
    return this.http.patch<ILigneVente>(`${this.resourceUrl}/${getLigneVenteIdentifier(ligneVente) as number}`, ligneVente, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILigneVente>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILigneVente[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLigneVenteToCollectionIfMissing(
    ligneVenteCollection: ILigneVente[],
    ...ligneVentesToCheck: (ILigneVente | null | undefined)[]
  ): ILigneVente[] {
    const ligneVentes: ILigneVente[] = ligneVentesToCheck.filter(isPresent);
    if (ligneVentes.length > 0) {
      const ligneVenteCollectionIdentifiers = ligneVenteCollection.map(ligneVenteItem => getLigneVenteIdentifier(ligneVenteItem)!);
      const ligneVentesToAdd = ligneVentes.filter(ligneVenteItem => {
        const ligneVenteIdentifier = getLigneVenteIdentifier(ligneVenteItem);
        if (ligneVenteIdentifier == null || ligneVenteCollectionIdentifiers.includes(ligneVenteIdentifier)) {
          return false;
        }
        ligneVenteCollectionIdentifiers.push(ligneVenteIdentifier);
        return true;
      });
      return [...ligneVentesToAdd, ...ligneVenteCollection];
    }
    return ligneVenteCollection;
  }
}
