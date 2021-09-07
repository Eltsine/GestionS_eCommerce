import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LigneVenteComponent } from '../list/ligne-vente.component';
import { LigneVenteDetailComponent } from '../detail/ligne-vente-detail.component';
import { LigneVenteUpdateComponent } from '../update/ligne-vente-update.component';
import { LigneVenteRoutingResolveService } from './ligne-vente-routing-resolve.service';

const ligneVenteRoute: Routes = [
  {
    path: '',
    component: LigneVenteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LigneVenteDetailComponent,
    resolve: {
      ligneVente: LigneVenteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LigneVenteUpdateComponent,
    resolve: {
      ligneVente: LigneVenteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LigneVenteUpdateComponent,
    resolve: {
      ligneVente: LigneVenteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ligneVenteRoute)],
  exports: [RouterModule],
})
export class LigneVenteRoutingModule {}
