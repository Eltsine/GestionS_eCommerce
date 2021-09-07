import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILigneCommandeClient, LigneCommandeClient } from '../ligne-commande-client.model';
import { LigneCommandeClientService } from '../service/ligne-commande-client.service';

@Injectable({ providedIn: 'root' })
export class LigneCommandeClientRoutingResolveService implements Resolve<ILigneCommandeClient> {
  constructor(protected service: LigneCommandeClientService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILigneCommandeClient> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ligneCommandeClient: HttpResponse<LigneCommandeClient>) => {
          if (ligneCommandeClient.body) {
            return of(ligneCommandeClient.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LigneCommandeClient());
  }
}
