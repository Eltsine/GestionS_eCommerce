import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMvtStk, MvtStk } from '../mvt-stk.model';
import { MvtStkService } from '../service/mvt-stk.service';

@Injectable({ providedIn: 'root' })
export class MvtStkRoutingResolveService implements Resolve<IMvtStk> {
  constructor(protected service: MvtStkService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMvtStk> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((mvtStk: HttpResponse<MvtStk>) => {
          if (mvtStk.body) {
            return of(mvtStk.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MvtStk());
  }
}
