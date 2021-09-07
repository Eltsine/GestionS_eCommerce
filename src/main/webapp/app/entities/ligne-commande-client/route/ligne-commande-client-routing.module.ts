import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LigneCommandeClientComponent } from '../list/ligne-commande-client.component';
import { LigneCommandeClientDetailComponent } from '../detail/ligne-commande-client-detail.component';
import { LigneCommandeClientUpdateComponent } from '../update/ligne-commande-client-update.component';
import { LigneCommandeClientRoutingResolveService } from './ligne-commande-client-routing-resolve.service';

const ligneCommandeClientRoute: Routes = [
  {
    path: '',
    component: LigneCommandeClientComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LigneCommandeClientDetailComponent,
    resolve: {
      ligneCommandeClient: LigneCommandeClientRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LigneCommandeClientUpdateComponent,
    resolve: {
      ligneCommandeClient: LigneCommandeClientRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LigneCommandeClientUpdateComponent,
    resolve: {
      ligneCommandeClient: LigneCommandeClientRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ligneCommandeClientRoute)],
  exports: [RouterModule],
})
export class LigneCommandeClientRoutingModule {}
