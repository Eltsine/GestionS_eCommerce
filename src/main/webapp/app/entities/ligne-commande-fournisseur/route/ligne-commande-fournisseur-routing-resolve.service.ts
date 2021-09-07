import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILigneCommandeFournisseur, LigneCommandeFournisseur } from '../ligne-commande-fournisseur.model';
import { LigneCommandeFournisseurService } from '../service/ligne-commande-fournisseur.service';

@Injectable({ providedIn: 'root' })
export class LigneCommandeFournisseurRoutingResolveService implements Resolve<ILigneCommandeFournisseur> {
  constructor(protected service: LigneCommandeFournisseurService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILigneCommandeFournisseur> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ligneCommandeFournisseur: HttpResponse<LigneCommandeFournisseur>) => {
          if (ligneCommandeFournisseur.body) {
            return of(ligneCommandeFournisseur.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LigneCommandeFournisseur());
  }
}
