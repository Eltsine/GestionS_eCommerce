import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersonnel, getPersonnelIdentifier } from '../personnel.model';

export type EntityResponseType = HttpResponse<IPersonnel>;
export type EntityArrayResponseType = HttpResponse<IPersonnel[]>;

@Injectable({ providedIn: 'root' })
export class PersonnelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/personnel');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(personnel: IPersonnel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(personnel);
    return this.http
      .post<IPersonnel>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(personnel: IPersonnel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(personnel);
    return this.http
      .put<IPersonnel>(`${this.resourceUrl}/${getPersonnelIdentifier(personnel) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(personnel: IPersonnel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(personnel);
    return this.http
      .patch<IPersonnel>(`${this.resourceUrl}/${getPersonnelIdentifier(personnel) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPersonnel>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPersonnel[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPersonnelToCollectionIfMissing(
    personnelCollection: IPersonnel[],
    ...personnelToCheck: (IPersonnel | null | undefined)[]
  ): IPersonnel[] {
    const personnel: IPersonnel[] = personnelToCheck.filter(isPresent);
    if (personnel.length > 0) {
      const personnelCollectionIdentifiers = personnelCollection.map(personnelItem => getPersonnelIdentifier(personnelItem)!);
      const personnelToAdd = personnel.filter(personnelItem => {
        const personnelIdentifier = getPersonnelIdentifier(personnelItem);
        if (personnelIdentifier == null || personnelCollectionIdentifiers.includes(personnelIdentifier)) {
          return false;
        }
        personnelCollectionIdentifiers.push(personnelIdentifier);
        return true;
      });
      return [...personnelToAdd, ...personnelCollection];
    }
    return personnelCollection;
  }

  protected convertDateFromClient(personnel: IPersonnel): IPersonnel {
    return Object.assign({}, personnel, {
      dateNaissance: personnel.dateNaissance?.isValid() ? personnel.dateNaissance.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateNaissance = res.body.dateNaissance ? dayjs(res.body.dateNaissance) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((personnel: IPersonnel) => {
        personnel.dateNaissance = personnel.dateNaissance ? dayjs(personnel.dateNaissance) : undefined;
      });
    }
    return res;
  }
}
