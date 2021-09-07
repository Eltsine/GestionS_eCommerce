import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMvtStk, getMvtStkIdentifier } from '../mvt-stk.model';

export type EntityResponseType = HttpResponse<IMvtStk>;
export type EntityArrayResponseType = HttpResponse<IMvtStk[]>;

@Injectable({ providedIn: 'root' })
export class MvtStkService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mvt-stks');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(mvtStk: IMvtStk): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mvtStk);
    return this.http
      .post<IMvtStk>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(mvtStk: IMvtStk): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mvtStk);
    return this.http
      .put<IMvtStk>(`${this.resourceUrl}/${getMvtStkIdentifier(mvtStk) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(mvtStk: IMvtStk): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mvtStk);
    return this.http
      .patch<IMvtStk>(`${this.resourceUrl}/${getMvtStkIdentifier(mvtStk) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMvtStk>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMvtStk[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMvtStkToCollectionIfMissing(mvtStkCollection: IMvtStk[], ...mvtStksToCheck: (IMvtStk | null | undefined)[]): IMvtStk[] {
    const mvtStks: IMvtStk[] = mvtStksToCheck.filter(isPresent);
    if (mvtStks.length > 0) {
      const mvtStkCollectionIdentifiers = mvtStkCollection.map(mvtStkItem => getMvtStkIdentifier(mvtStkItem)!);
      const mvtStksToAdd = mvtStks.filter(mvtStkItem => {
        const mvtStkIdentifier = getMvtStkIdentifier(mvtStkItem);
        if (mvtStkIdentifier == null || mvtStkCollectionIdentifiers.includes(mvtStkIdentifier)) {
          return false;
        }
        mvtStkCollectionIdentifiers.push(mvtStkIdentifier);
        return true;
      });
      return [...mvtStksToAdd, ...mvtStkCollection];
    }
    return mvtStkCollection;
  }

  protected convertDateFromClient(mvtStk: IMvtStk): IMvtStk {
    return Object.assign({}, mvtStk, {
      dateMvt: mvtStk.dateMvt?.isValid() ? mvtStk.dateMvt.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateMvt = res.body.dateMvt ? dayjs(res.body.dateMvt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((mvtStk: IMvtStk) => {
        mvtStk.dateMvt = mvtStk.dateMvt ? dayjs(mvtStk.dateMvt) : undefined;
      });
    }
    return res;
  }
}
