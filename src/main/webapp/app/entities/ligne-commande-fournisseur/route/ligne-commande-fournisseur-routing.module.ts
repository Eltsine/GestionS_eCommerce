import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LigneCommandeFournisseurComponent } from '../list/ligne-commande-fournisseur.component';
import { LigneCommandeFournisseurDetailComponent } from '../detail/ligne-commande-fournisseur-detail.component';
import { LigneCommandeFournisseurUpdateComponent } from '../update/ligne-commande-fournisseur-update.component';
import { LigneCommandeFournisseurRoutingResolveService } from './ligne-commande-fournisseur-routing-resolve.service';

const ligneCommandeFournisseurRoute: Routes = [
  {
    path: '',
    component: LigneCommandeFournisseurComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LigneCommandeFournisseurDetailComponent,
    resolve: {
      ligneCommandeFournisseur: LigneCommandeFournisseurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LigneCommandeFournisseurUpdateComponent,
    resolve: {
      ligneCommandeFournisseur: LigneCommandeFournisseurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LigneCommandeFournisseurUpdateComponent,
    resolve: {
      ligneCommandeFournisseur: LigneCommandeFournisseurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ligneCommandeFournisseurRoute)],
  exports: [RouterModule],
})
export class LigneCommandeFournisseurRoutingModule {}
