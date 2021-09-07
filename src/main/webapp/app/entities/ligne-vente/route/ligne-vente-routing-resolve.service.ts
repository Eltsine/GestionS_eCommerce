import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILigneVente, LigneVente } from '../ligne-vente.model';
import { LigneVenteService } from '../service/ligne-vente.service';

@Injectable({ providedIn: 'root' })
export class LigneVenteRoutingResolveService implements Resolve<ILigneVente> {
  constructor(protected service: LigneVenteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILigneVente> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ligneVente: HttpResponse<LigneVente>) => {
          if (ligneVente.body) {
            return of(ligneVente.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LigneVente());
  }
}
