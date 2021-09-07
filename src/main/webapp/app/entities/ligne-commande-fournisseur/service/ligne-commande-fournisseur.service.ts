import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILigneCommandeFournisseur, getLigneCommandeFournisseurIdentifier } from '../ligne-commande-fournisseur.model';

export type EntityResponseType = HttpResponse<ILigneCommandeFournisseur>;
export type EntityArrayResponseType = HttpResponse<ILigneCommandeFournisseur[]>;

@Injectable({ providedIn: 'root' })
export class LigneCommandeFournisseurService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ligne-commande-fournisseurs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ligneCommandeFournisseur: ILigneCommandeFournisseur): Observable<EntityResponseType> {
    return this.http.post<ILigneCommandeFournisseur>(this.resourceUrl, ligneCommandeFournisseur, { observe: 'response' });
  }

  update(ligneCommandeFournisseur: ILigneCommandeFournisseur): Observable<EntityResponseType> {
    return this.http.put<ILigneCommandeFournisseur>(
      `${this.resourceUrl}/${getLigneCommandeFournisseurIdentifier(ligneCommandeFournisseur) as number}`,
      ligneCommandeFournisseur,
      { observe: 'response' }
    );
  }

  partialUpdate(ligneCommandeFournisseur: ILigneCommandeFournisseur): Observable<EntityResponseType> {
    return this.http.patch<ILigneCommandeFournisseur>(
      `${this.resourceUrl}/${getLigneCommandeFournisseurIdentifier(ligneCommandeFournisseur) as number}`,
      ligneCommandeFournisseur,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILigneCommandeFournisseur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILigneCommandeFournisseur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLigneCommandeFournisseurToCollectionIfMissing(
    ligneCommandeFournisseurCollection: ILigneCommandeFournisseur[],
    ...ligneCommandeFournisseursToCheck: (ILigneCommandeFournisseur | null | undefined)[]
  ): ILigneCommandeFournisseur[] {
    const ligneCommandeFournisseurs: ILigneCommandeFournisseur[] = ligneCommandeFournisseursToCheck.filter(isPresent);
    if (ligneCommandeFournisseurs.length > 0) {
      const ligneCommandeFournisseurCollectionIdentifiers = ligneCommandeFournisseurCollection.map(
        ligneCommandeFournisseurItem => getLigneCommandeFournisseurIdentifier(ligneCommandeFournisseurItem)!
      );
      const ligneCommandeFournisseursToAdd = ligneCommandeFournisseurs.filter(ligneCommandeFournisseurItem => {
        const ligneCommandeFournisseurIdentifier = getLigneCommandeFournisseurIdentifier(ligneCommandeFournisseurItem);
        if (
          ligneCommandeFournisseurIdentifier == null ||
          ligneCommandeFournisseurCollectionIdentifiers.includes(ligneCommandeFournisseurIdentifier)
        ) {
          return false;
        }
        ligneCommandeFournisseurCollectionIdentifiers.push(ligneCommandeFournisseurIdentifier);
        return true;
      });
      return [...ligneCommandeFournisseursToAdd, ...ligneCommandeFournisseurCollection];
    }
    return ligneCommandeFournisseurCollection;
  }
}
